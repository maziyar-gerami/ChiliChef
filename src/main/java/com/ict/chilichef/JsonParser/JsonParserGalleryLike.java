package com.ict.chilichef.JsonParser;


import com.ict.chilichef.Model.GalleryLikeModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Maziyar on 6/29/2017.
 */

public class JsonParserGalleryLike {


    private static final String KEY_LIK = "nLiked";
    private static final String KEY_LIKED = "isLiked";
    private static final String KEY_DATE	= "Date";



    GalleryLikeModel galleryLikeModel = new GalleryLikeModel();

    public GalleryLikeModel parseJson(String jsonstring) {

        try {

            JSONArray jsonarray = new JSONArray(jsonstring);
            JSONObject jsonobject = jsonarray.getJSONObject(0);

            galleryLikeModel.setnLikes(jsonobject.getInt(KEY_LIK));
            galleryLikeModel.setIsLiked(jsonobject.getInt(KEY_LIKED));
            galleryLikeModel.setDate(jsonobject.getString(KEY_DATE));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return galleryLikeModel;
    }


}