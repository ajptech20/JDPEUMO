package com.jdpmc.jwatcherapp.database;

public class UseFulResource {
    //Data Variables
    private String title;
    private String description;
    private String id;
    private String repuuid;
    private String like_count;
    private String comm_count;
    private String vidurl;
    private String fileurl;
    private String previewimg;
    private String imgfileurl;
    private String authour;
    private String date;
    private String type;

    //Getters and Setters
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getDescrib() {return description;}
    public void setDescrib(String description) {this.description = description;}

    public String getVidurl() {return vidurl;}
    public void setVidUrl(String vidurl) {this.vidurl = vidurl;}

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getRepuuid() {return repuuid;}
    public void setRepuuid(String repuuid) {this.repuuid = repuuid;}

    public String getLikes() {return like_count;}
    public void setLikes(String like_count) {this.like_count = like_count;}

    public String getComments() {return comm_count;}
    public void setComments(String comm_count) {this.comm_count = comm_count;}

    public String getFileUrl() {return fileurl;}
    public void setFileUrl(String fileurl) {this.fileurl = fileurl;}

    public String getPrevieImg() {return previewimg;}
    public void setPrevieImg(String previewimg) {this.previewimg = previewimg;}

    public String getImgFileurl() {return imgfileurl;}
    public void setImgFileurl(String imgfileurl) {this.imgfileurl = imgfileurl;}

    public String getAuthor() {return authour;}
    public void setAuthour(String authour) {this.authour = authour;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}
}
