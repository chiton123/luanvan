package com.example.luanvan.ui.Model;

import java.io.Serializable;

public class Skill implements Serializable {
    private int id;
    private String name;
    private float star;
    private String description;

    public Skill(int id, String name, float star, String description) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.description = description;
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
