package com.example.gargt.picturesscramble.model;

/**
 * Created by gargt on 12/7/16.
 */

public class Photo  {

    private String farm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String isprimary;
    private String secret;
    private String server;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
}
