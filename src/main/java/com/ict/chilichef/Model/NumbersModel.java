package com.ict.chilichef.Model;

/**
 * Created by Maziyar on 7/7/2017.
 */

public class NumbersModel {

    private int numberOfFollowings;
    private int numberOfFollowers;
    private int NumberOfPictures;
    private int NumberOfRecipes;
    private int Score;
    private int Level;

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

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

    public int getNumberOfFollowings() {
        return numberOfFollowings;
    }

    public void setNumberOfFollowings(int numberOfFollowings) {
        this.numberOfFollowings = numberOfFollowings;
    }

    public int getNumberOfFollowers() {
        return numberOfFollowers;
    }

    public void setNumberOfFollowers(int numberOfFollowers) {
        this.numberOfFollowers = numberOfFollowers;
    }

}
