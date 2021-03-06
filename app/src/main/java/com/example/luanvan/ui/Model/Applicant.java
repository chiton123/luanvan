package com.example.luanvan.ui.Model;

import java.io.Serializable;

public class Applicant implements Serializable {
    private int id;
    private int job_id;
    private String job_name;
    private int user_id;
    private String user_id_f;
    private String username;
    private String email;
    private String address;
    private String phone;
    private int cv_id;
    private int status;
    private String note;
    private String date;

    public Applicant() {
    }

    public Applicant(int id, int job_id, String job_name, int user_id, String user_id_f, String username, String email, String address, String phone, int cv_id, int status,
                     String note, String date) {
        this.id = id;
        this.job_id = job_id;
        this.job_name = job_name;
        this.user_id = user_id;
        this.user_id_f = user_id_f;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.cv_id = cv_id;
        this.status = status;
        this.note = note;
        this.date = date;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJob_id() {
        return job_id;
    }

    public void setJob_id(int job_id) {
        this.job_id = job_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCv_id() {
        return cv_id;
    }

    public void setCv_id(int cv_id) {
        this.cv_id = cv_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
