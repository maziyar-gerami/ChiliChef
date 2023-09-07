package com.ict.chilichef.Model;

/**
 * Created by Elham on 8/6/2017.
 */

public class AddIngredientsModel {

    public int ID;
    public String Name;

    public AddIngredientsModel(String name,int ID) {
        this.ID = ID;
        this.Name = name;
    }

    public AddIngredientsModel() {

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
