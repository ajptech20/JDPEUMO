package com.jdpmc.jwatcherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChildResponse implements Parcelable
{

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("Passport")
    @Expose
    private String image;

    public final static Creator<ChildResponse> CREATOR = new Creator<ChildResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ChildResponse createFromParcel(Parcel in) {
            return new ChildResponse(in);
        }

        public ChildResponse[] newArray(int size) {
            return (new ChildResponse[size]);
        }

    }
            ;

    protected ChildResponse(Parcel in) {
        this.response = ((String) in.readValue((String.class.getClassLoader())));

        this.image = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ChildResponse() {
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response){
        this.response = response;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(response);
        dest.writeValue(image);
    }

    public int describeContents() {
        return 0;
    }

}
