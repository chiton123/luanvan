package com.example.luanvan.ui.Model;

import java.io.Serializable;

public class Schedule implements Serializable {
    private int id;
    private int id_recruiter;
    private int id_job;
    private String job_name;
    private int id_user;
    private String username;
    private int type;
    private String date;
    private String start_hour;
    private String end_hour;
    private String note;
    private String note_candidate;
    private int status;

    public Schedule(int id, int id_recruiter, int id_job,String job_name, int id_user, String username, int type, String date,
                    String start_hour, String end_hour, String note, String note_candidate, int status) {
        this.id = id;
        this.id_recruiter = id_recruiter;
        this.id_job = id_job;
        this.job_name = job_name;
        this.id_user = id_user;
        this.username = username;
        this.type = type;
        this.date = date;
        this.start_hour = start_hour;
        this.end_hour = end_hour;
        this.note = note;
        this.note_candidate = note_candidate;
        this.status = status;
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

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
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

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
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
}
