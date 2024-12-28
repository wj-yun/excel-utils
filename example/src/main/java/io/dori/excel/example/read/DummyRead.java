package io.dori.excel.example.read;

import io.dori.excel.reader.ExcelFile;

import java.io.FileInputStream;

public class DummyRead {

    public static void main(String[] args) {
        var excelFilePath = "example/src/main/resources/read_example.xlsx";

        try (var fileInputStream = new FileInputStream(excelFilePath)) {
            var excelFile = new ExcelFile<>(fileInputStream, Dummy.class);
            var items = excelFile.read();

            items.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
