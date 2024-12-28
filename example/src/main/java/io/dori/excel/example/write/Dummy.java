package io.dori.excel.example.write;

import io.dori.excel.common.annotation.Column;

import java.time.ZonedDateTime;

public class Dummy {
    @Column(headerName = "Name")
    private final String name;

    @Column(headerName = "Email")
    private final String email;

    @Column(headerName = "Phone")
    private final String phone;

    @Column(headerName = "createdAt")
    private final ZonedDateTime createdAt;

    public Dummy(String name, String email, String phone, ZonedDateTime createdAt) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
