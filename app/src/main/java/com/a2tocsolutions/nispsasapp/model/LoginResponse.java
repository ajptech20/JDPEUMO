package com.a2tocsolutions.nispsasapp.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse implements Parcelable
{

    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("profile")
    @Expose
    private List<Profile> profile = null;
    public final static Parcelable.Creator<LoginResponse> CREATOR = new Creator<LoginResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LoginResponse createFromParcel(Parcel in) {
            return new LoginResponse(in);
        }

        public LoginResponse[] newArray(int size) {
            return (new LoginResponse[size]);
        }

    }
            ;

    protected LoginResponse(Parcel in) {
        this.response = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.profile, (com.a2tocsolutions.nispsasapp.model.Profile.class.getClassLoader()));
    }

    public LoginResponse() {
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<Profile> getProfile() {
        return profile;
    }

    public void setProfile(List<Profile> profile) {
        this.profile = profile;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(response);
        dest.writeList(profile);
    }

    public int describeContents() {
        return 0;
    }

}
