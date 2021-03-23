package com.example.luanvan.ui.Model;

public class NotificationRecruiter {
    private int id;
    private Integer ap_id;
    private int job_id;
    private String type_notification;
    private int type_user;
    private int id_user;
    private String content;
    private int status;
    private int kind;
    private String img;
    private String date_read;
    private int ap_status;
    private String ap_note;
    public NotificationRecruiter(int id, Integer ap_id, int job_id, String type_notification, int type_user, int id_user, String content, int status,
                                 int kind, String img, String date_read,
                                 int ap_status, String ap_note) {
        this.id = id;
        this.ap_id = ap_id;
        this.job_id = job_id;
        this.type_notification = type_notification;
        this.type_user = type_user;
        this.id_user = id_user;
        this.content = content;
        this.status = status;
        this.kind = kind;
        this.img = img;
        this.date_read = date_read;
        this.ap_status = ap_status;
        this.ap_note = ap_note;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public int getAp_status() {
        return ap_status;
    }

    public void setAp_status(int ap_status) {
        this.ap_status = ap_status;
    }

    public String getAp_note() {
        return ap_note;
    }

    public void setAp_note(String ap_note) {
        this.ap_note = ap_note;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getJob_id() {
        return job_id;
    }

    public void setJob_id(int job_id) {
        this.job_id = job_id;
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
