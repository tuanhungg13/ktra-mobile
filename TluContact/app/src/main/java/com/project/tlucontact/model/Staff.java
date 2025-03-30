package com.project.tlucontact.model;

import java.io.Serializable;

public class Staff implements Serializable {
    private String staffId;
    private String fullName;
    private String position;
    private String phone;
    private String email;
    private int avatar;
    private String address;

    public Staff(String staffId, String fullName, String position, String phone, String email, int avatar, String address) {
        this.staffId = staffId;
        this.fullName = fullName;
        this.position = position;
        this.phone = phone;
        this.email = email;
        this.avatar = avatar;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
}
