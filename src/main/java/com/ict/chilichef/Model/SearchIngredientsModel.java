package com.ict.chilichef.Model;

/**
 * Created by Maziyar on 8/15/2017.
 */

public class SearchIngredientsModel {

    private int success;
    private int ID;
    private int Status;
    private String Name;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setStatus(int status) {
        this.Status = status;
    }

    public int getStatus() {
        return Status;
    }



    public SearchIngredientsModel(int ID, String name) {
        this.ID = ID;
        this.Name = name;
    }

    public SearchIngredientsModel() {

    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

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
        this.Name = name;
    }
}
