package com.example.luanvan.ui.modelCV;

public class UserCV {
    private String username;
    private String position;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private String birthday;

    public UserCV() {
    }

    public UserCV(String username, String position, String email, String phone, String address, String gender, String birthday) {
        this.username = username;
        this.position = position;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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
