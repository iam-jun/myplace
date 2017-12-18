package com.tu.place.model;

import java.io.Serializable;

/**
 * Created by NhaKhoaPaRis on 12/7/2017.
 */

public class User implements Serializable{
    public String name, phone, username, password, dob;
    public boolean gender;
    public int type;
    public String url;
    public double latitu,longitu;

    public User() {
    }

    public User(String name, String phone, String password, boolean gender, int type) {
        this.name = name;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.type = type;
    }

    public User(String name, String phone, String password, boolean gender, String dob, int type) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.dob = dob;
        this.type = type;
    }


    public User(String name, String phone, String password, String dob, boolean gender, int type, String url) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
        this.type = type;
        this.url = url;
    }
}
