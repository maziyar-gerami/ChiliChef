package com.ict.chilichef.Model;

import java.util.Date;

import static com.ict.chilichef.MainActivity.URL_ProfileImages;

/**
 * Created by Maziyar on 6/17/2017.
 */

public class RecipeCommentsModel {

    private  int ID_Comment;
    private String Comment;
    private Date Date;
    private int ID_Writer;
    private  String Name;
    private String Pic_Url_User;
    private int Is_Following;
//    private int nComments;

//    public int getnComments() {
//        return nComments;
//    }
//
//    public void setnComments(int nComments) {
//        this.nComments = nComments;
//    }

    public int getIs_Following() {
        return Is_Following;
    }

    public void setIs_Following(int is_Following) {
        Is_Following = is_Following;
    }

    public int getID_Comment() {
        return ID_Comment;
    }

    public void setID_Comment(int ID_Comment) {
        this.ID_Comment = ID_Comment;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getID_Writer() {
        return ID_Writer;
    }

    public void setID_Writer(int ID_Writer) {
        this.ID_Writer = ID_Writer;
    }

    public String getPic_Url_User() {
        return Pic_Url_User;
    }

    public void setPic_Url_User(String pic_Url_User) {
        Pic_Url_User = URL_ProfileImages+pic_Url_User;
    }
}
