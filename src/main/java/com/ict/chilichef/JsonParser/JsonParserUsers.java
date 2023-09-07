package com.ict.chilichef.JsonParser;


import com.ict.chilichef.Model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Maziyar on 5/27/2017.
 */

public class JsonParserUsers {

    private static final String KEY_SUC 	= "success";
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
    private static final String KEY_Prompted = "Prompted";
    private static final String KEY_Status = "Status";
    private static final String KEY_NFO = "numberOfFollowings";
    private static final String KEY_NFR = "numberOfFollowers";




    public UserModel parseJson(String jsonstring){
        UserModel userModel = new UserModel();
        try {

            JSONArray jsonarray = new JSONArray(jsonstring);
            JSONObject jsonobject = jsonarray.getJSONObject(0);
            userModel.setSuccess(jsonobject.getBoolean(KEY_SUC ));
            if (jsonobject.getBoolean(KEY_SUC )==true) {
                userModel.setId_user(jsonobject.getInt(KEY_ID));
                userModel.setUsername(jsonobject.getString(KEY_UNAME));
                userModel.setName(jsonobject.getString(KEY_NAME));
                userModel.setPass(jsonobject.getString(KEY_PASS));
                userModel.setScore(jsonobject.getInt(KEY_SCORE));
                userModel.setLevel(jsonobject.getInt(KEY_LEVEL));
                userModel.setPaid(jsonobject.getInt(KEY_PAID));
                userModel.setPic_Url(jsonobject.getString(KEY_PICN));
                userModel.setDescription(jsonobject.getString(KEY_DESC));
                userModel.setTelephone(jsonobject.getInt(KEY_TEL));
                userModel.setLenght(jsonobject.getInt(KEY_LENGHT));
                userModel.setWeight(jsonobject.getInt(KEY_WEIGHT));
                userModel.setFat(jsonobject.getString(KEY_FAT));
                userModel.setSugar(jsonobject.getString(KEY_SUGAR));
                userModel.setPrompted(jsonobject.getString(KEY_Prompted));
                userModel.setStatus(jsonobject.getInt(KEY_Status));
                userModel.setnFollowing(jsonobject.getInt(KEY_NFO));
                userModel.setnFollower(jsonobject.getInt(KEY_NFR));


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



        return userModel;
    }
}
