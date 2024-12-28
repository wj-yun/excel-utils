package io.dori.excel;

import io.dori.excel.annotation.Column;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ReadOnlyExcelFileTest {

    @Test
    void write_fail() {
        var readOnlyExcelFile = ExcelFile.readOnly(Dummy.class);
        var dummy = new Dummy("John", 30);
        assertThrows(UnsupportedOperationException.class, () -> readOnlyExcelFile.addRow(dummy));
    }

    public static class Dummy {
        @Column(headerName = "name")
        private String name;

        @Column(headerName = "age")
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Dummy(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}
