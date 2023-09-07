package com.ict.chilichef.JsonParser;

import com.ict.chilichef.Model.Data;
import com.ict.chilichef.Model.NotesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by roshan on 12/3/2017.
 */

public class JsonParserDataDesign {

    private static final String KEY_ID = "Id_Design";
    private static final String KEY_Title = "Title";
    private static final String KEY_PicUrl = "Pic_URL";

    public ArrayList<Data> parseJson(String jsonstring) {
        ArrayList<Data> dataModels = new ArrayList<Data>();
        try {

            JSONArray jsonarray = new JSONArray(jsonstring);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                Data data = new Data();
                data.setTitle(jsonobject.getString(KEY_Title));
                data.setId_cat(jsonobject.getInt(KEY_ID));
                data.setPicUrl((jsonobject.getString(KEY_PicUrl)));


                dataModels.add(data);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataModels;
    }

}
