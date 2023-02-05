package com.jdpmc.jwatcherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatingResponse implements Parcelable {

    @SerializedName("Response")
    @Expose
    private String response;

    @SerializedName("ratestr1")
    @Expose
    private String ratestr1;

    @SerializedName("ratestr2")
    @Expose
    private String ratestr2;

    @SerializedName("ratestr3")
    @Expose
    private String ratestr3;

    @SerializedName("ratestr4")
    @Expose
    private String ratestr4;

    @SerializedName("ratestr5")
    @Expose
    private String ratestr5;

    @SerializedName("successres")
    @Expose
    private String successres;

    @SerializedName("err")
    @Expose
    private String err;

    public final static Creator<RatingResponse> CREATOR = new Creator<RatingResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RatingResponse createFromParcel(Parcel in) {
            return new RatingResponse(in);
        }

        public RatingResponse[] newArray(int size) {
            return (new RatingResponse[size]);
        }

    }
            ;

    protected RatingResponse(Parcel in) {
        this.response = ((String) in.readValue((String.class.getClassLoader())));
        this.ratestr1 = ((String) in.readValue((String.class.getClassLoader())));
        this.ratestr2 = ((String) in.readValue((String.class.getClassLoader())));
        this.ratestr3 = ((String) in.readValue((String.class.getClassLoader())));
        this.ratestr4 = ((String) in.readValue((String.class.getClassLoader())));
        this.ratestr5 = ((String) in.readValue((String.class.getClassLoader())));
        this.successres = ((String) in.readValue((String.class.getClassLoader())));
        this.err = ((String) in.readValue((String.class.getClassLoader())));
    }

    public RatingResponse() {
    }

    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }

    public String getRatestr1() {return ratestr1;}
    public void setRatestr1(String ratestr1) {
        this.ratestr1 = ratestr1;
    }
    public String getRatestr2() {return ratestr2;}
    public void setRatestr2(String ratestr2) {
        this.ratestr2 = ratestr2;
    }
    public String getRatestr3() {return ratestr3;}
    public void setRatestr3(String ratestr3) {
        this.ratestr3 = ratestr3;
    }
    public String getRatestr4() {return ratestr4;}
    public void setRatestr4(String ratestr4) {
        this.ratestr4 = ratestr4;
    }
    public String getRatestr5() {return ratestr5;}
    public void setRatestr5(String ratestr5) {
        this.ratestr5 = ratestr5;
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
        dest.writeValue(ratestr1);
        dest.writeValue(ratestr2);
        dest.writeValue(ratestr3);
        dest.writeValue(ratestr4);
        dest.writeValue(ratestr5);
        dest.writeValue(err);
    }

    public int describeContents() {
        return 0;
    }

}
