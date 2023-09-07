package com.ict.chilichef.JsonParser;

import com.ict.chilichef.Model.GalleryCommentsModel;
import com.ict.chilichef.Model.RecipeCommentsModel;

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

public class JsonParserRecipeComments {

    private static final String KEY_IDC 	= "ID_Comment";
    private static final String KEY_COMM 	= "Comment";
    private static final String KEY_DATE	= "Date";
    private static final String KEY_IDW	    = "ID_Writer";
    private static final String KEY_NAME	= "Name";
    private static final String KEY_PICU	= "Pic_Name_User";
    private static final String KEY_ISFOLLOWING ="isFollowing";
//    private static final String KEY_NCOMMENTS ="nComments";


    public ArrayList<RecipeCommentsModel> parseJson(String jsonstring) throws ParseException {
        ArrayList<RecipeCommentsModel> recipeCommentsModels = new ArrayList<RecipeCommentsModel>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            JSONArray jsonarray = new JSONArray(jsonstring);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                RecipeCommentsModel recipeCommentsModel = new RecipeCommentsModel();
                recipeCommentsModel.setID_Comment(jsonobject.getInt(KEY_IDC));
                recipeCommentsModel.setComment(jsonobject.getString(KEY_COMM));
                String dateStr = jsonobject.getString(KEY_DATE);
                Date date = sdf.parse(dateStr);
                recipeCommentsModel.setDate(date);
                recipeCommentsModel.setID_Writer(jsonobject.getInt(KEY_IDW));
                recipeCommentsModel.setName(jsonobject.getString(KEY_NAME));
                recipeCommentsModel.setPic_Url_User(jsonobject.getString(KEY_PICU));
                recipeCommentsModel.setIs_Following(jsonobject.getInt(KEY_ISFOLLOWING));
//                recipeCommentsModel.setnComments(jsonobject.getInt(KEY_NCOMMENTS));


                recipeCommentsModels.add(recipeCommentsModel);
            }
            return recipeCommentsModels;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipeCommentsModels;
    }
}
