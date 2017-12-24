package com.tu.place.model;

import java.io.Serializable;

/**
 * Created by DONG A on 12/24/2017.
 */

public class Comment implements Serializable {
    String content, userId, userName, placeId, urlAvatar;
    long time;
    String[] arrImg;

    public Comment() {
    }

    public Comment(String content, String userId, String placeId, long time, String[] arrImg) {
        this.content = content;
        this.userId = userId;
        this.placeId = placeId;
        this.time = time;
        this.arrImg = arrImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String[] getArrImg() {
        return arrImg;
    }

    public void setArrImg(String[] arrImg) {
        this.arrImg = arrImg;
    }
}
