package com.jdpmc.jwatcherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseTransporter implements Parcelable
{

    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("name")
    @Expose
    private String dname;
    @SerializedName("phone")
    @Expose
    private String dphone;
    @SerializedName("union")
    @Expose
    private String dunion;
    @SerializedName("image")
    @Expose
    private String image;

    public final static Parcelable.Creator<ResponseTransporter> CREATOR = new Creator<ResponseTransporter>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ResponseTransporter createFromParcel(Parcel in) {
            return new ResponseTransporter(in);
        }

        public ResponseTransporter[] newArray(int size) {
            return (new ResponseTransporter[size]);
        }

    }
            ;

    protected ResponseTransporter(Parcel in) {
        this.response = ((String) in.readValue((String.class.getClassLoader())));
        this.dname = ((String) in.readValue((String.class.getClassLoader())));
        this.dphone = ((String) in.readValue((String.class.getClassLoader())));
        this.dunion = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ResponseTransporter() {
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response){
        this.response = response;
    }
    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }
    public String getDphone() {
        return dphone;
    }

    public void setDphone(String dphone) {
        this.dphone = dphone;
    }
    public String getDunion() {
        return dunion;
    }

    public void setDunion(String dunion) {
        this.dunion = dunion;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(response);
        dest.writeValue(dname);
        dest.writeValue(dphone);
        dest.writeValue(dunion);
        dest.writeValue(image);
    }

    public int describeContents() {
        return 0;
    }

}
