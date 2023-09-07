package com.ict.chilichef.Model;

/**
 * Created by NP on 9/5/2017.
 */

public class GetLikesModel {

    private int ID_Pic;
    private int Likes;
    private int Liked;

    public int getID_Pic() {
        return ID_Pic;
    }

    public void setID_Pic(int ID_Pic) {
        this.ID_Pic = ID_Pic;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        this.Likes = likes;
    }

    public int getLiked() {
        return Liked;
    }

    public void setLiked(int liked) {
        Liked = liked;
    }
}
