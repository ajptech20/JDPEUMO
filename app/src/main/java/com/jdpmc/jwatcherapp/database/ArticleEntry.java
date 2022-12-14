package com.jdpmc.jwatcherapp.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class ArticleEntry {


    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "articleid")
    private String articleid;

    @ColumnInfo(name = "picname")
    private String picname;

    @ColumnInfo(name = "posttitle")
    private String posttitle;

    @ColumnInfo(name = "postnotes")
    private String postnotes;

    @ColumnInfo(name = "postcategory")
    private String postcategory;

    @ColumnInfo(name = "articletype")
    private String articletype;

    @ColumnInfo(name = "videourl")
    private String videourl;

    @Ignore
    public ArticleEntry(String articleid, String picname, String posttitle, String postnotes, String postcategory, String articletype, String videourl) {
        this.articleid = articleid;
        this.picname = picname;
        this.posttitle = posttitle;
        this.postnotes = postnotes;
        this.postcategory = postcategory;
        this.articletype = articletype;
        this.videourl = videourl;
    }

    public ArticleEntry(int id, String articleid, String picname, String posttitle, String postnotes, String postcategory, String articletype, String videourl) {
        this.id = id;
        this.articleid = articleid;
        this.picname = picname;
        this.posttitle = posttitle;
        this.postnotes = postnotes;
        this.postcategory = postcategory;
        this.articletype = articletype;
        this.videourl = videourl;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getArticleid() { return articleid; }

    public void setArticleid(String articleid) {
        this.articleid = articleid;
    }

    public String getPicname() {
        return picname;
    }

    public void setPicname(String picname) {
        this.picname = picname;
    }

    public String getPosttitle() {
        return posttitle;
    }

    public void setPosttitle(String posttitle) {
        this.posttitle = posttitle;
    }

    public String getPostnotes() {
        return postnotes;
    }

    public void setPostnotes(String postnotes) {
        this.postnotes = postnotes;
    }

    public String getPostcategory() {
        return postcategory;
    }

    public void setPostcategory(String postcategory) {
        this.postcategory = postcategory;
    }

    public String getArticletype() {
        return articletype;
    }

    public void setArticletype(String articletype) {
        this.articletype = articletype;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }
}
