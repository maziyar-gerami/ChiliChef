package com.ict.chilichef.JsonParser;


import com.ict.chilichef.Model.UsersPromptedModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NP on 10/29/2017.
 */

public class JsonParserUsersPrompted {

    private static final String KEY_Prompted = "Prompted";
    private static final String KEY_Status = "Status";
    private static final String KEY_ID 		= "id_User";
    private static final String KEY_UNAME 	= "Username";
    private static final String KEY_PASS 	= "Password";
    private static final String KEY_NAME 	= "Name";
    private static final String KEY_SCORE	= "Score";
    private static final String KEY_LEVEL	= "Level";
    private static final String KEY_PAID	= "Paid";
    private static final String KEY_PICN	= "Pic_Name";
    private static final String KEY_DESC	= "Description";
    private static final String KEY_TEL     = "UserTel";
    private static final String KEY_LENGHT  = "UserLenght";
    private static final String KEY_WEIGHT  = "UserWeight";
    private static final String KEY_FAT     = "UserFat";
    private static final String KEY_SUGAR   = "UserSugar";



    UsersPromptedModel usersPromptedModel = new UsersPromptedModel();

    public UsersPromptedModel parseJson(String jsonstring) {

        try {

            JSONArray jsonArray = new JSONArray(jsonstring);

            JSONObject jsonobject = jsonArray.getJSONObject(0);

            usersPromptedModel.setPrompted(jsonobject.getString(KEY_Prompted));
            usersPromptedModel.setStatus(jsonobject.getInt(KEY_Status));
            usersPromptedModel.setId_user(jsonobject.getInt(KEY_ID));
            usersPromptedModel.setUsername(jsonobject.getString(KEY_UNAME));
            usersPromptedModel.setName(jsonobject.getString(KEY_NAME));
            usersPromptedModel.setPass(jsonobject.getString(KEY_PASS));
            usersPromptedModel.setScore(jsonobject.getInt(KEY_SCORE));
            usersPromptedModel.setLevel(jsonobject.getInt(KEY_LEVEL));
            usersPromptedModel.setPaid(jsonobject.getInt(KEY_PAID));
            usersPromptedModel.setPic_Url(jsonobject.getString(KEY_PICN));
            usersPromptedModel.setDescription(jsonobject.getString(KEY_DESC));
            usersPromptedModel.setTelephone(jsonobject.getInt(KEY_TEL));
            usersPromptedModel.setLenght(jsonobject.getInt(KEY_LENGHT));
            usersPromptedModel.setWeight(jsonobject.getInt(KEY_WEIGHT));
            usersPromptedModel.setFat(jsonobject.getString(KEY_FAT));
            usersPromptedModel.setSugar(jsonobject.getString(KEY_SUGAR));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return usersPromptedModel;
    }
}
