package com.tu.place.model;

import java.io.Serializable;

/**
 * Created by SEV_USER on 4/26/2017.
 */

public class Place implements Serializable{
    private String address, content, img, info;
    private double latitu;
    private double longitu;
    private String phone;
    private int score;
    private String title;

    public Place() {
    }

    public Place(String address, String content, String img, String info, double latitu, double longitu, String phone, int score, String title) {
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
