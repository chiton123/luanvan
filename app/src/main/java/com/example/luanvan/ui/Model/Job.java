package com.example.luanvan.ui.Model;

import java.io.Serializable;

public class Job implements Serializable {
    private int id;
    private String name;
    private int idcompany;
    private String company_name;
    private String img;
    private int idtype;
    private int idprofession;
    private String date;
    private int salary;
    private int idarea;
    private String area;
    private int gender;
    private String experience;
    private int number;
    private String position;
    private String description;
    private String requirement;
    private String benefit;
    private int status;
    private String type_job;
    public Job(int id, String name,int idcompany, String img,String area, int idtype, int idprofession, String date,
               int salary, int idarea, int gender, String experience, int number, String position, String description,
               String requirement, String benefit, int status,String company_name, String type_job) {
        this.id = id;
        this.name = name;
        this.idcompany = idcompany;
        this.img = img;
        this.area = area;
        this.idtype = idtype;
        this.idprofession = idprofession;
        this.date = date;
        this.salary = salary;
        this.idarea = idarea;
        this.gender = gender;
        this.experience = experience;
        this.number = number;
        this.position = position;
        this.description = description;
        this.requirement = requirement;
        this.benefit = benefit;
        this.status = status;
        this.company_name = company_name;
        this.type_job = type_job;
    }

    public String getType_job() {
        return type_job;
    }

    public void setType_job(String type_job) {
        this.type_job = type_job;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
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

    public int getIdcompany() {
        return idcompany;
    }

    public void setIdcompany(int idcompany) {
        this.idcompany = idcompany;
    }

    public int getIdtype() {
        return idtype;
    }

    public void setIdtype(int idtype) {
        this.idtype = idtype;
    }

    public int getIdprofession() {
        return idprofession;
    }

    public void setIdprofession(int idprofession) {
        this.idprofession = idprofession;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getIdarea() {
        return idarea;
    }

    public void setIdarea(int idarea) {
        this.idarea = idarea;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
