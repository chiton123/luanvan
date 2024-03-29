package com.example.luanvan.ui.Model;

import java.io.Serializable;

public class JobList implements Serializable {
    private int id;
    private String name;
    private int idcompany;
    private String company_name;
    private String img;
    private int idtype;
    private int idprofession;
    private String start_date;
    private String end_date;
    private int salary_min;
    private int salary_max;
    private int idarea;
    private String area;
    private String address;
    private String experience;
    private int number;
    private String description;
    private String requirement;
    private String benefit;
    private int status;
    private String type_job;
    private String note_reject;
    private int totalDocument;
    private int new_document;
    private int interview;
    private int work;
    private int skip;

    public JobList(int id, String name,int idcompany, String img,String address, int idtype, int idprofession, String start_date, String end_date,
                   int salary_min, int salary_max, int idarea, String area,String experience, int number, String description,
                   String requirement, String benefit, int status,String company_name, String type_job, String note_reject,
                   int totalDocument, int new_document, int interview, int work, int skip) {
        this.id = id;
        this.name = name;
        this.idcompany = idcompany;
        this.company_name = company_name;
        this.img = img;
        this.idtype = idtype;
        this.idprofession = idprofession;
        this.start_date = start_date;
        this.end_date = end_date;
        this.salary_min = salary_min;
        this.salary_max = salary_max;
        this.idarea = idarea;
        this.area = area;
        this.address = address;
        this.experience = experience;
        this.number = number;
        this.description = description;
        this.requirement = requirement;
        this.benefit = benefit;
        this.status = status;
        this.type_job = type_job;
        this.note_reject = note_reject;
        this.totalDocument = totalDocument;
        this.new_document = new_document;
        this.interview = interview;
        this.work = work;
        this.skip = skip;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNote_reject() {
        return note_reject;
    }

    public void setNote_reject(String note_reject) {
        this.note_reject = note_reject;
    }

    public int getSalary_max() {
        return salary_max;
    }

    public void setSalary_max(int salary_max) {
        this.salary_max = salary_max;
    }

    public int getSalary_min() {
        return salary_min;
    }

    public void setSalary_min(int salary_min) {
        this.salary_min = salary_min;
    }

    public int getTotalDocument() {
        return totalDocument;
    }

    public void setTotalDocument(int totalDocument) {
        this.totalDocument = totalDocument;
    }

    public int getNew_document() {
        return new_document;
    }

    public void setNew_document(int new_document) {
        this.new_document = new_document;
    }

    public int getInterview() {
        return interview;
    }

    public void setInterview(int interview) {
        this.interview = interview;
    }

    public int getWork() {
        return work;
    }

    public void setWork(int work) {
        this.work = work;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
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
