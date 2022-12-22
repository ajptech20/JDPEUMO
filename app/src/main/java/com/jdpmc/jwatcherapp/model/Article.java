package com.jdpmc.jwatcherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article implements Parcelable
{

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("officername")
    @Expose
    private String officername;

    @SerializedName("postcomment")
    @Expose
    private String postcomment;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("reptype")
    @Expose
    private String reptype;

    @SerializedName("area")
    @Expose
    private String area;

    @SerializedName("like_count")
    @Expose
    private String like_count;

    @SerializedName("comm_count")
    @Expose
    private String comm_count;

    @SerializedName("preview")
    @Expose
    private String preview;

    @SerializedName("vid")
    @Expose
    private String vid;

    @SerializedName("date")
    @Expose
    private String date;

    public final static Parcelable.Creator<Article> CREATOR = new Creator<Article>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        public Article[] newArray(int size) {
            return (new Article[size]);
        }

    };

    protected Article(Parcel in) {
        this.id = (int) in.readValue((String.class.getClassLoader()));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.officername = ((String) in.readValue((String.class.getClassLoader())));
        this.postcomment = ((String) in.readValue((String.class.getClassLoader())));
        this.state = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.reptype = ((String) in.readValue((String.class.getClassLoader())));
        this.area = ((String) in.readValue((String.class.getClassLoader())));
        this.like_count = ((String) in.readValue((String.class.getClassLoader())));
        this.comm_count = ((String) in.readValue((String.class.getClassLoader())));
        this.preview = ((String) in.readValue((String.class.getClassLoader())));
        this.vid = ((String) in.readValue((String.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));

    }

    public Article() {
    }

    public int getId() { return id; }
    public void setId(int id) {
        this.id = id;
    }

    public String getUserimage() {
        return image;
    }
    public void setUserimage(String image) {
        this.image = image;
    }

    public String getUsername() {return officername;}
    public void setUsername(String officername) {
        this.officername = officername;
    }

    public String getPostcomment() {
        return postcomment;
    }
    public void setPostcomment(String postcomment) {
        this.postcomment = postcomment;
    }

    public String getPoststate() {
        return state;
    }
    public void setPoststate(String state) {
        this.state = state;
    }

    public String getPoststatus() {return status;}
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
    public void setCommentcount(String comm_count) {
        this.comm_count = comm_count;
    }

    public String getPreview() {
        return preview;
    }
    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getVidrscurl() {
        return vid;
    }
    public void setVidrscurl(String vid) {
        this.vid = vid;
    }

    public String getPostdate() {
        return date;
    }
    public void setPostdate(String postdate) {
        this.date = postdate;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(image);
        dest.writeValue(officername);
        dest.writeValue(postcomment);
        dest.writeValue(state);
        dest.writeValue(status);
        dest.writeValue(reptype);
        dest.writeValue(area);
        dest.writeValue(like_count);
        dest.writeValue(comm_count);
        dest.writeValue(preview);
        dest.writeValue(vid);
        dest.writeValue(date);
    }

    public int describeContents() {
        return 0;
    }

}
