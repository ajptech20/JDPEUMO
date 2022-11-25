package com.a2tocsolutions.nispsasapp.model;

public class Verify {
    private int id;
    private int thumbnail;
    private String name;

    public Verify() {
    }

    public Verify(int id, int thumbnail, String name) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
