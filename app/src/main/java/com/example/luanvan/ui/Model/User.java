package com.example.luanvan.ui.Model;

public class User {
    private int id;
    private int idposition;
    private String position;
    private String name;
    private String birthday;
    private int gender;
    private String address;
    private String email;
    private String introduction;
    private int phone;
    private int status;
    private int mode;

    public User(){

    }

    public User(int id, int idposition, String position, String name, String birthday, int gender, String address,
                String email, String introduction, int phone, int status, int mode) {
        this.id = id;
        this.idposition = idposition;
        this.position = position;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
        this.email = email;
        this.introduction = introduction;
        this.phone = phone;
        this.status = status;
        this.mode = mode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdposition() {
        return idposition;
    }

    public void setIdposition(int idposition) {
        this.idposition = idposition;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
