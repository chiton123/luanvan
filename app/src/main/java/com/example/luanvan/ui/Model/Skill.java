package com.example.luanvan.ui.Model;

import java.io.Serializable;

public class Skill implements Serializable {
    private String id;
    private int iduser;
    private int idskill;
    private String name;
    private float star;
    private String description;

    public Skill() {
    }

    public Skill(String id, int iduser, int idskill, String name, float star, String description) {
        this.id = id;
        this.iduser = iduser;
        this.idskill = idskill;
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

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public int getIdskill() {
        return idskill;
    }

    public void setIdskill(int idskill) {
        this.idskill = idskill;
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
