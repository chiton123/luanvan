package com.example.luanvan.ui.Model;

public class Skill {
    private String name;
    private int star;
    private String description;

    public Skill(String name, int star, String description) {
        this.name = name;
        this.star = star;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
