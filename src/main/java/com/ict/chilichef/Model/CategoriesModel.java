package com.ict.chilichef.Model;


/**
 * Created by Maziyar on 4/16/2017.
 */

public class CategoriesModel {

    private int Id_Cat;
    private String Name;
    private  int parent;
    private String pic_url;
    private String Description;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isLeaf() {
        return IsLeaf;
    }

    public void setLeaf(boolean leaf) {
        IsLeaf = leaf;
    }




    private boolean IsLeaf;

    public static final String ImageCatFolder = "http://chilichef.000webhostapp.com/android_connect/Image/Cat/";

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = ImageCatFolder+pic_url;
    }



    public int getId_Cat() {
        return Id_Cat;
    }

    public void setId_Cat(int id_Cat) {
        Id_Cat = id_Cat;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String toString(){
        return Name;
    }


}
