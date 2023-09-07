package com.ict.chilichef.Model;


import static com.ict.chilichef.MainActivity.URL_FoodsImages500;
import static com.ict.chilichef.MainActivity.URL_ProfileImages;

/**
 * Created by Maziyar on 6/29/2017.
 */

public class RecipeModel {

    private String Id_Food;
    private String Title;
    private String Recepies;
    private int Likes;
    private int Score;
    private int Time;
    private int Meal;
    private String Pic_URL;
    private int Writer;
    private String SubmittedTime;
    private int Liked;
    private String WriterName;
    private String Writer_Pic_URL;
    private String Cat_l;
    private String Cat_p;
    private int Is_Following;
    private int Saved;
    private int Hard;
    private int Views;
    private int nComments;

    public int getnComments() {
        return nComments;
    }

    public void setnComments(int nComments) {
        this.nComments = nComments;
    }
    public int getViews() {
        return Views;
    }

    public void setViews(int views) {
        Views = views;
    }

    public int getHard() {
        return Hard;
    }

    public void setHard(int hard) {
        Hard = hard;
    }

    public int getSaved() {
        return Saved;
    }

    public void setSaved(int saved) {
        Saved = saved;
    }


    public int getIs_Following() {
        return Is_Following;
    }

    public void setIs_Following(int is_Following) {
        Is_Following = is_Following;
    }


    public String getWriterName() {
        return WriterName;
    }

    public void setWriterName(String writerName) {
        WriterName = writerName;
    }

    public String getWriter_Pic_URL() {
        return Writer_Pic_URL;
    }

    public void setWriter_Pic_URL(String writer_Pic_URL) {
        Writer_Pic_URL = URL_ProfileImages + writer_Pic_URL;
    }

    public int getLiked() {
        return Liked;
    }

    public void setLiked(int liked) {
        Liked = liked;
    }

    public String getId_Food() {
        return Id_Food;
    }

    public void setId_Food(String id_Food) {
        Id_Food = id_Food;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getRecepies() {
        return Recepies;
    }

    public void setRecepies(String recepies) {
        Recepies = recepies;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public int getTime() {
        return Time;
    }

    public void setTime(int time) {
        Time = time;
    }

    public int getMeal() {
        return Meal;
    }

    public void setMeal(int meal) {
        Meal = meal;
    }

    public String getPic_URL() {
        return Pic_URL;
    }

    public void setPic_URL(String pic_URL) {
        Pic_URL = URL_FoodsImages500 + pic_URL;
    }

    public int getWriter() {
        return Writer;
    }

    public void setWriter(int writer) {
        Writer = writer;
    }

    public String getSubmittedTime() {
        return SubmittedTime;
    }

    public void setSubmittedTime(String submittedTime) {
        SubmittedTime = submittedTime;
    }

    public String getCat_l() {
        return Cat_l;
    }

    public void setCat_l(String cat_l) {
        Cat_l = cat_l;
    }

    public String getCat_p() {
        return Cat_p;
    }

    public void setCat_p(String cat_p) {
        Cat_p = cat_p;
    }
}
