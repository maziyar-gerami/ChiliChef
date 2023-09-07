package com.ict.chilichef.JsonParser;


import com.ict.chilichef.Model.RecipeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Maziyar on 6/29/2017.
 */

public class JsonParserRecipe {

    private static final String KEY_ID = "id_Food";
    private static final String KEY_TIT = "Title";
    private static final String KEY_REC = "Recepies";
    private static final String KEY_LIK = "Likes";
    private static final String KEY_SCO = "Score";
    private static final String KEY_TIM = "Time";
    private static final String KEY_MEA = "Meal";
    private static final String KEY_PICN = "Pic_URL";
    private static final String KEY_WRI = "Writer";
    private static final String KEY_LIKED = "Liked";
    private static final String KEY_SUT = "SubmittedTime";
    private static final String KEY_NAME = "Name";
    private static final String KEY_PNU = "Pic_Name_User";
    private static final String KEY_CATL = "Cat_l";
    private static final String KEY_CATP = "Cat_p";
    private static final String KEY_ISFOLLOWING = "isFollowing";
    private static final String KEY_SAVED = "Saved";
    private static final String KEY_HARD = "Hard";
    private static final String KEYKEY_VIEWS = "Views";
    private static final String KEY_NCOMMENTS ="nComments";

    RecipeModel recipeModel = new RecipeModel();

    public RecipeModel parseJson(String jsonstring) {
        ArrayList<RecipeModel> recipeModels = new ArrayList<RecipeModel>();

        try {

            JSONArray jsonarray = new JSONArray(jsonstring);
            JSONObject jsonobject = jsonarray.getJSONObject(0);

            recipeModel.setId_Food(jsonobject.getString(KEY_ID));
            recipeModel.setTitle(jsonobject.getString(KEY_TIT));
            recipeModel.setRecepies(jsonobject.getString(KEY_REC));
            recipeModel.setLikes(jsonobject.getInt(KEY_LIK));
            recipeModel.setScore(jsonobject.getInt(KEY_SCO));
            recipeModel.setTime(jsonobject.getInt(KEY_TIM));
            recipeModel.setMeal(jsonobject.getInt(KEY_MEA));
            recipeModel.setPic_URL(jsonobject.getString(KEY_PICN));
            recipeModel.setWriter(jsonobject.getInt(KEY_WRI));
            recipeModel.setSubmittedTime(jsonobject.getString(KEY_SUT));
            recipeModel.setLiked(jsonobject.getInt(KEY_LIKED));
            recipeModel.setWriterName(jsonobject.getString(KEY_NAME));
            recipeModel.setWriter_Pic_URL(jsonobject.getString(KEY_PNU));
            recipeModel.setCat_l(jsonobject.getString(KEY_CATL));
            recipeModel.setCat_p(jsonobject.getString(KEY_CATP));
            recipeModel.setIs_Following(jsonobject.getInt(KEY_ISFOLLOWING));
            recipeModel.setSaved(jsonobject.getInt(KEY_SAVED));
            recipeModel.setHard(jsonobject.getInt(KEY_HARD));
            recipeModel.setViews(jsonobject.getInt(KEYKEY_VIEWS));
            recipeModel.setnComments(jsonobject.getInt(KEY_NCOMMENTS));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipeModel;
    }


}