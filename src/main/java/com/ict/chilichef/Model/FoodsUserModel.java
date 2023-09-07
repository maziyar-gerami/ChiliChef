package com.ict.chilichef.Model;

import java.util.ArrayList;

import static com.ict.chilichef.MainActivity.URL_FoodsImages200;

/**
 * Created by Maziyar on 6/3/2017.
 */

public class FoodsUserModel extends ArrayList<FoodsUserModel> {

    private int id_Food;
    private String Title;
    private int Likes;
    private String Food_Pic_Url;


    public String getFood_Pic_Url() {
        return Food_Pic_Url;
    }

    public void setFood_Pic_Url(String food_Pic_Url) {
        Food_Pic_Url = URL_FoodsImages200+food_Pic_Url;
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
