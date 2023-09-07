package com.ict.chilichef.JsonParser;


import com.ict.chilichef.Model.NumbersModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Maziyar on 7/7/2017.
 */

public class JsonParserNumbers {

    private static final String KEY_NFO = "numberOfFollowings";
    private static final String KEY_NFR = "numberOfFollowers";
    private static final String KEY_NPI     = "numberOfPictures";
    private static final String KEY_NRE     = "numberOfRecipes";
    private static final String KEY_SCO     = "score";
    private static final String KEY_LEV     = "level";



    public NumbersModel parseJson(String jsonstring){

        NumbersModel numbersModel = new NumbersModel();


        try {

            JSONArray jsonarray = new JSONArray(jsonstring);
            JSONObject jsonobject = jsonarray.getJSONObject(0);

            numbersModel.setNumberOfFollowings(jsonobject.getInt(KEY_NFO));
            numbersModel.setNumberOfFollowers(jsonobject.getInt(KEY_NFR));
            numbersModel.setNumberOfPictures(jsonobject.getInt(KEY_NPI));
            numbersModel.setNumberOfRecipes(jsonobject.getInt(KEY_NRE));
            numbersModel.setLevel(jsonobject.getInt(KEY_LEV));
            numbersModel.setScore(jsonobject.getInt(KEY_SCO));



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return numbersModel;
    }

}
