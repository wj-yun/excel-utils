package io.dori.excel;

import io.dori.excel.annotation.Column;
import io.dori.excel.exception.InstanceCreationFailureException;
import io.dori.excel.exception.PropertySetterNotFoundException;
import io.dori.excel.exception.WorkbookReadFailureException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ReadOnlyExcelFile<T> implements ExcelFile<T> {
    private static final int HEADER_ROW_INDEX = 0;

    private final Class<T> clazz;

    public ReadOnlyExcelFile(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public List<T> read(InputStream inputStream) {
        try (var workbook = new XSSFWorkbook(inputStream)) {
            var sheet = workbook.getSheetAt(0);
            var headers = readHeaders(sheet);

            var properties = Stream.of(clazz.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Column.class))
                    .toList();

            return StreamSupport.stream(sheet.spliterator(), false)
                    .filter(row -> row.getRowNum() > HEADER_ROW_INDEX)
                    .map(row -> readRow(row, headers, clazz, properties))
                    .toList();
        } catch (IOException e) {
            throw new WorkbookReadFailureException("Failed to read the Excel file. message=" + e.getMessage());
        }
    }

    @Override
    public void write(OutputStream outputStream) { ExcelFile.uoe();}

    @Override
    public void addRow(T row) { ExcelFile.uoe(); }

    @Override
    public void addRows(Iterable<T> rows) { ExcelFile.uoe(); }

    private Map<String, Integer> readHeaders(Sheet sheet) {
        var headerRow = sheet.getRow(HEADER_ROW_INDEX);
        var headers = new HashMap<String, Integer>();
        headerRow.cellIterator().forEachRemaining(cell -> headers.put(cell.getStringCellValue(), cell.getColumnIndex()));
        return headers;
    }

    private T readRow(Row row, Map<String, Integer> headers, Class<T> clazz, List<Field> properties) {
        var item = createInstance(clazz);

        properties.forEach(property -> {
            var cellIndex = headers.get(property.getAnnotation(Column.class).headerName());
            var cell = row.getCell(cellIndex);
            var value = readCell(cell);

            try {
                var setterMethodName = "set" + property.getName().substring(0, 1).toUpperCase() + property.getName().substring(1);
                var setter = clazz.getMethod(setterMethodName, property.getType());
                setter.invoke(item, writeValue(value, property));
            } catch (Exception e) {
                throw new PropertySetterNotFoundException("Failed to set a value to the field. property=" + property.getName());
            }
        });

        return item;
    }

    private String readCell(Cell cell) {
        return switch (cell.getCellType()) {
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case STRING -> cell.getStringCellValue();
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            case BLANK -> "";
            default -> throw new IllegalArgumentException("Unsupported cell type. cellType=" + cell.getCellType());
        };
    }

    private Object writeValue(String value, Field field) {
        return switch (field.getType().getName()) {
            case "int", "java.lang.Integer" -> Double.valueOf(value).intValue();
            case "long", "java.lang.Long" -> Double.valueOf(value).longValue();
            case "double", "java.lang.Double" -> Double.parseDouble(value);
            case "float", "java.lang.Float" -> Float.parseFloat(value);
            case "boolean", "java.lang.Boolean" -> Boolean.parseBoolean(value);
            case "java.lang.String" -> value;
            case "java.time.LocalDate" -> java.time.LocalDate.parse(value);
            case "java.time.LocalDateTime" -> java.time.LocalDateTime.parse(value);
            case "java.time.LocalTime" -> java.time.LocalTime.parse(value);
            default -> throw new IllegalArgumentException("Unsupported field type. field=" + field.getName());
        };
    }

    private T createInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new InstanceCreationFailureException("Failed to create an instance of the class. class=" + clazz.getName());
        }
    }
}
