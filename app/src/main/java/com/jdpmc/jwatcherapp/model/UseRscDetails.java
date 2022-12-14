package com.jdpmc.jwatcherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UseRscDetails implements Parcelable
{

    @SerializedName("Response")
    @Expose
    private String response;

    @SerializedName("Name")
    @Expose
    private String sgname;

    @SerializedName("State")
    @Expose
    private String sgstate;

    @SerializedName("VideoUrl")
    @Expose
    private String vidsrc;

    @SerializedName("Phone")
    @Expose
    private String sgphone;

    @SerializedName("Comment")
    @Expose
    private String sgcomment;

    @SerializedName("Vidstatus")
    @Expose
    private String sgstatuse;

    @SerializedName("Type")
    @Expose
    private String sgreptype;

    @SerializedName("Lga")
    @Expose
    private String lga;

    @SerializedName("Date")
    @Expose
    private String date;

    @SerializedName("Image")
    @Expose
    private String image;

    //////////////////////
    public final static Creator<UseRscDetails> CREATOR = new Creator<UseRscDetails>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UseRscDetails createFromParcel(Parcel in) {
            return new UseRscDetails(in);
        }

        public UseRscDetails[] newArray(int size) {
            return (new UseRscDetails[size]);
        }

    }
            ;

    protected UseRscDetails(Parcel in) {
        this.response = ((String) in.readValue((String.class.getClassLoader())));
        this.sgname = ((String) in.readValue((String.class.getClassLoader())));
        this.sgstate = ((String) in.readValue((String.class.getClassLoader())));
        this.vidsrc = ((String) in.readValue((String.class.getClassLoader())));
        this.sgphone = ((String) in.readValue((String.class.getClassLoader())));
        this.sgcomment = ((String) in.readValue((String.class.getClassLoader())));
        this.sgstatuse = ((String) in.readValue((String.class.getClassLoader())));
        this.sgreptype = ((String) in.readValue((String.class.getClassLoader())));
        this.lga = ((String) in.readValue((String.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
    }

    public UseRscDetails() {
    }

    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }

    public String getSGname() {return sgname;}
    public void setSGname(String sgname) {
        this.sgname = sgname;
    }

    public String getSGphone() {return sgphone;}
    public void setSGphone(String sgphone) {this.sgphone = sgphone;}

    public String getSGdate() {return date;}
    public void setSGdate(String date) {this.date = date;}

    public String getSGcomment() {return sgcomment;}
    public void setSgcomment(String sgcomment) {this.sgcomment = sgcomment;}

    public String getSgstatuse() {return sgstatuse;}
    public void setSgstatuse(String sgstatuse) {this.sgstatuse = sgstatuse;}

    public String getSgreptype() {return sgreptype;}
    public void setSgreptype(String sgreptype) {this.sgreptype = sgreptype;}

    public String getLga() {return lga;}
    public void setLga(String lga) {this.lga = lga;}

    public String getSGstate() {return sgstate;}
    public void setSGcompany(String sgstate) {this.sgstate = sgstate;}

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
        dest.writeValue(sgstate);
        dest.writeValue(vidsrc);
        dest.writeValue(sgreptype);
        dest.writeValue(lga);
        dest.writeValue(sgcomment);
        dest.writeValue(sgstatuse);
        dest.writeValue(image);

    }

    public int describeContents() {
        return 0;
    }

}