package com.ict.chilichef.JsonParser;

import android.graphics.Color;

import com.ict.chilichef.Model.FriendsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Maziyar on 6/15/2017.
 */

public class JsonParserFriends {

    private static final String KEY_ID 		= "id_Friend";
    private static final String KEY_NAME 	= "Name";
    private static final String KEY_PICN	= "Pic_Name";
    private static final String KEY_ISF	    = "isFollowing";
    private static final String KEY_NFR     = "numberOfFollowers";
    private static final String KEY_NFI     = "numberOfFollowings";
    private static final String KEY_NPI     = "numberOfPictures";
    private static final String KEY_NRE     = "numberOfRecipes";
    private static final String KEY_SCO     = "score";
    private static final String KEY_LEV     = "level";


    public ArrayList<FriendsModel> parseJson(String jsonstring){
        ArrayList<FriendsModel> FriendsModels = new ArrayList<FriendsModel>();

        try {

            JSONArray jsonarray = new JSONArray(jsonstring);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                FriendsModel friendsModel = new FriendsModel();
                friendsModel.setID_Friend(jsonobject.getInt(KEY_ID));
                friendsModel.setName_Friend(jsonobject.getString(KEY_NAME));
                friendsModel.setPicUrl_Friend(jsonobject.getString(KEY_PICN));
                friendsModel.setScore(jsonobject.getInt(KEY_SCO));
                friendsModel.setIsFollowing(jsonobject.getInt(KEY_ISF));
                friendsModel.setLevel(jsonobject.getInt(KEY_LEV));
                friendsModel.setNumberOfFollowings(jsonobject.getInt(KEY_NFI));
                friendsModel.setNumberOfFollowers(jsonobject.getInt(KEY_NFR));

                friendsModel.setNumberOfPictures(jsonobject.getInt(KEY_NPI));
                friendsModel.setNumberOfRecipes(jsonobject.getInt(KEY_NRE));
                friendsModel.setScore(jsonobject.getInt(KEY_SCO));

                if (friendsModel.getIsFollowing()==0){

                    friendsModel.setColor(Color.GRAY);
                    friendsModel.setText("دنبال کن");
                }
                else{

                    friendsModel.setColor(Color.GREEN);
                    friendsModel.setText("دنبال می کنم");
                }

                FriendsModels.add(friendsModel);
            }
            return FriendsModels;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return FriendsModels;
    }
}
