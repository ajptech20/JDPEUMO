package com.jdpmc.jwatcherapp.model;

public class Complaint {
    private int id;
    private int thumbnail;
    private String name;
    private String sub;

    public Complaint() {
    }

    public Complaint(int id, int thumbnail,  String name) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.sub = sub;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
