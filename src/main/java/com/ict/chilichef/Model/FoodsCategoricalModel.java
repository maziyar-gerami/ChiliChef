package com.ict.chilichef.Model;

import static com.ict.chilichef.MainActivity.URL_FoodsImages200;
import static com.ict.chilichef.MainActivity.URL_ProfileImages;

/**
 * Created by Maziyar on 6/3/2017.
 */

public class FoodsCategoricalModel {

    private int id_Food;
    private String Title;
    private int Likes;
    private int isFree;
    private String Food_Pic_Url;
    private int Writer;
    private String Writer_Pic_Url;
    private String WriterName;

    public FoodsCategoricalModel(){
        WriterName="";
    }

    public String getFood_Pic_Url() {
        return Food_Pic_Url;
    }

    public void setFood_Pic_Url(String food_Pic_Url) {
        Food_Pic_Url = URL_FoodsImages200+food_Pic_Url;
    }

    public String getWriter_Pic_url() {
        return Writer_Pic_Url;
    }

    public void setWriter_Pic_url(String writer_Pic_url) {
        Writer_Pic_Url = URL_ProfileImages+writer_Pic_url;
    }

    public String getWriterName() {
        return WriterName;
    }

    public void setWriterName(String writerName) {
        WriterName = writerName;
    }


    public int getWriter() {
        return Writer;
    }

    public void setWriter(int writer) {
        Writer = writer;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getId_Food() {
        return id_Food;
    }

    public void setId_Food(int id_Food) {
        this.id_Food = id_Food;
    }

}
