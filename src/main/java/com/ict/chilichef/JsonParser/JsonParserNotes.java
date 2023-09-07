package com.ict.chilichef.JsonParser;

import com.ict.chilichef.Model.NotesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Elham on 8/24/2017.
 */

public class JsonParserNotes {

    private static final String KEY_ID 	        	= "ID";
    private static final String KEY_Title       	= "Title";
    private static final String KEY_Description 	= "Description";




    public ArrayList<NotesModel> parseJson(String jsonstring){
        ArrayList<NotesModel> notesModels = new ArrayList<NotesModel>();
        try {

            JSONArray jsonarray = new JSONArray(jsonstring);
            for (int i = 0; i < jsonarray.length(); i++) {


                JSONObject jsonobject = jsonarray.getJSONObject(i);


                NotesModel notesModel = new NotesModel();
                notesModel.setID(jsonobject.getInt(KEY_ID));
                notesModel.setTitle(jsonobject.getString(KEY_Title));
                notesModel.setDescription(jsonobject.getString(KEY_Description));

                notesModels.add(notesModel);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return notesModels;
    }

}