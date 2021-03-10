package com.example.luanvan.ui.Model;

import java.io.Serializable;

public class Job_Apply implements Serializable {
    private int id;
    private String name;
    private int idcompany;
    private int id_recruiter;
    private int id_cv;
    private String company_name;
    private String img;
    private int idtype;
    private int idprofession;
    private String start_date;
    private String end_date;
    private int salary;
    private int idarea;
    private String address;
    private String experience;
    private int number;
    private String description;
    private String requirement;
    private String benefit;
    private int status;
    private String type_job;

    public Job_Apply() {
    }

    public Job_Apply(int id, String name, int idcompany, int id_recruiter, int id_cv, String img, String address, int idtype, int idprofession, String start_date, String end_date,
                     int salary, int idarea, String experience, int number, String description,
                     String requirement, String benefit, int status, String company_name, String type_job) {
        this.id = id;
        this.name = name;
        this.idcompany = idcompany;
        this.id_recruiter = id_recruiter;
        this.id_cv = id_cv;
        this.img = img;
        this.address = address;
        this.idtype = idtype;
        this.idprofession = idprofession;
        this.start_date = start_date;
        this.end_date = end_date;
        this.salary = salary;
        this.idarea = idarea;
        this.experience = experience;
        this.number = number;
        this.description = description;
        this.requirement = requirement;
        this.benefit = benefit;
        this.status = status;
        this.company_name = company_name;
        this.type_job = type_job;
    }

    public int getId_cv() {
        return id_cv;
    }

    public void setId_cv(int id_cv) {
        this.id_cv = id_cv;
    }

    public int getId_recruiter() {
        return id_recruiter;
    }

    public void setId_recruiter(int id_recruiter) {
        this.id_recruiter = id_recruiter;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
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
