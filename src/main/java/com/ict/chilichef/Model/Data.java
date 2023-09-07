package com.ict.chilichef.Model;

import static com.ict.chilichef.MainActivity.URL_DesignPics;

/**
 * Created by roshan on 2/27/2017.
 */

public class Data {

    private String title;
    private int imageId;
    private int id_cat;
    private String PicUrl;
    private String Description;

    public Data(int imageId, String title, int id_cat) {
        this.title = title;
        this.imageId = imageId;
        this.id_cat = id_cat;
        this.Description = "";
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = URL_DesignPics + picUrl;
    }

    public Data(String title, int imageId, int id_cat, String Description) {
        this.title = title;
        this.imageId = imageId;
        this.id_cat = id_cat;
        this.Description = Description;
    }

    public Data(String title, int imageId) {
        this.title = title;
        this.imageId = imageId;
    }

    public Data() {
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getId_cat() {
        return id_cat;
    }

    public String getDescription() {
        return Description;
    }

    public void setId_cat(int id_cat) {
        this.id_cat = id_cat;
    }

    public String getTitle() {
        return title;
    }

    public int getImageId() {
        return imageId;
    }
}

