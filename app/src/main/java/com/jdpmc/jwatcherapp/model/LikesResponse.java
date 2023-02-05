package com.jdpmc.jwatcherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikesResponse implements Parcelable {

    @SerializedName("Response")
    @Expose
    private String response;

    @SerializedName("liked")
    @Expose
    private String liked;

    @SerializedName("like_count")
    @Expose
    private String like_count;

    @SerializedName("unliked")
    @Expose
    private String unliked;

    @SerializedName("err")
    @Expose
    private String err;

    public final static Creator<LikesResponse> CREATOR = new Creator<LikesResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LikesResponse createFromParcel(Parcel in) {
            return new LikesResponse(in);
        }

        public LikesResponse[] newArray(int size) {
            return (new LikesResponse[size]);
        }

    }
            ;

    protected LikesResponse(Parcel in) {
        this.response = ((String) in.readValue((String.class.getClassLoader())));
        this.liked = ((String) in.readValue((String.class.getClassLoader())));
        this.like_count = ((String) in.readValue((String.class.getClassLoader())));
        this.unliked = ((String) in.readValue((String.class.getClassLoader())));
        this.err = ((String) in.readValue((String.class.getClassLoader())));
    }

    public LikesResponse() {
    }

    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }

    public String getLiked() {return liked;}
    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getLikeCount() {return like_count;}
    public void setLikeCount(String like_count) {
        this.like_count = like_count;
    }

    public String getUnliked() {return unliked;}
    public void setUnliked(String unliked) {
        this.unliked = unliked;
    }

    public String getLikeErr() {
        return err;
    }
    public void setLikeErr(String err) {
        this.err = err;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(liked);
        dest.writeValue(unliked);
        dest.writeValue(err);
    }

    public int describeContents() {
        return 0;
    }

}
