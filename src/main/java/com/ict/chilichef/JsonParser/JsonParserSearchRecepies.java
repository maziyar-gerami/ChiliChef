package com.ict.chilichef.JsonParser;


import com.ict.chilichef.Model.SearchRecipesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Maziyar on 6/19/2017.
 */

public class JsonParserSearchRecepies {

    private static final String KEY_ID 		= "id_Food";
    private static final String KEY_TIT 	= "Title";
    private static final String KEY_LIKES	= "Likes";
    private static final String KEY_PICU	= "Pic_URL";
    private static final String KEY_WID	    = "Writer";
    private static final String KEY_WNAME   = "WriterName";
    private static final String KEY_WPIC	= "WriterPicURL";



    public ArrayList<SearchRecipesModel> parseJson(String jsonstring){
        ArrayList<SearchRecipesModel> searchRecipesModels = new ArrayList<SearchRecipesModel>();

        try {

            JSONArray jsonarray = new JSONArray(jsonstring);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                SearchRecipesModel searchRecipesModel = new SearchRecipesModel();
                searchRecipesModel.setId_Food(jsonobject.getInt(KEY_ID));
                searchRecipesModel.setTitle(jsonobject.getString(KEY_TIT));
                searchRecipesModel.setLikes(jsonobject.getInt(KEY_LIKES));
                searchRecipesModel.setPic_Url(jsonobject.getString(KEY_PICU));
                searchRecipesModel.setWriterID(jsonobject.getInt(KEY_WID));
                searchRecipesModel.setWriterName(jsonobject.getString(KEY_WNAME));
                searchRecipesModel.setWriterPicUrl(jsonobject.getString(KEY_WPIC));


                searchRecipesModels.add(searchRecipesModel);
            }
            return searchRecipesModels;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return searchRecipesModels;
    }

}
