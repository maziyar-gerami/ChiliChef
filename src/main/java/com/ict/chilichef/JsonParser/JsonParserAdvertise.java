package com.ict.chilichef.JsonParser;


import com.ict.chilichef.Model.advertiseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by NP on 9/3/2017.
 */

public class JsonParserAdvertise {


    private static final String KEY_IDA 	= "ID";
    private static final String KEY_NAME	= "Name";
    private static final String KEY_URLP	= "Pic_Url";
    private static final String KEY_URLT	= "Target_Url";

    public ArrayList<advertiseModel> parseJson(String jsonstring){
        ArrayList<advertiseModel> advertiseModels = new ArrayList<advertiseModel>();

        try {

            JSONArray jsonarray = new JSONArray(jsonstring);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                advertiseModel advertiseModel = new advertiseModel();
                advertiseModel.setID(jsonobject.getInt(KEY_IDA));
                advertiseModel.setName(jsonobject.getString(KEY_NAME));
                advertiseModel.setPic_Url(jsonobject.getString(KEY_URLP));
                advertiseModel.setTarget_Url(jsonobject.getString(KEY_URLT));

                advertiseModels.add(advertiseModel);
            }
            return advertiseModels;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return advertiseModels;
    }

}
