package com.example.luanvan.ui.modelCV;

public class ExperienceCV {
    private String id;
    private String company;
    private String position;
    private String start;
    private String end;
    private String description;

    public ExperienceCV() {
    }

    public ExperienceCV(String id, String company, String position, String start, String end, String description) {
        this.id = id;
        this.company = company;
        this.position = position;
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
