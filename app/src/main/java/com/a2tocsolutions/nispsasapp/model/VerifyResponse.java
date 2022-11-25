package com.a2tocsolutions.nispsasapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyResponse implements Parcelable
{

    @SerializedName("response")
    @Expose
    private String response;
    public final static Parcelable.Creator<VerifyResponse> CREATOR = new Creator<VerifyResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public VerifyResponse createFromParcel(Parcel in) {
            return new VerifyResponse(in);
        }

        public VerifyResponse[] newArray(int size) {
            return (new VerifyResponse[size]);
        }

    }
            ;

    protected VerifyResponse(Parcel in) {
        this.response = ((String) in.readValue((String.class.getClassLoader())));
    }

    public VerifyResponse() {
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(response);
    }

    public int describeContents() {
        return 0;
    }

}
