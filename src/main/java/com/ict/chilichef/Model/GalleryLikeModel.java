package com.ict.chilichef.Model;

import java.util.Date;

import static com.ict.chilichef.MainActivity.URL_ProfileImages;


/**
 * Created by Maziyar on 6/17/2017.
 */

public class GalleryLikeModel {

    private  int isLiked;
    private String Date;
    private int nLikes;



    public int getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(int isLiked) {
        this.isLiked = isLiked;
    }

    public int getnLikes() {
        return nLikes;
    }

    public void setnLikes(int nLikes) {
        this.nLikes = nLikes;
    }

    public String Date() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDate() {
        return Date;
    }


}
