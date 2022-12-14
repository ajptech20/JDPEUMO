package com.jdpmc.jwatcherapp.database;

public class HomePosts {
    //Data Variables
    private String image;
    private String name;
    private String id;
    private String postcomment;
    private String callid;
    private String state;
    private String status;
    private String reptype;
    private String area;
    private String like_count;
    private String comm_count;
    private String preview;
    private String vid;
    private String date;
    private String postimage;

    //Getters and Setters
    public String getImageUrl() {return image;}
    public void setImageUrl(String image) {this.image = image;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getComment() {return postcomment;}
    public void setComment(String postcomment) {this.postcomment = postcomment;}

    public String getRepId() {return callid;}
    public void setRepId(String callid) {this.callid = callid;}

    public String getState() {return state;}
    public void setState(String state) {this.state = state;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public String getRepType() {return reptype;}
    public void setRepType(String reptype) {this.reptype = reptype;}

    public String getArea() {return area;}
    public void setArea(String area) {this.area = area;}

    public String getLikes() {return like_count;}
    public void setLikes(String like_count) {this.like_count = like_count;}

    public String getComments() {return comm_count;}
    public void setComments(String comm_count) {this.comm_count = comm_count;}

    public String getPreview() {return preview;}
    public void setPreview(String preview) {this.preview = preview;}

    public String getResUri() {return vid;}
    public void setResUri(String vid) {this.vid = vid;}

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}

    public String getPostImg() {return postimage;}
    public void setPostImg(String postimage) {this.postimage = postimage;}
}
