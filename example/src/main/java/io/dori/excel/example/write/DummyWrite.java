package io.dori.excel.example.write;

import io.dori.excel.ExcelFile;

import java.io.FileOutputStream;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class DummyWrite {

    public static void main(String[] args) {
        var dummies = new ArrayList<Dummy>();
        for (int i = 0; i < 500000; i++) {
            dummies.add(new Dummy("USER_" + i, "USER_" + i + "@example.com", "010-0000-0000", ZonedDateTime.now()));
        }

        var excelFile = ExcelFile.writable(Dummy.class);
        excelFile.addRows(dummies);

        try (var outputStream = new FileOutputStream("dummy.xlsx")) {
            excelFile.write(outputStream);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to write excel file", e);
        }
    }
}
