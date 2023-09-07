package com.ict.chilichef.Model;

import static com.ict.chilichef.MainActivity.URL_Gallery;
import static com.ict.chilichef.MainActivity.URL_ProfileImages;

/**
 * Created by Maziyar on 6/19/2017.
 */

public class SearchPicturesModel {

    private int ID_Pic;
    private String FoodName;
    private String Description;
    private String picUrl;
    private int WriterID;
    private String WriterName;
    private String PicUrlWriter;
    private int Is_Following;
    private int likes;
    private int Liked;
    private int nComments;

    public int getnComments() {
        return nComments;
    }

    public void setnComments(int nComments) {
        this.nComments = nComments;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getLiked() {
        return Liked;
    }

    public void setLiked(int liked) {
        Liked = liked;
    }

    public int getIs_Following() {
        return Is_Following;
    }

    public void setIs_Following(int is_Following) {
        Is_Following = is_Following;
    }

    public int getWriterID() {
        return WriterID;
    }

    public void setWriterID(int writerID) {
        WriterID = writerID;
    }

    public String getWriterName() {
        return WriterName;
    }

    public void setWriterName(String writerName) {
        WriterName = writerName;
    }

    public String getPicUrlWriter() {
        return PicUrlWriter;
    }

    public void setPicUrlWriter(String picUrlWriter) {
        PicUrlWriter = URL_ProfileImages+picUrlWriter;
    }

    public int getID_Pic() {
        return ID_Pic;
    }

    public void setID_Pic(int ID_Pic) {
        this.ID_Pic = ID_Pic;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = URL_Gallery+picUrl;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }
}
