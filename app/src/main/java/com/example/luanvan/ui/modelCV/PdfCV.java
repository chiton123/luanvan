package com.example.luanvan.ui.modelCV;

public class PdfCV {
    public String uid;
    public String name;
    public String url;
    public String key;

    public PdfCV() {
    }

    public PdfCV(String uid, String name, String url, String key) {
        this.uid = uid;
        this.name = name;
        this.url = url;
        this.key = key;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
