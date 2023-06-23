package com.medi.dailynews.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

//@Entity(tableName = "saved_articles")
//public class Article {
//
//    @PrimaryKey(autoGenerate = true)
//    private int id;
//
//    @SerializedName("author")
//    private String author;
//    private String content;
//    @SerializedName("description")
//    private String description;
//
//    @SerializedName("publishedAt")
//    private String publishedAt;
//
//    @SerializedName("source")
//    private Source source;
//    @SerializedName("title")
//    private String title;
//
//    @SerializedName("url")
//    private String url;
//
//    @SerializedName("urlToImage")
//    private String urlToImage;
//
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(String author) {
//        this.author = author;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getPublishedAt() {
//        return publishedAt;
//    }
//
//    public void setPublishedAt(String publishedAt) {
//        this.publishedAt = publishedAt;
//    }
//
//    public Source getSource() {
//        return source;
//    }
//
//    public void setSource(Source source) {
//        this.source = source;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public String getUrlToImage() {
//        return urlToImage;
//    }
//
//    public void setUrlToImage(String urlToImage) {
//        this.urlToImage = urlToImage;
//    }
//
//    @Override
//    public String toString() {
//        return "Article{" +
//                "author='" + author + '\'' +
//                ", content='" + content + '\'' +
//                ", description='" + description + '\'' +
//                ", publishedAt='" + publishedAt + '\'' +
//                ", source=" + source +
//                ", title='" + title + '\'' +
//                ", url='" + url + '\'' +
//                ", urlToImage='" + urlToImage + '\'' +
//                '}';
//    }
//}
@Entity(tableName = "saved_articles")
public class Article  implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("source")
    private Source source;

    @SerializedName("author")
    private String author;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("url")
    private String url;

    @SerializedName("urlToImage")
    private String urlToImage;

    @SerializedName("publishedAt")
    private String publishedAt;

    @SerializedName("content")
    private String content;

    protected Article(Parcel in) {
        id = in.readInt();
        author = in.readString();
        title = in.readString();
        description = in.readString();
        url = in.readString();
        urlToImage = in.readString();
        publishedAt = in.readString();
        content = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public Article() {
        // required empty constructor
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public Source getSource() {
        return source;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                "source=" + source +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(urlToImage);
        dest.writeString(publishedAt);
        dest.writeString(content);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this)
            return true;

        Article newsItem = (Article) obj;
        return newsItem.id == this.id;
    }
}
