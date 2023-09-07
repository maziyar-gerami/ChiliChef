package com.ict.chilichef.Model;

import java.util.List;

import static com.ict.chilichef.MainActivity.URL_DesignPics;

/**
 * Created by Elham on 8/26/2017.
 */

public class DesignModel {

    private String Title;
    private String Description;
    private int Saved;
    private List<String> Pic_Url;

    public int getSaved() {
        return Saved;
    }

    public void setSaved(int saved) {
        Saved = saved;
    }

    public List<String> getPic_Url() {
        return Pic_Url;
    }

    public void setPic_Url(List<String> pic_Url) {
        Pic_Url = pic_Url;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }


}
