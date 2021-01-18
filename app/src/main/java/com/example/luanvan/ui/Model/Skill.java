package com.example.luanvan.ui.Model;

import java.io.Serializable;

public class Skill implements Serializable {
    private String id;
    private String uid;
    private String name;
    private float star;
    private String description;

    public Skill() {
    }

    public Skill(String id, String uid, String name, float star, String description) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.star = star;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
