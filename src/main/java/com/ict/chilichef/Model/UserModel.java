package com.ict.chilichef.Model;


import static com.ict.chilichef.MainActivity.URL_ProfileImages;

/**
 * Created by Maziyar on 5/27/2017.
 */

public class UserModel {


    private boolean success;
    private int id_user;
    private String Username;
    private String Name;
    private String Pass;
    private int Score;
    private int Level;
    private int Paid;
    private String Pic_Url;
    private String Description;
    private int Telephone;
    private int lenght;
    private int weight;
    private String fat;
    private String sugar;
    private String Prompted;
    private int Status;
    private int nFollowing;
    private int nFollower;


    public int getnFollowing() {
        return nFollowing;
    }

    public void setnFollowing(int nFollowing) {
        this.nFollowing = nFollowing;
    }

    public int getnFollower() {
        return nFollower;
    }

    public void setnFollower(int nFollower) {
        this.nFollower = nFollower;
    }

    public String getPrompted() {
        return Prompted;
    }

    public void setPrompted(String prompted) {
        Prompted = prompted;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public UserModel() {
        Level = 0;
        Score = 0;
        Pass = null;
        Username = null;
        Name = null;
        id_user = 0;
        Paid = 0;
        Pic_Url = null;
        Description = null;
        Telephone = 0;
        lenght = 0;
        weight = 0;
        fat = null;
        sugar = null;
        Prompted = null;
        Status = 0;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPaid() {
        return Paid;
    }

    public void setPaid(int paid) {
        Paid = paid;
    }

    public String getPic_Url() {
        return Pic_Url;
    }

    public void setPic_Url(String pic_Name) {
        Pic_Url = URL_ProfileImages + pic_Name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String descripion) {
        Description = descripion;
    }

    public void setTelephone(int telephone) {
        Telephone = telephone;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }


    public boolean isSuccess() {
        return success;
    }


    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public int getLevel() {
        return Level;
    }

    public int getTelephone() {
        return Telephone;
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public int getWeight() {
        return weight;
    }

    public void setLevel(int level) {
        Level = level;
    }
}
