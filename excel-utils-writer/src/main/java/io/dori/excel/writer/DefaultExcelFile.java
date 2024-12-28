package io.dori.excel.writer;

import io.dori.excel.annotation.Column;
import io.dori.excel.exception.ExtractFieldValueFailureException;
import io.dori.excel.exception.WorkbookWriteFailureException;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DefaultExcelFile<T> implements ExcelFile<T> {
    private static final DateTimeFormatter ZONED_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm:ss");

    private final SXSSFWorkbook workbook;
    private final Sheet sheet;

    private final Class<T> clazz;

    public DefaultExcelFile(Class<T> clazz) {
        this.workbook = new SXSSFWorkbook();
        this.sheet = this.workbook.createSheet("Sheet");
        this.clazz = clazz;

        this.renderHeader();
    }

    @Override
    public void write(OutputStream outputStream) {
        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            throw new WorkbookWriteFailureException(e.getMessage(), e);
        }
    }

    @Override
    public void addRow(T object) {
        var row = sheet.createRow(sheet.getLastRowNum() + 1);

        int col = 0;
        for (Field field : this.clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                var cell = row.createCell(col++);
                cell.setCellValue(getFieldValue(field, object));
            }
        }
    }

    @Override
    public void addRows(List<T> objects) {
        objects.forEach(this::addRow);
    }

    private void renderHeader() {
        var row = sheet.createRow(0);
        var col = 0;

        for (Field field : this.clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                var annotation = field.getAnnotation(Column.class);
                var cell = row.createCell(col++);
                cell.setCellValue(annotation.headerName());
            }
        }
    }

    private String getFieldValue(Field field, Object object) {
        try {
            var method = clazz.getMethod("get" + StringUtils.capitalize(field.getName()));
            var invokeResult = method.invoke(object);

            return getObjectValue(invokeResult);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new ExtractFieldValueFailureException(e.getMessage(), e);
        }
    }

    private String getObjectValue(Object object) {
        if (object == null) {
            return StringUtils.EMPTY;
        }

        if (object instanceof String stringObject) {
            return stringObject;
        } else if (object instanceof Number numberObject) {
            return numberObject.toString();
        } else if (object instanceof Boolean booleanObject) {
            return BooleanUtils.toString(booleanObject, "true", "false");
        } else if (object instanceof Character characterObject) {
            return characterObject.toString();
        } else if (object instanceof LocalDateTime localDateTimeObject) {
            return localDateTimeObject.format(DATE_TIME_FORMATTER);
        } else if (object instanceof LocalDate localDateObject) {
            return localDateObject.format(DATE_FORMATTER);
        } else if (object instanceof LocalTime localTimeObject) {
            return localTimeObject.format(TIME_FORMATTER);
        } else if (object instanceof ZonedDateTime zonedDateTimeObject) {
            return zonedDateTimeObject.format(ZONED_DATE_TIME_FORMATTER);
        } else if (object instanceof Enum<?> enumObject) {
            return enumObject.name();
        } else {
            throw new IllegalArgumentException("Unsupported field. type=" + object.getClass().getName());
        }
    }
}
