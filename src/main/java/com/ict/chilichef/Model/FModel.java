package com.ict.chilichef.Model;

/**
 * Created by roshan on 6/30/2017.
 */

public class FModel {

    private String name;
    private int id_food;

    public FModel(int id_food, String name) {
        this.id_food = id_food;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId_food() {
        return id_food;
    }

    public void setId_food(int id_food) {
        this.id_food = id_food;
    }
}
