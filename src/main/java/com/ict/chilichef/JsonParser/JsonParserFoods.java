package com.ict.chilichef.JsonParser;

import com.ict.chilichef.Model.FoodsCategoricalModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Maziyar on 6/3/2017.
 */

public class JsonParserFoods {

    private static final String KEY_ID 		= "id_Food";
    private static final String KEY_TITLE 	= "Title";
    private static final String KEY_LIKES	= "Likes";
    private static final String KEY_FPN	    = "Food_Pic_Name";
    private static final String KEY_ISFREE	= "isFree";
    private static final String KEY_WRITER	= "Writer";
    private static final String KEY_WNAM    = "WriterName";
    private static final String KEY_WPN	    = "Writer_Pic_Name";



    public ArrayList<FoodsCategoricalModel> parseJson(String jsonstring){
        ArrayList<FoodsCategoricalModel> foodModels = new ArrayList<FoodsCategoricalModel>();

        try {

            JSONArray jsonarray = new JSONArray(jsonstring);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                FoodsCategoricalModel foodModel = new FoodsCategoricalModel();
                foodModel.setId_Food(jsonobject.getInt(KEY_ID));
                foodModel.setTitle(jsonobject.getString(KEY_TITLE));
                foodModel.setLikes(jsonobject.getInt(KEY_LIKES));
                foodModel.setIsFree(jsonobject.getInt(KEY_ISFREE));
                foodModel.setWriter(jsonobject.getInt(KEY_WRITER));
                foodModel.setWriterName(jsonobject.getString(KEY_WNAM));
                foodModel.setFood_Pic_Url(jsonobject.getString(KEY_FPN));
                foodModel.setWriter_Pic_url(jsonobject.getString(KEY_WPN));

                foodModels.add(foodModel);
            }
            return foodModels;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return foodModels;
    }

}
