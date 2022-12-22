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

    @ColumnInfo(name = "image")
    String image;

    @ColumnInfo(name = "officername")
    String officername;

    @ColumnInfo(name = "postcomment")
    private String postcomment;

    @ColumnInfo(name = "state")
    String state;

    @ColumnInfo(name = "status")
    String status;

    @ColumnInfo(name = "reptype")
    String reptype;

    @ColumnInfo(name = "area")
    String area;

    @ColumnInfo(name = "like_count")
    String like_count;

    @ColumnInfo(name = "comm_count")
    String comm_count;

    @ColumnInfo(name = "preview")
    private String preview;

    @ColumnInfo(name = "vid")
    String vid;

    @ColumnInfo(name = "date")
    String date;

    @Ignore
    public ArticleEntry(String image, String officername, String postcomment, String state, String status, String reptype, String area,
                        String like_count, String comm_count, String preview, String vid, String date) {
        this.image = image;
        this.officername = officername;
        this.postcomment = postcomment;
        this.state = state;
        this.status = status;
        this.reptype = reptype;
        this.area = area;

        this.like_count = like_count;
        this.comm_count = comm_count;
        this.preview = preview;
        this.vid = vid;
        this.date = date;
    }

    public ArticleEntry(int id, String image, String officername, String postcomment, String state, String status, String reptype, String area,
                        String like_count, String comm_count, String preview, String vid, String date) {
        this.id = id;
        this.image = image;
        this.officername = officername;
        this.postcomment = postcomment;
        this.state = state;
        this.status = status;
        this.reptype = reptype;
        this.area = area;
        this.like_count = like_count;
        this.comm_count = comm_count;
        this.preview = preview;
        this.vid = vid;
        this.date = date;
    }

    @NonNull
    public int getId() {
        return id;
    }
    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getUserimage() { return image; }
    public void setUserimage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return officername;
    }
    public void setUsername(String officername) {
        this.officername = officername;
    }

    public String getPostcomment() {return postcomment;}
    public void setPostcomment(String postcomment) {
        this.postcomment = postcomment;
    }

    public String getPoststate() {
        return state;
    }
    public void setPoststate(String state) {
        this.state = state;
    }

    public String getPoststatus() {
        return status;
    }
    public void setPoststatus(String status) {
        this.status = status;
    }

    public String getPosttype() {
        return reptype;
    }
    public void setPosttype(String reptype) {
        this.reptype = reptype;
    }

    public String getPostarea() {
        return area;
    }
    public void setPostarea(String area) {
        this.area = area;
    }

    public String getLikscount() {
        return like_count;
    }
    public void setLikscount(String like_count) {
        this.like_count = like_count;
    }

    public String getCommentcount() {
        return comm_count;
    }
    public void setCommentcountt(String comm_count) {
        this.comm_count = comm_count;
    }

    public String getPreview() {return preview;}
    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getVidrscurl() {
        return vid;
    }
    public void setVidrscurl(String vid) {this.vid = vid;}

    public String getPostdate() {
        return date;
    }
    public void setPostdate(String date) {
        this.date = date;
    }
}
