package com.ict.chilichef.JsonParser;


import com.ict.chilichef.Model.FoodsUserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Maziyar on 6/29/2017.
 */

public class JsonParserUserRecipe {

    private static final String KEY_TIT = "Title";
    private static final String KEY_ID = "id_Food";
    private static final String KEY_LIK = "Likes";
    private static final String KEY_PICN = "Pic_URL";



    public ArrayList<FoodsUserModel> parseJson(String jsonstring){
        ArrayList<FoodsUserModel> recipeModels = new ArrayList<FoodsUserModel>();

        try {

            JSONArray jsonarray = new JSONArray(jsonstring);

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                FoodsUserModel recipeModel = new FoodsUserModel();

                recipeModel.setId_Food(jsonobject.getInt(KEY_ID));
                recipeModel.setTitle(jsonobject.getString(KEY_TIT));
                recipeModel.setLikes(jsonobject.getInt(KEY_LIK));
                recipeModel.setFood_Pic_Url(jsonobject.getString(KEY_PICN));

                recipeModels.add(recipeModel);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipeModels;
    }


}