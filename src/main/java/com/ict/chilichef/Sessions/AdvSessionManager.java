package com.ict.chilichef.Sessions;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;


/**
 * Created by Elham on 7/27/2017.
 */

public class AdvSessionManager {

    SharedPreferences preferences;
    Editor myEditor;
    Context context;
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFERENCE_NAME = "AndroidSavePref";

    // All Shared Preferences Keys
    private static final String SAVE_STATUS = "saveStatus";

    // User name (make variable public to access from outside)
    public static final String KEY_STATUS = "0";

    public AdvSessionManager(Context context) {
        this.context = context;
        preferences=context.getSharedPreferences(PREFERENCE_NAME,PRIVATE_MODE);
        myEditor=preferences.edit();

    }



    public void createSaveSession(String status){

        myEditor.putBoolean(SAVE_STATUS,true);
        myEditor.putString(KEY_STATUS,status);
        myEditor.apply();
    }

    public boolean checkedExitMode(){
        // Check login status
        if(!this.saveStatus()){

            // chef_icon is not logged in redirect him to Login Activity

            return true;
        }
        return false;
    }


    public HashMap<String, String> getSaveDetails(){

        //Use hashmap to store chef_icon credentials
        HashMap<String, String> adv = new HashMap<String, String>();

        // chef_icon name
        adv.put(KEY_STATUS, preferences.getString(KEY_STATUS, null));

        // return chef_icon
        return adv;
    }


    public void resetVariable(){

        // Clearing all settings data from Shared Preferences
         myEditor.clear();
        myEditor.apply();
    }



    public boolean saveStatus(){
        return preferences.getBoolean(SAVE_STATUS, false);
    }


}
