package com.ict.chilichef.JsonParser;


import com.ict.chilichef.Model.SearchIngredientsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Maziyar on 8/15/2017.
 */

public class JsonParserSearchIngredients {

    private static final String KEY_ID 		= "ID";
    private static final String KEY_Name	= "Name";
    private static final String KEY_STATUS	= "Status";

    public ArrayList<SearchIngredientsModel> parseJson(String jsonstring){
        ArrayList<SearchIngredientsModel> SearchIngredientsModels = new ArrayList<SearchIngredientsModel>();

        try {

            JSONArray jsonarray = new JSONArray(jsonstring);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                SearchIngredientsModel searchIngredientsModel = new SearchIngredientsModel();
                searchIngredientsModel.setID(jsonobject.getInt(KEY_ID));
                searchIngredientsModel.setName(jsonobject.getString(KEY_Name));
                searchIngredientsModel.setStatus(jsonobject.getInt(KEY_STATUS));


                SearchIngredientsModels.add(searchIngredientsModel);
            }
            return SearchIngredientsModels;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return SearchIngredientsModels;
    }


}
