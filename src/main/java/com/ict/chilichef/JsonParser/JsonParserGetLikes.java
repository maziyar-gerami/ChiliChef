package com.ict.chilichef.JsonParser;

import com.ict.chilichef.Model.GetLikesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by NP on 9/5/2017.
 */

public class JsonParserGetLikes {

    private static final String KEY_ID = "ID_Pic";
    private static final String KEY_LIKES = "Likes";
    private static final String KEY_LIKED = "Liked";


    GetLikesModel getLikesModel = new GetLikesModel();

    public GetLikesModel parseJson(String jsonstring) {
        ArrayList<GetLikesModel> getLikesModels = new ArrayList<GetLikesModel>();

        try {

            JSONArray jsonarray = new JSONArray(jsonstring);
            JSONObject jsonobject = jsonarray.getJSONObject(0);

            getLikesModel.setID_Pic(jsonobject.getInt(KEY_ID));
            getLikesModel.setLikes(jsonobject.getInt(KEY_LIKES));
            getLikesModel.setLiked(jsonobject.getInt(KEY_LIKED));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getLikesModel;
    }


}
