package com.ict.chilichef.JsonParser;


import com.ict.chilichef.Model.SearchUsersModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Maziyar on 6/19/2017.
 */

public class JsonParserSearchUser {

    private static final String KEY_ID 		= "id_User";
    private static final String KEY_NAME 	= "Name";
    private static final String KEY_LEVEL	= "level";
    private static final String KEY_PICU	= "PicUrl";
    private static final String KEY_ISF	= "isFollowing";
    private static final String KEY_NFI= "numberOfFollowings";
    private static final String KEY_NFE	= "numberOfFollowers";
    private static final String KEY_NPI     = "numberOfPictures";
    private static final String KEY_NRE     = "numberOfRecipes";
    private static final String KEY_SCO     = "score";



    public ArrayList<SearchUsersModel> parseJson(String jsonstring){
        ArrayList<SearchUsersModel> searchUsersModels = new ArrayList<SearchUsersModel>();

        try {

            JSONArray jsonarray = new JSONArray(jsonstring);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                SearchUsersModel searchUsersModel = new SearchUsersModel();
                searchUsersModel.setId_User(jsonobject.getInt(KEY_ID));
                searchUsersModel.setName(jsonobject.getString(KEY_NAME));
                searchUsersModel.setLevel(jsonobject.getInt(KEY_LEVEL));
                searchUsersModel.setPic_Url(jsonobject.getString(KEY_PICU));
                searchUsersModel.setIsFollowing(jsonobject.getInt(KEY_ISF));
                searchUsersModel.setNumberOfFollowings(jsonobject.getInt(KEY_NFI));
                searchUsersModel.setNumberOfFollowers(jsonobject.getInt(KEY_NFE));
                searchUsersModel.setNumberOfPictures(jsonobject.getInt(KEY_NPI));
                searchUsersModel.setNumberOfRecipes(jsonobject.getInt(KEY_NRE));
                searchUsersModel.setScore(jsonobject.getInt(KEY_SCO));


                searchUsersModels.add(searchUsersModel);
            }
            return searchUsersModels;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return searchUsersModels;
    }



}
