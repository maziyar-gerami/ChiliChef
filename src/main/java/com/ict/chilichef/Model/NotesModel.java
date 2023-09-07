package com.ict.chilichef.Model;

import static com.ict.chilichef.MainActivity.URL_NotesPics;

/**
 * Created by Elham on 8/24/2017.
 */
public class NotesModel {

    private int ID;
    private String Title;
    private String Description;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

}
