package com.ict.chilichef.Model;


import static com.ict.chilichef.MainActivity.URL_FoodsImages200;
import static com.ict.chilichef.MainActivity.URL_ProfileImages;

/**
 * Created by Maziyar on 6/19/2017.
 */

public class SearchRecipesModel {

    private int id_Food;
    private String Title;
    private int Likes;
    private String Pic_Url;
    private int WriterID;
    private  String WriterName;


    public String getWriterPicUrl() {
        return WriterPicUrl;
    }

    public void setWriterPicUrl(String writerPicUrl) {
        WriterPicUrl = URL_ProfileImages+writerPicUrl;
    }

    private String WriterPicUrl;

    public int getId_Food() {
        return id_Food;
    }

    public void setId_Food(int id_Food) {
        this.id_Food = id_Food;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
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

    public String getPic_Url() {
        return Pic_Url;
    }

    public void setPic_Url(String pic_Url) {
        Pic_Url = URL_FoodsImages200+pic_Url;
    }
}
