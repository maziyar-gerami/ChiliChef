package com.ict.chilichef.JsonParser;


import com.ict.chilichef.Model.UsersStatusModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NP on 10/29/2017.
 */

public class JsonParserUsersStatus {

    private static final String KEY_Status = "Status";


    UsersStatusModel usersStatusModel = new UsersStatusModel();

    public UsersStatusModel parseJson(String jsonstring) {

        try {

            JSONObject jsonobject = new JSONObject(jsonstring);

            usersStatusModel.setStatus(jsonobject.getInt(KEY_Status));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return usersStatusModel;
    }
}
