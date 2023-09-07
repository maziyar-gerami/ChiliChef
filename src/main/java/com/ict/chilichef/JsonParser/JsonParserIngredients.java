package com.ict.chilichef.JsonParser;

import com.ict.chilichef.Model.IngredientsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Maziyar on 6/29/2017.
 */

public class JsonParserIngredients {

    private static final String KEY_IDI = "id_ing";
    private static final String KEY_AMU = "Number";
    private static final String KEY_INGN = "Ing_Name";
    private static final String KEY_UNIN = "Unit_Name";

    public static ArrayList<IngredientsModel> parseJson(String jsonstring){
        ArrayList<IngredientsModel> ingredientsModels = new ArrayList<IngredientsModel>();

        try {

            JSONArray jsonarray = new JSONArray(jsonstring);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                IngredientsModel ingredientsModel = new IngredientsModel();
                ingredientsModel.setId_ing(jsonobject.getInt(KEY_IDI));
                ingredientsModel.setAmount(jsonobject.getString(KEY_AMU));
                ingredientsModel.setIng_Name(jsonobject.getString(KEY_INGN));
                ingredientsModel.setUnit_Name(jsonobject.getString(KEY_UNIN));

                ingredientsModels.add(ingredientsModel);
            }
            return ingredientsModels;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ingredientsModels;
    }
}
