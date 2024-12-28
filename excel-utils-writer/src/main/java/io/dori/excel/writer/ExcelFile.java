package io.dori.excel.writer;

import java.io.OutputStream;
import java.util.List;

public interface ExcelFile<T> {
    void write(OutputStream outputStream);

    void addRow(T row);

    void addRows(List<T> rows);
}
