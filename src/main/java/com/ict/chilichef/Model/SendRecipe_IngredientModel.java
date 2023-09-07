package com.ict.chilichef.Model;

/**
 * Created by Elham on 8/6/2017.
 */

public class SendRecipe_IngredientModel {

    private int id_ing;
    private String amount;
    private String Ing_Name;
    private String Unit_Name;

    public SendRecipe_IngredientModel(String amount, String unit_Name, String ing_Name) {
        this.amount = amount;
        Ing_Name = ing_Name;
        Unit_Name = unit_Name;
    }

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

        return Ing_Name + " " + amount + " " + Unit_Name + "\n";
    }
}
