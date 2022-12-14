package com.jdpmc.jwatcherapp.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArticleResponse implements Parcelable
{

    @SerializedName("articles")
    @Expose
    private List<Article> articles = null;
    public final static Parcelable.Creator<ArticleResponse> CREATOR = new Creator<ArticleResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ArticleResponse createFromParcel(Parcel in) {
            return new ArticleResponse(in);
        }

        public ArticleResponse[] newArray(int size) {
            return (new ArticleResponse[size]);
        }

    };

    protected ArticleResponse(Parcel in) {
        in.readList(this.articles, (com.jdpmc.jwatcherapp.model.Article.class.getClassLoader()));
    }

    public ArticleResponse() {
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(articles);
    }

    public int describeContents() {
        return 0;
    }

}
