package com.example.luanvan.ui.Model;

import java.io.Serializable;

public class Experience implements Serializable {
    private String id;
    private int iduser;
    private String company;
    private String position;
    private String date_start;
    private String date_end;
    private String description;

    public Experience() {
    }

    public Experience(String id, int iduser, String company, String position, String date_start, String date_end, String description) {
        this.id = id;
        this.iduser = iduser;
        this.company = company;
        this.position = position;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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
