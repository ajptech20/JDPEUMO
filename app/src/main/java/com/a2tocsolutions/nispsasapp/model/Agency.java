package com.a2tocsolutions.nispsasapp.model;

public class Agency {

    private int thumbnail;
    private String url;
    private String title;

    public Agency() {
    }

    public Agency(int thumbnail, String url, String title) {
        this.thumbnail = thumbnail;
        this.url = url;
        this.title =title;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {  return title;  }

    public void setTitle(String title) {
        this.title = title;
    }
}
