package com.ict.chilichef.JsonParser;


import com.ict.chilichef.Model.DesignModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Elham on 8/24/2017.
 */

public class JsonParserDesign {

    private static final String KEY_Title 	      = "Title";
    private static final String KEY_Description   = "Description";
    private static final String KEY_PICS   = "Pics";
    private static final String KEY_Saved   = "Saved";



    DesignModel designModel = new DesignModel();

    public DesignModel parseJson(String jsonstring) {

        try {

            JSONArray jsonarray = new JSONArray(jsonstring);
            JSONObject jsonobject = jsonarray.getJSONObject(0);

            designModel.setTitle(jsonobject.getString(KEY_Title));
            designModel.setDescription(jsonobject.getString(KEY_Description));
            designModel.setSaved(jsonobject.getInt(KEY_Saved));

            JSONArray pics = jsonobject.getJSONArray("Pics");

            ArrayList <String> tempPics = new ArrayList<>();

            for (int i = 0; i < pics.length(); i++) {

                tempPics.add(pics.getString(i));
            }

            designModel.setPic_Url(tempPics);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return designModel;
    }

}
