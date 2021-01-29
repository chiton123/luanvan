package com.example.luanvan.ui.Model;

public class UserCV {
    private String username;
    private String position;
    private String email;
    private String phone;
    private String address;

    public UserCV() {
    }

    public UserCV(String username, String position, String email, String phone, String address) {
        this.username = username;
        this.position = position;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
