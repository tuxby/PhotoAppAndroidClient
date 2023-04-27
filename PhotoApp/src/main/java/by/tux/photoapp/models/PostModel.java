package by.tux.photoapp.models;

import java.util.Date;

public class PostModel {
    private long id;
    private long authorId;
    private String authorLogin;
    private String authorName;
    private String disc;
    private long likes;
    private long  publishTime;
    private String imageUrl;

    public PostModel(long id, long authorId, String authorLogin, String authorName, String disc, long likes, long publishTime, String imageUrl) {
        this.id = id;
        this.authorId = authorId;
        this.authorLogin = authorLogin;
        this.authorName = authorName;
        this.disc = disc;
        this.likes = likes;
        this.publishTime = publishTime;
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorLogin() {
        return authorLogin;
    }

    public void setAuthorLogin(String authorLogin) {
        this.authorLogin = authorLogin;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
