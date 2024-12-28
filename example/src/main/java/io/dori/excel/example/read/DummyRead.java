package io.dori.excel.example.read;

import io.dori.excel.ExcelFile;
import io.dori.excel.ReadOnlyExcelFile;

import java.io.FileInputStream;

public class DummyRead {

    public static void main(String[] args) {
        var excelFilePath = "example/src/main/resources/read_example.xlsx";

        try (var fileInputStream = new FileInputStream(excelFilePath)) {
            var excelFile = ExcelFile.readOnly(RecordDummy.class);
            var items = excelFile.read(fileInputStream);

            items.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
