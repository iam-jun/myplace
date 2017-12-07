package com.tu.place.model;

/**
 * Created by SEV_USER on 4/25/2017.
 */

public class Friend {
    private String avatar;
    private String name;

    public Friend(String avatar, String name) {
        this.avatar = avatar;
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
