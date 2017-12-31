package com.tu.place.model;

import java.io.Serializable;

/**
 * Created by DONG A on 12/24/2017.
 */

public class Rating implements Serializable {
    String userId, placeId;
    float rating;
    long time;

    public Rating() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
