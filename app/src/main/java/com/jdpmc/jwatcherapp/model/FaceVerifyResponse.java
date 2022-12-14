package com.jdpmc.jwatcherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaceVerifyResponse implements Parcelable
{
    @SerializedName("Response")
    @Expose
    private String fresponse;
    @SerializedName("Name")
    @Expose
    private String fname;
    @SerializedName("Phone")
    @Expose
    private String fphone;
    @SerializedName("Age")
    @Expose
    private String fage;
    @SerializedName("Gender")
    @Expose
    private String fgender;
    @SerializedName("State")
    @Expose
    private String fstate;
    @SerializedName("LGA")
    @Expose
    private String flga;
    @SerializedName("Address")
    @Expose
    private String faddress;
    @SerializedName("Trade")
    @Expose
    private String ftrade;
    @SerializedName("Passport")
    @Expose
    private String fpassport;
    @SerializedName("Empcomment")
    @Expose
    private String fempcomment;
    @SerializedName("Empname")
    @Expose
    private String fempname;
    @SerializedName("Empdate")
    @Expose
    private String fempdate;
    @SerializedName("Emprate")
    @Expose
    private String femprate;

    public final static Creator<FaceVerifyResponse> CREATOR = new Creator<FaceVerifyResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public FaceVerifyResponse createFromParcel(Parcel in) {
            return new FaceVerifyResponse(in);
        }

        public FaceVerifyResponse[] newArray(int size) {
            return (new FaceVerifyResponse[size]);
        }

    }
            ;

    protected FaceVerifyResponse(Parcel in) {
        this.fresponse = ((String) in.readValue((String.class.getClassLoader())));
        this.fname = ((String) in.readValue((String.class.getClassLoader())));
        this.fphone = ((String) in.readValue((String.class.getClassLoader())));
        this.fage = ((String) in.readValue((String.class.getClassLoader())));
        this.fgender = ((String) in.readValue((String.class.getClassLoader())));
        this.fstate = ((String) in.readValue((String.class.getClassLoader())));
        this.flga = ((String) in.readValue((String.class.getClassLoader())));
        this.faddress = ((String) in.readValue((String.class.getClassLoader())));
        this.ftrade = ((String) in.readValue((String.class.getClassLoader())));
        this.fpassport = ((String) in.readValue((String.class.getClassLoader())));
        this.fempcomment = ((String) in.readValue((String.class.getClassLoader())));
        this.fempname = ((String) in.readValue((String.class.getClassLoader())));
        this.fempdate = ((String) in.readValue((String.class.getClassLoader())));
        this.femprate = ((String) in.readValue((String.class.getClassLoader())));
    }

    public FaceVerifyResponse() {
    }

    public String getFResponse() {
        return fresponse;
    }

    public void setFResponse(String fresponse) {
        this.fresponse = fresponse;
    }

    public String getFName() {
        return fname;
    }

    public void setFName(String fname) {
        this.fname = fname;
    }

    public String getFPhone() {
        return fphone;
    }

    public void setFPhone(String fphone) {
        this.fphone = fphone;
    }

    public String getFAge() {
        return fage;
    }

    public void setFAge(String fage) {
        this.fage = fage;
    }

    public String getFGender() {
        return fgender;
    }

    public void setFGender(String fgender) {
        this.fgender = fgender;
    }

    public String getFState() {
        return fstate;
    }

    public void setFState(String fstate) {
        this.fstate = fstate;
    }

    public String getFLGA() {
        return flga;
    }

    public void setFLga(String flga) {
        this.flga = flga;
    }

    public String getFAddress() {
        return faddress;
    }

    public void setFAddress(String faddress) {
        this.faddress = faddress;
    }

    public String getFTrade() {
        return ftrade;
    }

    public void setFTrade(String ftrade) {
        this.ftrade = ftrade;
    }

    public String getFPassport() {
        return fpassport;
    }

    public void setFPassport(String fpassport) {
        this.fpassport = fpassport;
    }
    public String getFempcomment() {
        return fempcomment;
    }

    public void setFempcomment(String fempcomment) {
        this.fempcomment = fempcomment;
    }
    public String getFempname() {
        return fempname;
    }

    public void setFempname(String fempname) {
        this.fempname = fempname;
    }
    public String getFempdate() {
        return fempdate;
    }

    public void setFempdate(String fempdate) {
        this.fempdate = fempdate;
    }
    public String getFemprate() {
        return femprate;
    }

    public void setFemprate(String femprate) {
        this.femprate = femprate;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(fresponse);
        dest.writeValue(fname);
        dest.writeValue(fphone);
        dest.writeValue(fage);
        dest.writeValue(fgender);
        dest.writeValue(fstate);
        dest.writeValue(flga);
        dest.writeValue(faddress);
        dest.writeValue(ftrade);
        dest.writeValue(fpassport);
        dest.writeValue(fempcomment);
        dest.writeValue(fempname);
        dest.writeValue(fempdate);
        dest.writeValue(fempdate);
    }

    public int describeContents() {
        return 0;
    }

}