package com.ict.chilichef.Sessions;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class UserSessionManager {

    // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "AndroidExamplePref";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "chef_icon";

    // Email address (make variable public to access from outside)
    public static final String KEY_PASS = "pass";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    public static final String KEY_IMAGE = "image";

    public static final String KEY_ID_USER = "UserId";

    public static final String KEY_TEL_USER = "UserTel";
    public static final String KEY_LENGHT_USER = "UserLenght";
    public static final String KEY_WEIGHT_USER = "UserWeight";
    public static final String KEY_FAT_USER = "UserFat";
    public static final String KEY_SUGAR_USER = "UserSugar";
    public static final String KEY_Prompted = "Prompted";
    public static final String KEY_Status = "Status";
    public static final String KEY_Level = "Level";



    // Constructor
    public UserSessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserLoginSession(String user, String pass, String email, String image, Integer UserId, Integer UserTel,
                                       Integer UserLenght, Integer UserWeight, String UserFat, String UserSugar, String Prompted, Integer Status,Integer Level){

        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, user);

        // Storing email in pref
        editor.putString(KEY_PASS, pass);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        //Storing image in Pref
        editor.putString(KEY_IMAGE, image);

        editor.putString(KEY_ID_USER, String.valueOf(UserId));

        editor.putString(KEY_TEL_USER, String.valueOf(UserTel));

        editor.putString(KEY_LENGHT_USER, String.valueOf(UserLenght));

        editor.putString(KEY_WEIGHT_USER, String.valueOf(UserWeight));

        editor.putString(KEY_FAT_USER, UserFat);

        editor.putString(KEY_SUGAR_USER, UserSugar);

        editor.putString(KEY_Prompted, Prompted);

        editor.putString(KEY_Status, String.valueOf(Status));

        editor.putString(KEY_Level, String.valueOf(Level));

        // commit changes
        editor.apply();
    }

    /**
     * Check login method will check chef_icon login status
     * If false_email it will redirect chef_icon to login page
     * Else do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){

            // chef_icon is not logged in redirect him to Login Activity

            return true;
        }
        return false;
    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){

        //Use hashmap to store chef_icon credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // chef_icon name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // chef_icon pass id
        user.put(KEY_PASS, pref.getString(KEY_PASS, null));

        // chef_icon email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        user.put(KEY_IMAGE,pref.getString(KEY_IMAGE,null));

        user.put(KEY_ID_USER, pref.getString(KEY_ID_USER,null));

        user.put(KEY_TEL_USER, pref.getString(KEY_TEL_USER,null));

        user.put(KEY_LENGHT_USER, pref.getString(KEY_LENGHT_USER,null));

        user.put(KEY_WEIGHT_USER, pref.getString(KEY_WEIGHT_USER,null));

        user.put(KEY_FAT_USER, pref.getString(KEY_FAT_USER,null));

        user.put(KEY_SUGAR_USER, pref.getString(KEY_SUGAR_USER,null));

        user.put(KEY_Prompted, pref.getString(KEY_Prompted,null));

        user.put(KEY_Status, pref.getString(KEY_Status,null));

        user.put(KEY_Level, pref.getString(KEY_Level,null));



        // return chef_icon
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){

        // Clearing all chef_icon data from Shared Preferences
        editor.clear();
        editor.apply();


//        Intent intent= new Intent(_context, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        _context.startActivity(intent);
    }


    // Check for login
    public boolean isUserLoggedIn(){



        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}