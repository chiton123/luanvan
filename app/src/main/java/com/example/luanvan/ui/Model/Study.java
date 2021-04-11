package com.example.luanvan.ui.Model;

import java.io.Serializable;

public class Study implements Serializable {
    private String id;
    private int iduser;
    private String school;
    private String major;
    private String date_start;
    private String date_end;
    private String description;

    public Study(){

    }

    public Study(String id, int iduser, String school, String major, String date_start, String date_end, String description) {
        this.id = id;
        this.iduser = iduser;
        this.school = school;
        this.major = major;
        this.date_start = date_start;
        this.date_end = date_end;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
