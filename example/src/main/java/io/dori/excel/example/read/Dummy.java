package io.dori.excel.example.read;

import io.dori.excel.common.annotation.Column;

public class Dummy {
    @Column(headerName = "name")
    private String name;

    @Column(headerName = "age")
    private Integer age;

    @Column(headerName = "phone")
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Dummy{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                '}';
    }
}
