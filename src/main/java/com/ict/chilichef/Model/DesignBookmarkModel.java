package com.ict.chilichef.Model;

import java.util.List;

/**
 * Created by Elham on 8/26/2017.
 */

public class DesignBookmarkModel {

    private int ID_Design;
    private int Saved;
    private String Title;
    private List<String> Pic_Url;

    public int getID_Design() {
        return ID_Design;
    }

    public void setID_Design(int ID_Design) {
        this.ID_Design = ID_Design;
    }

    public int getSaved() {
        return Saved;
    }

    public void setSaved(int saved) {
        Saved = saved;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public List<String> getPic_Url() {
        return Pic_Url;
    }

    public void setPic_Url(List<String> pic_Url) {
        Pic_Url = pic_Url;
    }
}
