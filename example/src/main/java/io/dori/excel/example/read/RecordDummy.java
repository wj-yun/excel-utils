package io.dori.excel.example.read;

import io.dori.excel.annotation.Column;

public record RecordDummy(
        @Column(headerName = "name")
        String name,
        @Column(headerName = "age")
        int age,
        @Column(headerName = "phone")
        String phone
) {  }
