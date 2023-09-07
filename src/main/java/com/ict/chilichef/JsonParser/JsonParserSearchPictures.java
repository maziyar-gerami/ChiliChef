package com.ict.chilichef.JsonParser;


import com.ict.chilichef.Model.SearchPicturesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Maziyar on 6/19/2017.
 */

public class JsonParserSearchPictures {

    private static final String KEY_ID 		= "ID_Pic";
    private static final String KEY_DES 	= "Description";
    private static final String KEY_PICU	= "Pic_Name";
    private static final String KEY_WID	    = "WriterID";
    private static final String KEY_WNAM	= "WriterName";
    private static final String KEY_WPIC	= "WriterPicURL";
    private static final String KEY_ISFOLLOWING = "isFollowing";
    private static final String KEY_LIKES   =  "Likes";
    private static final String KEY_LIKED   = "Liked";
    private static final String KEY_FNAME	= "FoodName";
    private static final String KEY_NCOMMENT	= "nComments";




    public ArrayList<SearchPicturesModel> parseJson(String jsonstring){
        ArrayList<SearchPicturesModel> searchPicturesModels = new ArrayList<SearchPicturesModel>();

        try {

            JSONArray jsonarray = new JSONArray(jsonstring);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                SearchPicturesModel searchPicturesModel = new SearchPicturesModel();
                searchPicturesModel.setID_Pic(jsonobject.getInt(KEY_ID));
                searchPicturesModel.setDescription(jsonobject.getString(KEY_DES));
                searchPicturesModel.setPicUrl(jsonobject.getString(KEY_PICU));
                searchPicturesModel.setWriterName(jsonobject.getString(KEY_WNAM));
                searchPicturesModel.setWriterID(jsonobject.getInt(KEY_WID));
                searchPicturesModel.setPicUrlWriter(jsonobject.getString(KEY_WPIC));
                searchPicturesModel.setIs_Following(jsonobject.getInt(KEY_ISFOLLOWING));
                searchPicturesModel.setLiked(jsonobject.getInt(KEY_LIKED));
                searchPicturesModel.setLikes(jsonobject.getInt(KEY_LIKES));
                searchPicturesModel.setFoodName(jsonobject.getString(KEY_FNAME));
                searchPicturesModel.setnComments(jsonobject.getInt(KEY_NCOMMENT));


                searchPicturesModels.add(searchPicturesModel);
            }
            return searchPicturesModels;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return searchPicturesModels;
    }

}
