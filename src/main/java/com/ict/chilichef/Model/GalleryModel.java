package com.ict.chilichef.Model;

import java.util.Date;

import static com.ict.chilichef.MainActivity.URL_Gallery;
import static com.ict.chilichef.MainActivity.URL_ProfileImages;

/**
 * Created by Maziyar on 6/17/2017.
 */

public class GalleryModel implements Comparable {
    private int ID_Pic;
    private int ID_User;
    private int likes;
    private int Liked;
    private String Pic_Url;
    private String Description;
    private String FoodName;
    private String Name;
    private String Pic_url_user;
    private int Is_Following;
    private int nComments;
    private java.util.Date Date;
    private int Saved;


    public int getSaved() {
        return Saved;
    }

    public void setSaved(int saved) {
        Saved = saved;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public int getnComments() {
        return nComments;
    }

    public void setnComments(int nComments) {
        this.nComments = nComments;
    }

    public int getIs_Following() {
        return Is_Following;
    }

    public void setIs_Following(int is_Following) {
        Is_Following = is_Following;
    }

    public String getPic_url_user() {
        return Pic_url_user;
    }

    public void setPic_url_user(String pic_url_user) {
        Pic_url_user = URL_ProfileImages + pic_url_user;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getPic_Url() {
        return Pic_Url;
    }

    public void setPic_Url(String pic_Url) {
        Pic_Url = URL_Gallery + pic_Url;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getLiked() {
        return Liked;
    }

    public void setLiked(int liked) {
        Liked = liked;
    }

    public int getID_User() {
        return ID_User;
    }

    public void setID_User(int ID_User) {
        this.ID_User = ID_User;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getID_Pic() {
        return ID_Pic;
    }

    public void setID_Pic(int ID_Pic) {
        this.ID_Pic = ID_Pic;
    }

    @Override
    public int compareTo(Object o) {

        if (getLikes() > likes) {

            return 1;
        }

        return 0;
    }


}
