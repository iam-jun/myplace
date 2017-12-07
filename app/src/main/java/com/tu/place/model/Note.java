package com.tu.place.model;

import java.io.Serializable;

/**
 * Created by SEV_USER on 4/28/2017.
 */

public class Note implements Serializable{
    private String id;
    private String title;
    private String content;
    private ArrayList<String> arrImage;

    public Note(String id, String title, String content,ArrayList<String> arrImage) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.arrImage = arrImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ArrayList<String> getArrImage() {
        return arrImage;
    }

    public void setArrImage(ArrayList<String> arrImage) {
        this.arrImage = arrImage;
    }
}
