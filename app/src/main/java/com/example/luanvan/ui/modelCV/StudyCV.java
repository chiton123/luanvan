package com.example.luanvan.ui.modelCV;

public class StudyCV {
    private String id;
    private String school;
    private String major;
    private String start;
    private String end;
    private String description;

    public StudyCV() {
    }

    public StudyCV(String id, String school, String major, String start, String end, String description) {
        this.id = id;
        this.school = school;
        this.major = major;
        this.start = start;
        this.end = end;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
