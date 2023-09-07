package com.ict.chilichef.Model;


import static com.ict.chilichef.MainActivity.URL_DesignPics;

/**
 * Created by Elham on 8/26/2017.
 */

public class DesignCategoricalModel {

    private int Id_Design;
    private String Title;
    private String Pic_Url;

    public int getId_Design() {
        return Id_Design;
    }

    public void setId_Design(int id_Design) {
        Id_Design = id_Design;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPic_Url() {
        return Pic_Url;
    }

    public void setPic_Url(String pic_Url) {
        Pic_Url = URL_DesignPics+pic_Url;
    }
}
