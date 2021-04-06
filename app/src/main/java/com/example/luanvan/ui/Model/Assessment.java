package com.example.luanvan.ui.Model;

public class Assessment {
    private int id;
    private int idcompany;
    private int iduser;
    private String username;
    private String remark;
    private float star;

    public Assessment(int id, int idcompany, int iduser, String username, String remark, float star) {
        this.id = id;
        this.idcompany = idcompany;
        this.iduser = iduser;
        this.username = username;
        this.remark = remark;
        this.star = star;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdcompany() {
        return idcompany;
    }

    public void setIdcompany(int idcompany) {
        this.idcompany = idcompany;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }
}
