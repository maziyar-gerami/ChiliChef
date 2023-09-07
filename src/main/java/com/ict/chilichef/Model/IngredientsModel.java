package com.ict.chilichef.Model;

/**
 * Created by Maziyar on 6/29/2017.
 */

public class IngredientsModel {

    private int id_ing;
    private String amount;
    private String Ing_Name;
    private String Unit_Name;

    public int getId_ing() {
        return id_ing;
    }

    public void setId_ing(int id_ing) {
        this.id_ing = id_ing;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIng_Name() {
        return Ing_Name;
    }

    public void setIng_Name(String ing_Name) {
        Ing_Name = ing_Name;
    }

    public String getUnit_Name() {
        return Unit_Name;
    }

    public void setUnit_Name(String unit_Name) {
        Unit_Name = unit_Name;
    }

    @Override
    public String toString() {

        return Ing_Name + " " + amount + " " + Unit_Name +"\n";
    }
}
