package com.a2tocsolutions.nispsasapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article implements Parcelable
{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("articleid")
    @Expose
    private String articleid;
    @SerializedName("picname")
    @Expose
    private String picname;
    @SerializedName("post_title")
    @Expose
    private String postTitle;
    @SerializedName("post_notes")
    @Expose
    private String postNotes;
    @SerializedName("post_category")
    @Expose
    private Object postCategory;
    @SerializedName("page_id")
    @Expose
    private Object pageId;
    @SerializedName("views")
    @Expose
    private Object views;
    @SerializedName("articletype")
    @Expose
    private String articletype;
    @SerializedName("videourl")
    @Expose
    private String videourl;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("sortorder")
    @Expose
    private String sortorder;
    @SerializedName("author")
    @Expose
    private String author;
    public final static Parcelable.Creator<Article> CREATOR = new Creator<Article>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        public Article[] newArray(int size) {
            return (new Article[size]);
        }

    }
            ;

    protected Article(Parcel in) {
        this.id = (int) in.readValue((String.class.getClassLoader()));
        this.articleid = (String) in.readValue((String.class.getClassLoader()));
        this.picname = ((String) in.readValue((String.class.getClassLoader())));
        this.postTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.postNotes = ((String) in.readValue((String.class.getClassLoader())));
        this.postCategory = ((Object) in.readValue((Object.class.getClassLoader())));
        this.pageId = ((Object) in.readValue((Object.class.getClassLoader())));
        this.views = ((Object) in.readValue((Object.class.getClassLoader())));
        this.articletype = ((String) in.readValue((String.class.getClassLoader())));
        this.videourl = ((String) in.readValue((String.class.getClassLoader())));
        this.category = ((String) in.readValue((String.class.getClassLoader())));
        this.sortorder = ((String) in.readValue((String.class.getClassLoader())));
        this.author = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Article() {
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getArticleid() { return articleid;
    }

    public void setArticleid(String articleid) {
        this.articleid = articleid;
    }

    public String getPicname() {
        return picname;
    }

    public void setPicname(String picname) {
        this.picname = picname;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostNotes() {
        return postNotes;
    }

    public void setPostNotes(String postNotes) {
        this.postNotes = postNotes;
    }

    public Object getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(Object postCategory) {
        this.postCategory = postCategory;
    }

    public Object getPageId() {
        return pageId;
    }

    public void setPageId(Object pageId) {
        this.pageId = pageId;
    }

    public Object getViews() {
        return views;
    }

    public void setViews(Object views) {
        this.views = views;
    }

    public String getArticletype() {
        return articletype;
    }

    public void setArticletype(String articletype) {
        this.articletype = articletype;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSortorder() {
        return sortorder;
    }

    public void setSortorder(String sortorder) {
        this.sortorder = sortorder;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(articleid);
        dest.writeValue(picname);
        dest.writeValue(postTitle);
        dest.writeValue(postNotes);
        dest.writeValue(postCategory);
        dest.writeValue(pageId);
        dest.writeValue(views);
        dest.writeValue(articletype);
        dest.writeValue(videourl);
        dest.writeValue(category);
        dest.writeValue(sortorder);
        dest.writeValue(author);
    }

    public int describeContents() {
        return 0;
    }

}
