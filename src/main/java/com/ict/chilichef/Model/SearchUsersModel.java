package com.ict.chilichef.Model;


import static com.ict.chilichef.MainActivity.URL_ProfileImages;

/**
 * Created by Maziyar on 6/19/2017.
 */

public class SearchUsersModel {

    private int id_User;
    private String Name;
    private int Level;
    private String Pic_Url;
    private int NumberOfFollowers;
    private int NumberOfFollowings;
    private int NumberOfPictures;
    private int NumberOfRecipes;
    private int Score;
    private int isFollowing;

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public int getNumberOfPictures() {
        return NumberOfPictures;
    }

    public void setNumberOfPictures(int numberOfPictures) {
        NumberOfPictures = numberOfPictures;
    }

    public int getNumberOfRecipes() {
        return NumberOfRecipes;
    }

    public void setNumberOfRecipes(int numberOfRecipes) {
        NumberOfRecipes = numberOfRecipes;
    }

    public int getNumberOfFollowers() {
        return NumberOfFollowers;
    }

    public void setNumberOfFollowers(int numberOfFollowers) {
        NumberOfFollowers = numberOfFollowers;
    }

    public int getNumberOfFollowings() {
        return NumberOfFollowings;
    }

    public void setNumberOfFollowings(int numberOfFollowings) {
        NumberOfFollowings = numberOfFollowings;
    }

    public int getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(int isFollowing) {
        this.isFollowing = isFollowing;
    }

    public int getId_User() {
        return id_User;
    }

    public void setId_User(int id_User) {
        this.id_User = id_User;
    }

    public String getPic_Url() {
        return Pic_Url;
    }

    public void setPic_Url(String pic_Url) {
        Pic_Url = URL_ProfileImages+pic_Url;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }




}
