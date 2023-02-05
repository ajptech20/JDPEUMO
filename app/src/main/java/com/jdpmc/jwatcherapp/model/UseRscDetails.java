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

    @SerializedName("Title")
    @Expose
    private String sgtitle;

    @SerializedName("Description")
    @Expose
    private String sgdescription;

    @SerializedName("Author")
    @Expose
    private String sgauthor;

    @SerializedName("Vidurl")
    @Expose
    private String vidurl;

    @SerializedName("FileName")
    @Expose
    private String sgfile_name;

    @SerializedName("ImgPreview")
    @Expose
    private String sgpreview;

    @SerializedName("Date")
    @Expose
    private String date;

    @SerializedName("Image")
    @Expose
    private String image;

    @SerializedName("Type")
    @Expose
    private String type;

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
        this.sgtitle = ((String) in.readValue((String.class.getClassLoader())));
        this.sgdescription = ((String) in.readValue((String.class.getClassLoader())));
        this.sgauthor = ((String) in.readValue((String.class.getClassLoader())));
        this.vidurl = ((String) in.readValue((String.class.getClassLoader())));
        this.sgfile_name = ((String) in.readValue((String.class.getClassLoader())));
        this.sgpreview = ((String) in.readValue((String.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
    }

    public UseRscDetails() {
    }

    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }

    public String getSGtitle() {return sgtitle;}
    public void setSGtitle(String sgtitle) {
        this.sgtitle = sgtitle;
    }

    public String getSGsgdescription() {return sgdescription;}
    public void setSsgdescription(String sgdescription) {this.sgdescription = sgdescription;}

    public String getSGvidurl() {return vidurl;}
    public void setSGvidurl(String vidurl) {this.vidurl = vidurl;}

    public String getSGsgauthor() {return sgauthor;}
    public void setSgsgauthor(String sgauthor) {this.sgauthor = sgauthor;}

    public String getSgsgfile_name() {return sgfile_name;}
    public void setSgsgfile_name(String sgfile_name) {this.sgfile_name = sgfile_name;}

    public String getSgsgpreview() {return sgpreview;}
    public void setSgsgpreview(String sgpreview) {this.sgpreview = sgpreview;}

    public String getudate() {return date;}
    public void setudate(String date) {this.date = date;}

    public String getuImage() {
        return image;
    }
    public void setIumage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(response);
        dest.writeValue(sgtitle);
        dest.writeValue(sgdescription);
        dest.writeValue(vidurl);
        dest.writeValue(sgauthor);
        dest.writeValue(sgfile_name);
        dest.writeValue(sgpreview);
        dest.writeValue(date);
        dest.writeValue(image);
        dest.writeValue(type);

    }

    public int describeContents() {
        return 0;
    }

}