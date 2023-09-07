package com.ict.chilichef.Sessions;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;


/**
 * Created by Elham on 7/27/2017.
 */

public class SettingsSessionManager {

    SharedPreferences preferences;
    Editor myEditor;
    Context context;
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFERENCE_NAME = "AndroidExitPref";

    // All Shared Preferences Keys
    private static final String EXIT_STATUS = "exitStatus";

    // User name (make variable public to access from outside)
    public static final String KEY_STATUS = "0";

    public SettingsSessionManager(Context context) {
        this.context = context;
        preferences=context.getSharedPreferences(PREFERENCE_NAME,PRIVATE_MODE);
        myEditor=preferences.edit();

    }



    public void createExitSession(String status){

        myEditor.putBoolean(EXIT_STATUS,true);
        myEditor.putString(KEY_STATUS,status);
        myEditor.apply();
    }

    public boolean checkedExitMode(){
        // Check login status
        if(!this.exitStatus()){

            // chef_icon is not logged in redirect him to Login Activity

            return true;
        }
        return false;
    }


    public HashMap<String, String> getExitDetails(){

        //Use hashmap to store chef_icon credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // chef_icon name
        user.put(KEY_STATUS, preferences.getString(KEY_STATUS, null));

        // return chef_icon
        return user;
    }


    public void OriginalSettings(){

        // Clearing all settings data from Shared Preferences
         myEditor.clear();
        myEditor.apply();
    }



    public boolean exitStatus(){
        return preferences.getBoolean(EXIT_STATUS, false);
    }


}
