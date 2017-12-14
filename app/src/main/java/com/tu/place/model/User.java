package com.tu.place.model;

import java.io.Serializable;

/**
 * Created by NhaKhoaPaRis on 12/7/2017.
 */

public class User implements Serializable{
    public String name, phone, username, password;
    public boolean gender;
    public int type;

    public User() {
    }

    public User(String name, String phone, String username, String password, boolean gender, int type) {
        this.name = name;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.type = type;
    }

    public User(String name, String phone, String password, boolean gender, int type) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.type = type;
    }
}
