package com.a2tocsolutions.nispsasapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtinguisherResponse implements Parcelable
{

    @SerializedName("response")
    @Expose
    private String response;
    public final static Parcelable.Creator<ExtinguisherResponse> CREATOR = new Creator<ExtinguisherResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ExtinguisherResponse createFromParcel(Parcel in) {
            return new ExtinguisherResponse(in);
        }

        public ExtinguisherResponse[] newArray(int size) {
            return (new ExtinguisherResponse[size]);
        }

    }
            ;

    protected ExtinguisherResponse(Parcel in) {
        this.response = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ExtinguisherResponse() {
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
