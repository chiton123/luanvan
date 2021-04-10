package com.example.luanvan.ui.Model;

public class UserApplicant {
    private int id;
    private int ap_id;
    private String name;
    private String birthday;
    private int gender;
    private String address;
    private String email;
    private String introduction;
    private String position;
    private int phone;
    private int status;
    private int idcv;
    private int mode;
    public UserApplicant(){

    }

    public UserApplicant(int id, int ap_id, String name, String birthday, int gender, String address, String email, String introduction, String position, int phone, int status
            ,int idcv, int mode ) {
        this.id = id;
        this.ap_id = ap_id;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
        this.email = email;
        this.introduction = introduction;
        this.position = position;
        this.phone = phone;
        this.status = status;
        this.idcv = idcv;
        this.mode = mode;
    }

    public int getIdcv() {
        return idcv;
    }

    public void setIdcv(int idcv) {
        this.idcv = idcv;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getAp_id() {
        return ap_id;
    }

    public void setAp_id(int ap_id) {
        this.ap_id = ap_id;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
