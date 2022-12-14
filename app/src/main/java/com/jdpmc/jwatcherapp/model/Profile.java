package com.jdpmc.jwatcherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile implements Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("phonenumber")
    @Expose
    private String phonenumber;
    @SerializedName("house")
    @Expose
    private String house;
    @SerializedName("office")
    @Expose
    private String office;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("statecode")
    @Expose
    private String statecode;
    @SerializedName("username")
    @Expose
    private String username;
    public final static Parcelable.Creator<Profile> CREATOR = new Creator<Profile>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        public Profile[] newArray(int size) {
            return (new Profile[size]);
        }

    }
            ;

    protected Profile(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.fullname = ((String) in.readValue((String.class.getClassLoader())));
        this.phonenumber = ((String) in.readValue((String.class.getClassLoader())));
        this.house = ((String) in.readValue((String.class.getClassLoader())));
        this.office = ((String) in.readValue((String.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
        this.statecode = ((String) in.readValue((String.class.getClassLoader())));
        this.username = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Profile() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(fullname);
        dest.writeValue(phonenumber);
        dest.writeValue(house);
        dest.writeValue(office);
        dest.writeValue(date);
        dest.writeValue(statecode);
        dest.writeValue(username);
    }

    public int describeContents() {
        return 0;
    }

}
