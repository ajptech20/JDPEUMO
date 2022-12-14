package com.jdpmc.jwatcherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadServerResponse implements Parcelable
{

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("comments")
    @Expose
    private Object comments;
    @SerializedName("imagename")
    @Expose
    private String imagename;
    @SerializedName("uid")
    @Expose
    private String uid;
    public final static Parcelable.Creator<UploadServerResponse> CREATOR = new Creator<UploadServerResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UploadServerResponse createFromParcel(Parcel in) {
            return new UploadServerResponse(in);
        }

        public UploadServerResponse[] newArray(int size) {
            return (new UploadServerResponse[size]);
        }

    }
            ;

    protected UploadServerResponse(Parcel in) {
        this.error = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.comments = ((Object) in.readValue((Object.class.getClassLoader())));
        this.imagename = ((String) in.readValue((String.class.getClassLoader())));
        this.uid = ((String) in.readValue((String.class.getClassLoader())));
    }

    public UploadServerResponse() {
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getComments() {
        return comments;
    }

    public void setComments(Object comments) {
        this.comments = comments;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(error);
        dest.writeValue(message);
        dest.writeValue(comments);
        dest.writeValue(imagename);
        dest.writeValue(uid);
    }

    public int describeContents() {
        return 0;
    }

}
