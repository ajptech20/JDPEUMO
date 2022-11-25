package com.a2tocsolutions.nispsasapp.model;

public class Banks {
    private int id;
    private String name;
    private int thumbnail;
    private String url;

    public Banks() {
    }

    public Banks(int id, String name, int thumbnail, String url) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
