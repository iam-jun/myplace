package com.tu.place.model;

import java.io.Serializable;

/**
 * Created by SEV_USER on 4/26/2017.
 */

public class Place implements Serializable{
    private String id, address, content, img, titleInfo, info;
    private double latitu;
    private double longitu;
    private String phone;
    private double score;
    private String title;
    private String userId;
    private float distance;

    public Place() {
    }

    public Place(String address, String content, String img, String info, double latitu, double longitu, String phone, double score, String title) {
        this.address = address;
        this.content = content;
        this.img = img;
        this.info = info;
        this.latitu = latitu;
        this.longitu = longitu;
        this.phone = phone;
        this.score = score;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitleInfo() {
        return titleInfo;
    }

    public void setTitleInfo(String titleInfo) {
        this.titleInfo = titleInfo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public double getLatitu() {
        return latitu;
    }

    public void setLatitu(double latitu) {
        this.latitu = latitu;
    }

    public double getLongitu() {
        return longitu;
    }

    public void setLongitu(double longitu) {
        this.longitu = longitu;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Place{" +
                "address='" + address + '\'' +
                ", content='" + content + '\'' +
                ", img='" + img + '\'' +
                ", info='" + info + '\'' +
                ", latitu=" + latitu +
                ", longitu=" + longitu +
                ", phone='" + phone + '\'' +
                ", score=" + score +
                ", title='" + title + '\'' +
                '}';
    }
}
