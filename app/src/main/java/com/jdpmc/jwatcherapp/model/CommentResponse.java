package com.jdpmc.jwatcherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentResponse implements Parcelable {

    @SerializedName("Response")
    @Expose
    private String response;

    @SerializedName("comm_count")
    @Expose
    private String comm_count;

    @SerializedName("successres")
    @Expose
    private String successres;

    @SerializedName("err")
    @Expose
    private String err;

    public final static Creator<CommentResponse> CREATOR = new Creator<CommentResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CommentResponse createFromParcel(Parcel in) {
            return new CommentResponse(in);
        }

        public CommentResponse[] newArray(int size) {
            return (new CommentResponse[size]);
        }

    }
            ;

    protected CommentResponse(Parcel in) {
        this.response = ((String) in.readValue((String.class.getClassLoader())));
        this.comm_count = ((String) in.readValue((String.class.getClassLoader())));
        this.successres = ((String) in.readValue((String.class.getClassLoader())));
        this.err = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CommentResponse() {
    }

    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }

    public String getCommCount() {return comm_count;}
    public void setCommCount(String comm_count) {
        this.comm_count = comm_count;
    }

    public String getSuccessres() {return successres;}
    public void setSuccessres(String successres) {this.successres = successres;}

    public String getCommErr() {
        return err;
    }
    public void setCommErr(String err) {
        this.err = err;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(response);
        dest.writeValue(comm_count);
        dest.writeValue(err);
    }

    public int describeContents() {
        return 0;
    }

}
