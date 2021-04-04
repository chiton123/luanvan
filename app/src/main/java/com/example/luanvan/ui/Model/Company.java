package com.example.luanvan.ui.Model;

import java.io.Serializable;

public class Company implements Serializable {
    private int id;
    private String name;
    private String introduction;
    private String address;
    private int idarea;
    private int idrecruiter;
    private String image;
    private String website;
    private int number_job;
    private int status;
    private double vido;
    private double kinhdo;

    public Company(int id, String name, String introduction, String address, int idarea, int idrecruiter,
                   String image, String website, int number_job, int status, double vido, double kinhdo) {
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.address = address;
        this.idarea = idarea;
        this.idrecruiter = idrecruiter;
        this.image = image;
        this.website = website;
        this.number_job = number_job;
        this.status = status;
        this.vido = vido;
        this.kinhdo = kinhdo;
    }

    public int getNumber_job() {
        return number_job;
    }

    public void setNumber_job(int number_job) {
        this.number_job = number_job;
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIdarea() {
        return idarea;
    }

    public void setIdarea(int idarea) {
        this.idarea = idarea;
    }

    public int getIdrecruiter() {
        return idrecruiter;
    }

    public void setIdrecruiter(int idrecruiter) {
        this.idrecruiter = idrecruiter;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getVido() {
        return vido;
    }

    public void setVido(double vido) {
        this.vido = vido;
    }

    public double getKinhdo() {
        return kinhdo;
    }

    public void setKinhdo(double kinhdo) {
        this.kinhdo = kinhdo;
    }
}
