package com.tu.place.model;

/**
 * Created by SEV_USER on 4/26/2017.
 */

public class Place {
    private String title;
    private String content;
    private String img;
    private String address;
    private double longitu;
    private double latitu;
    private float  distance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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

    private String phone;
    private int score;

    public Place(int id, String title, String content, String img, String address,String info, String phone, int score, double latitu, double longitu) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.img = img;
        this.address = address;
        this.longitu = longitu;
        this.latitu = latitu;
        this.info = info;
        this.phone = phone;
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitu() {
        return longitu;
    }

    public void setLongitu(double longitu) {
        this.longitu = longitu;
    }

    public double getLatitu() {
        return latitu;
    }

    public void setLatitu(double latitu) {
        this.latitu = latitu;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
