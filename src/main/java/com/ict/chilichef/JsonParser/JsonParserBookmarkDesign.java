package com.ict.chilichef.JsonParser;


import com.ict.chilichef.Model.DesignBookmarkModel;
import com.ict.chilichef.Model.FoodsUserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Maziyar on 6/29/2017.
 */

public class JsonParserBookmarkDesign {

    private static final String KEY_ID = "ID_Design";
    private static final String KEY_TIT = "Title";
    private static final String KEY_SAVED = "Saved";
    private static final String KEY_PIC = "PicUrl";



    public ArrayList<DesignBookmarkModel> parseJson(String jsonstring){
        ArrayList<DesignBookmarkModel> designBookmarkModels = new ArrayList<DesignBookmarkModel>();

        try {

            JSONArray jsonarray = new JSONArray(jsonstring);

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);

                DesignBookmarkModel designBookmarkModel = new DesignBookmarkModel();

                designBookmarkModel.setID_Design(jsonobject.getInt(KEY_ID));
                designBookmarkModel.setTitle(jsonobject.getString(KEY_TIT));
                designBookmarkModel.setSaved(jsonobject.getInt(KEY_SAVED));

                try {

                    JSONArray PicUrl = jsonobject.getJSONArray("PicUrl");

                    ArrayList<String> tempPics = new ArrayList<>();

                    for (int j = 0; j < PicUrl.length(); j++) {

                        tempPics.add(PicUrl.getString(j));
                    }

                    designBookmarkModel.setPic_Url(tempPics);

                }catch (JSONException e){

                    ArrayList<String> tempPics = new ArrayList<>();
                    tempPics.add(jsonobject.getString(KEY_PIC));
                    designBookmarkModel.setPic_Url(tempPics);


                }



                designBookmarkModels.add(designBookmarkModel);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return designBookmarkModels;
    }


}