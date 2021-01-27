package com.example.luanvan.ui.modelCV;

public class SkillCV {
    private String name;
    private float star;
    private String key;
    public SkillCV() {
    }

    public SkillCV(String name, float star, String key) {
        this.name = name;
        this.star = star;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
