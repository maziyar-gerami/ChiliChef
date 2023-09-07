package com.ict.chilichef.Model;

import static com.ict.chilichef.MainActivity.URL_ProfileImages;

/**
 * Created by Maziyar on 6/15/2017.
 */

public class FriendsModel {

    private int ID_Friend;
    private String Name_Friend;
    private String PicUrl_Friend;
    private int Score;
    private String text;
    private int Level;
    private int color;
    private int NumberOfFollowers;
    private int NumberOfFollowings;
    private int NumberOfPictures;
    private int NumberOfRecipes;

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }




    private int isFollowing;

    public FriendsModel(){

        isFollowing =1;

    }

    public int getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(int isFollowing) {
        this.isFollowing = isFollowing;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public int getID_Friend() {
        return ID_Friend;
    }

    public void setID_Friend(int ID_Friend) {
        this.ID_Friend = ID_Friend;
    }

    public String getName_Friend() {
        return Name_Friend;
    }

    public void setName_Friend(String name_Friend) {
        Name_Friend = name_Friend;
    }

    public String getPicUrl_Friend() {
        return PicUrl_Friend;
    }

    public void setPicUrl_Friend(String picUrl_Friend) {
        PicUrl_Friend = URL_ProfileImages + picUrl_Friend;
    }
}
