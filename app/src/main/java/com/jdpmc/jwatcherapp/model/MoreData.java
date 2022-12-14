package com.jdpmc.jwatcherapp.model;

public class MoreData {
    private int id;
    private String name;
    private String subname;
    private int thumbnail;
    private String url;

    public MoreData() {
    }

    public MoreData(int id, String name, String subname, int thumbnail, String url) {
        this.id = id;
        this.name = name;
        this.subname = subname;
        this.thumbnail = thumbnail;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub() {
        return subname;
    }

    public void setSub(String subname) {
        this.subname = subname;
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
