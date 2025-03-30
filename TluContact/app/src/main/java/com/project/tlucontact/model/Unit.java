package com.project.tlucontact.model;

import java.io.Serializable;

public class Unit implements Serializable {
    private String uid;
    private String name;
    private String address;
    private int logoUnit;
    private String phone;
    private String email;
    //    private String fax;
    private String type; // Loại đơn vị: "Khoa", "Phòng", "Trung tâm"...

    public Unit(String uid, String name, String address, int logoUnit, String phone, String email, String type) {
        this.uid = uid;
        this.name = name;
        this.address = address;
        this.logoUnit = logoUnit;
        this.phone = phone;
        this.email = email;
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLogoUnit() {
        return logoUnit;
    }

    public void setLogoURL(int logoUnit) {
        this.logoUnit = logoUnit;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
