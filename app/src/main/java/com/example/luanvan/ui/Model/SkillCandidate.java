package com.example.luanvan.ui.Model;

public class SkillCandidate {
    private int id;
    private String name;
    private int check; // kiểm tra nó check hay chưa

    public SkillCandidate(int id, String name, int check) {
        this.id = id;
        this.name = name;
        this.check = check;
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

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }
}
