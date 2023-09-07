package com.ict.chilichef.Model;


import static com.ict.chilichef.MainActivity.URL_AdvertisePics;

/**
 * Created by NP on 9/3/2017.
 */

public class advertiseModel {

    private int ID;
    private String Name;
    private String Pic_Url;
    private String Target_Url;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPic_Url() {
        return Pic_Url;
    }

    public void setPic_Url(String pic_Url) {
        Pic_Url =URL_AdvertisePics+ pic_Url;
    }

    public String getTarget_Url() {
        return Target_Url;
    }

    public void setTarget_Url(String target_Url) {
        Target_Url = target_Url;
    }
}
