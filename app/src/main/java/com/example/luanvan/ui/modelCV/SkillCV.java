package com.example.luanvan.ui.modelCV;

public class SkillCV {
    private String name;
    private float star;
    private String id;
    public SkillCV() {
    }

    public SkillCV(String name, float star, String id) {
        this.name = name;
        this.star = star;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}
