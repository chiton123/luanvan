package com.example.luanvan.ui.Model;

public class UserSearch {
    private int iduser;
    private int idposition;
    private String position;
    private int idcv;
    private String user_id_f;
    private String username;
    private String birthday;
    private int gender;
    private String address;
    private String email;
    private String introduction;
    private int phone;
    private int mode;
    private String experience;
    private String study;
    private int idarea;
    private String area;

    public UserSearch(int iduser, int idposition, String position, int idcv, String user_id_f, String username, String birthday, int gender, String address, String email,
                      String introduction, int phone, int mode, String experience, String study, int idarea, String area) {
        this.iduser = iduser;
        this.idposition = idposition;
        this.position = position;
        this.idcv = idcv;
        this.user_id_f = user_id_f;
        this.username = username;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
        this.email = email;
        this.introduction = introduction;
        this.phone = phone;
        this.mode = mode;
        this.experience = experience;
        this.study = study;
        this.idarea = idarea;
        this.area = area;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
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

    public int getIdcv() {
        return idcv;
    }

    public void setIdcv(int idcv) {
        this.idcv = idcv;
    }

    public String getUser_id_f() {
        return user_id_f;
    }

    public void setUser_id_f(String user_id_f) {
        this.user_id_f = user_id_f;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getStudy() {
        return study;
    }

    public void setStudy(String study) {
        this.study = study;
    }

    public int getIdarea() {
        return idarea;
    }

    public void setIdarea(int idarea) {
        this.idarea = idarea;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
