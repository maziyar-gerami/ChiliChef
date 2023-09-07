package com.ict.chilichef.JsonParser;


import com.ict.chilichef.Model.GalleryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Maziyar on 6/17/2017.
 */

public class JsonParserGallery {


    private static final String KEY_IDP 	= "ID_Pic";
    private static final String KEY_IDU 	= "ID_User";
    private static final String KEY_LIKES	= "Likes";
    private static final String KEY_PICN	= "Pic_Name";
    private static final String KEY_LIKED	= "Liked";
    private static final String KEY_DESC	= "Description";
    private static final String KEY_FNAME	= "FoodName";
    private static final String KEY_UNAME	= "Name";
    private static final String KEY_PICNU = "Pic_Name_User";
    private static final String KEY_ISFOLLOWING = "isFollowing";
    private static final String KEY_NCOMMENTS = "nComments";
    private static final String KEY_DATE = "Date";
    private static final String KEY_SAVED = "Saved";





    public ArrayList<GalleryModel> parseJson(String jsonstring) throws ParseException {
        ArrayList<GalleryModel> galleryModels = new ArrayList<GalleryModel>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            JSONArray jsonarray = new JSONArray(jsonstring);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                GalleryModel galleryModel = new GalleryModel();
                galleryModel.setID_Pic(jsonobject.getInt(KEY_IDP));
                galleryModel.setID_User(jsonobject.getInt(KEY_IDU));
                galleryModel.setLikes(jsonobject.getInt(KEY_LIKES));
                galleryModel.setPic_Url(jsonobject.getString(KEY_PICN));
                galleryModel.setLiked(jsonobject.getInt(KEY_LIKED));
                galleryModel.setDescription(jsonobject.getString(KEY_DESC));
                galleryModel.setFoodName(jsonobject.getString(KEY_FNAME));
                galleryModel.setName(jsonobject.getString(KEY_UNAME));
                galleryModel.setPic_url_user(jsonobject.getString(KEY_PICNU));
                galleryModel.setIs_Following(jsonobject.getInt(KEY_ISFOLLOWING));
                galleryModel.setnComments(jsonobject.getInt(KEY_NCOMMENTS));
                galleryModel.setSaved(jsonobject.getInt(KEY_SAVED));

                String dateStr = jsonobject.getString(KEY_DATE);
                Date date = sdf.parse(dateStr);
                galleryModel.setDate(date);

                galleryModels.add(galleryModel);

            }
            return galleryModels;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return galleryModels;
    }

}
