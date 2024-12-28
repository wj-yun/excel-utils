package io.dori.excel;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface ExcelFile<T> {
    void write(OutputStream outputStream);

    List<T> read(InputStream inputStream);

    void addRow(T row);

    void addRows(Iterable<T> rows);

    static <T> ExcelFile<T> readOnly(Class<T> clazz) { return new ReadOnlyExcelFile<>(clazz); }

    static <T> ExcelFile<T> writable(Class<T> clazz) { return new WritableExcelFile<>(clazz); }

    static void uoe() { throw new UnsupportedOperationException(); }
}
