package com.jdpmc.jwatcherapp.model;

public class Emergency {
    private int id;
    private String name;
    private String sub;
    private int thumbnail;

    public Emergency() {
    }

    public Emergency(int id, String name, String sub, int thumbnail) {
        this.id = id;
        this.name = name;
        this.sub = sub;
        this.thumbnail = thumbnail;
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
}