package com.ict.chilichef.JsonParser;

import com.ict.chilichef.Model.GalleryCommentsModel;

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

public class JsonParserGalleryComments {

    private static final String KEY_IDC 	= "ID_Comment";
    private static final String KEY_COMM 	= "Comment";
    private static final String KEY_DATE	= "Date";
    private static final String KEY_IDW	    = "ID_Writer";
    private static final String KEY_NAME	= "Name";
    private static final String KEY_PICU	= "Pic_Name_User";
    private static final String KEY_ISFOLLOWING ="isFollowing";
//    private static final String KEY_NCOMMENTS ="nComments";


    public ArrayList<GalleryCommentsModel> parseJson(String jsonstring) throws ParseException {
        ArrayList<GalleryCommentsModel> galleryCommentsModels = new ArrayList<GalleryCommentsModel>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            JSONArray jsonarray = new JSONArray(jsonstring);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                GalleryCommentsModel galleryCommentsModel = new GalleryCommentsModel();
                galleryCommentsModel.setID_Comment(jsonobject.getInt(KEY_IDC));
                galleryCommentsModel.setComment(jsonobject.getString(KEY_COMM));
                String dateStr = jsonobject.getString(KEY_DATE);
                Date date = sdf.parse(dateStr);
                galleryCommentsModel.setDate(date);
                galleryCommentsModel.setID_Writer(jsonobject.getInt(KEY_IDW));
                galleryCommentsModel.setName(jsonobject.getString(KEY_NAME));
                galleryCommentsModel.setPic_Url_User(jsonobject.getString(KEY_PICU));
                galleryCommentsModel.setIs_Following(jsonobject.getInt(KEY_ISFOLLOWING));
//                galleryCommentsModel.setnComments(jsonobject.getInt(KEY_NCOMMENTS));


                galleryCommentsModels.add(galleryCommentsModel);
            }
            return galleryCommentsModels;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return galleryCommentsModels;
    }
}
