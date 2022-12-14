package com.jdpmc.jwatcherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyDetails implements Parcelable
{

    @SerializedName("Response")
    @Expose
    private String response;
    @SerializedName("Name")
    @Expose
    private String sgname;
    @SerializedName("Company")
    @Expose
    private String sgcompany;
    @SerializedName("Phone")
    @Expose
    private String vidsrc;
    @SerializedName("VideoUrl")
    @Expose
    private String sgphone;
    @SerializedName("Image")
    @Expose
    private String image;
    public final static Parcelable.Creator<VerifyDetails> CREATOR = new Creator<VerifyDetails>() {


        @SuppressWarnings({
                "unchecked"
        })
        public VerifyDetails createFromParcel(Parcel in) {
            return new VerifyDetails(in);
        }

        public VerifyDetails[] newArray(int size) {
            return (new VerifyDetails[size]);
        }

    }
            ;

    protected VerifyDetails(Parcel in) {
        this.response = ((String) in.readValue((String.class.getClassLoader())));
        this.sgname = ((String) in.readValue((String.class.getClassLoader())));
        this.sgcompany = ((String) in.readValue((String.class.getClassLoader())));
        this.vidsrc = ((String) in.readValue((String.class.getClassLoader())));
        this.sgphone = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
    }

    public VerifyDetails() {
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
    public String getSGname() {
        return sgname;
    }

    public void setSGname(String sgname) {
        this.sgname = sgname;
    }

    public String getSGphone() {
        return sgphone;
    }

    public void setSGphone(String sgphone) {
        this.sgphone = sgphone;
    }
    public String getSGcompany() {
        return sgcompany;
    }

    public void setSGcompany(String sgcompany) {
        this.sgcompany= sgcompany;
    }

    public String getSGvideourl() {
        return vidsrc;
    }

    public void getSGvideourl(String vidsrc) {
        this.vidsrc= vidsrc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(response);
        dest.writeValue(sgname);
        dest.writeValue(sgcompany);
        dest.writeValue(vidsrc);
        dest.writeValue(sgphone);
        dest.writeValue(image);

    }

    public int describeContents() {
        return 0;
    }

}