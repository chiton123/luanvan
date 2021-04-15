package com.example.luanvan.ui.Model;

import java.io.Serializable;

public class ScheduleCandidate implements Serializable {
    private int id;
    private int id_recruiter;
    private int id_job;
    private String job_name;
    private String company_name;
    private int type;
    private String date;
    private String start_hour;
    private String end_hour;
    private String note;
    private String note_candidate;
    private int status;

    public ScheduleCandidate(int id, int id_recruiter, int id_job, String job_name, String company_name, int type, String date,
                             String start_hour, String end_hour, String note, String note_candidate, int status) {
        this.id = id;
        this.id_recruiter = id_recruiter;
        this.id_job = id_job;
        this.job_name = job_name;
        this.company_name = company_name;
        this.type = type;
        this.date = date;
        this.start_hour = start_hour;
        this.end_hour = end_hour;
        this.note = note;
        this.note_candidate = note_candidate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_recruiter() {
        return id_recruiter;
    }

    public void setId_recruiter(int id_recruiter) {
        this.id_recruiter = id_recruiter;
    }

    public int getId_job() {
        return id_job;
    }

    public void setId_job(int id_job) {
        this.id_job = id_job;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart_hour() {
        return start_hour;
    }

    public void setStart_hour(String start_hour) {
        this.start_hour = start_hour;
    }

    public String getEnd_hour() {
        return end_hour;
    }

    public void setEnd_hour(String end_hour) {
        this.end_hour = end_hour;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote_candidate() {
        return note_candidate;
    }

    public void setNote_candidate(String note_candidate) {
        this.note_candidate = note_candidate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
