package com.example.luanvan.ui.Model;

public class Notification {
    private int id;
    private int ap_id;
    private String type_notification;
    private int type_user;
    private int id_user;
    private String content;
    private String img;
    private String date_read;

    public Notification(int id, int ap_id, String type_notification, int type_user, int id_user, String content, String img, String date_read) {
        this.id = id;
        this.ap_id = ap_id;
        this.type_notification = type_notification;
        this.type_user = type_user;
        this.id_user = id_user;
        this.content = content;
        this.img = img;
        this.date_read = date_read;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getType_notification() {
        return type_notification;
    }

    public void setType_notification(String type_notification) {
        this.type_notification = type_notification;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAp_id() {
        return ap_id;
    }

    public void setAp_id(int ap_id) {
        this.ap_id = ap_id;
    }



    public int getType_user() {
        return type_user;
    }

    public void setType_user(int type_user) {
        this.type_user = type_user;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate_read() {
        return date_read;
    }

    public void setDate_read(String date_read) {
        this.date_read = date_read;
    }
}
