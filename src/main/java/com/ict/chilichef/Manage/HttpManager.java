package com.ict.chilichef.Manage;

/**
 * Created by Maziyar on 5/23/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ict.chilichef.JsonParser.JsonParserCategories;
import com.ict.chilichef.JsonParser.JsonParserSearchIngredients;
import com.ict.chilichef.Model.CategoriesModel;
import com.ict.chilichef.Model.SearchIngredientsModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.ict.chilichef.MainActivity.URL_BASE;

public class HttpManager {

    public static boolean connected;

    static String path = "/data/data/com.ict.chilichef/databases/foodapp.db";

    private static SQLiteDatabase mydatabase;

    private static Context context;
    private static String adress;


    public HttpManager(Context context, String adress) throws JSONException {


        this.adress = adress;
        this.context = context;


    }

    public static boolean fetchCategories(Context context, String adress) {

        connected = checkNetwork(context);
        boolean sec = false;

        ArrayList<CategoriesModel> categoriesModels;

        if (connected == true) {

            mydatabase = context.openOrCreateDatabase(path, MODE_PRIVATE, null);

            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS categories(Id_Cat int,Name VARCHAR, Parent int, isLeaf BOOL, Description TEXT);");

            String categorisJson = getDataHttp(adress);

            List<String> resultList = new LinkedList<>();

            JsonParserCategories jsonParserCategories = new JsonParserCategories();

            categoriesModels = jsonParserCategories.parseJson(categorisJson);


            Cursor cursor = mydatabase.rawQuery("select count(*) FROM categories", null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            if (count == 0) {
                for (CategoriesModel cm : categoriesModels) {

                    resultList.add(cm.toString());

                    mydatabase.execSQL("INSERT INTO categories VALUES('" + cm.getId_Cat() + "','" + cm.getName() + "','" + cm.getParent() + "','" + cm.isLeaf() + "','" + cm.getDescription() + "')");
                }
            }

            Cursor cursor2 = mydatabase.rawQuery("select count(*) FROM categories", null);
            cursor2.moveToFirst();
            int count2 = cursor2.getInt(0);

            if (count2 == categoriesModels.size()) {

                sec = true;
            }

        }

        return sec;
    }

    public static boolean fetchIngredients(Context context, String adress) throws JSONException {

        connected = checkNetwork(context);
        ArrayList<SearchIngredientsModel> searchIngredientsModels;

        boolean sec = false;

        if (connected == true) {

            mydatabase = context.openOrCreateDatabase(path, MODE_PRIVATE, null);

            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS ingredients(Id_Ing int,Name VARCHAR, Status int);");

            String categorisJson = getDataHttp(adress);

            JsonParserSearchIngredients jsonParserSearchIngredients = new JsonParserSearchIngredients();

            searchIngredientsModels = jsonParserSearchIngredients.parseJson(categorisJson);
            Cursor cursor = mydatabase.rawQuery("select count(*) FROM ingredients", null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            if (count == 0) {
                for (SearchIngredientsModel cm : searchIngredientsModels) {
                    mydatabase.execSQL("INSERT INTO ingredients VALUES('" + cm.getID() + "','" + cm.getName() +"','"+ cm.getStatus()+ "')");
                }

                Cursor cursor2 = mydatabase.rawQuery("select count(*) FROM ingredients", null);
                cursor2.moveToFirst();
                int count2 = cursor2.getInt(0);

                if (count2 == searchIngredientsModels.size()) {

                    sec = true;
                }

            }


        } else {

            //fetchingDataProblem(context, "Ing");

        }

        return sec;

    }

    public static boolean checkNetwork(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }

    public static String getDataHttp(String adress) {

        String text = "";
        URL url = null;


        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();


            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getFoodsDataCategoryLegacy(int id_cat) {

        String adress = URL_BASE + "Pages/getFoodsCategoryDataLegacy.php?Id_Cat=" + id_cat;

        String text = "";
        URL url = null;


        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getFoodsDataCategoryProposed(int id_cat) {

        String adress = URL_BASE + "Pages/getFoodsCategoryDataProposed.php?Id_Cat=" + id_cat;

        String text = "";
        URL url = null;


        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getFoodsCategoryDataEvents(int id_cat) {

        String adress = URL_BASE + "Pages/getFoodsCategoryDataEvents.php?Id_Cat=" + id_cat;

        String text = "";
        URL url = null;


        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static Cursor getCategoriesLocal(String adress, int ID) {

        Cursor c = mydatabase.rawQuery("SELECT * FROM categories WHERE Parent=+" + ID, null);

        return c;
    }

    public static String getCategories(String adress, int ID) {

        String text = "";
        URL url = null;

        String key = "Id_Cat";

        try {
            String data = "?" + key + "=" + Integer.toString(ID);
            adress = adress + data;

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isLeafCheck(Context context, int id) {

        mydatabase = context.openOrCreateDatabase(path, MODE_PRIVATE, null);

        mydatabase.execSQL("SELECT isLeaf FROM categories WHERE Id_Cat=" + id + ";");


        Cursor cursor = mydatabase.rawQuery("SELECT isLeaf FROM categories WHERE Id_Cat=" + id + ";", null);


        return false;
    }

    public static int registerUser(String username, String password, String name) throws UnsupportedEncodingException {

        String adress = URL_BASE + "Pages/registration.php?userName=" + username + "&" + "Password=" + md5(password) + "&" + "name=" + URLEncoder.encode(name, "UTF-8");

        String text = "";
        URL url = null;

        int result = -1;

        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();

        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

            JSONObject jsonobject = new JSONObject(text);

            result = jsonobject.getInt("success");

        } catch (IOException o) {
            o.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return result;

    }

    public static String getFoodsDataCategory(int id_cat) {

        String adress = URL_BASE + "Pages/getFoodsCategoryData.php?Id_Cat=" + id_cat;

        String text = "";
        URL url = null;


        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getDataUser(String userName, String password) {

        String adress = URL_BASE + "Pages/login.php?userName=" + userName + "&" + "Password=" + md5(password);

        String text = "";
        URL url = null;


        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getFollowings(int ID) {

        String adress = URL_BASE + "Pages/getFollowings.php?Id_User=" + ID;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getFollowers(int ID) {

        String adress = URL_BASE + "Pages/getFollowers.php?Id_User=" + ID;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getRelatedPic(int IdUser, String FoodName) throws UnsupportedEncodingException {

        String adress = URL_BASE + "Pages/relatedPics.php?Id_User=" + IdUser + "&Keyword=" + URLEncoder.encode(FoodName, "UTF-8");

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String whatToCook(HashMap data) throws UnsupportedEncodingException {

        String adress = URL_BASE + "Pages/whatToCook.php?I_1=" + data.get("I_1") + "&I_2=" + data.get("I_2")
                + "&I_3=" + data.get("I_3") + "&I_4=" + data.get("I_4") + "&I_5=" + data.get("I_5")
                + "&I_6=" + data.get("I_6") + "&I_7=" + data.get("I_7") + "&I_8=" + data.get("I_8") + "&Id_User=" + data.get("Id_User");

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String Follow(int User1, int User2) {

        String adress = URL_BASE + "Pages/follow.php?Id_User1=" + User1 + "&Id_User2=" + User2;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String UnFollow(int User1, int User2) {

        String adress = URL_BASE + "Pages/unFollow.php?Id_User1=" + User1 + "&Id_User2=" + User2;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getGalleryComments(int ID_PicGallery, int ID_User) {

        String adress = URL_BASE + "Pages/getGalleryComments.php?ID_PicGallery=" + ID_PicGallery + "&Id_User=" + ID_User;


        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String sendCommentGallery(int User, String comment, int IdPicGallery) throws UnsupportedEncodingException {

        String adress = URL_BASE + "Pages/sendCommentGallery.php?Id_User=" + User + "&Comment=" + URLEncoder.encode(comment, "UTF-8") + "&ID_PicGallery=" + IdPicGallery;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String searchUsers(String keyword, int ID) throws UnsupportedEncodingException {

        String adress = URL_BASE + "Pages/searchUsers.php?Keyword=" + URLEncoder.encode(keyword, "UTF-8") + "&id_User=" + ID;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String searchGallery(String keyword, int IdUser) throws UnsupportedEncodingException {

        String adress = URL_BASE + "Pages/searchPictures.php?Keyword=" + URLEncoder.encode(keyword, "UTF-8") + "&Id_User=" + IdUser;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String searchRecepies(String keyword) throws UnsupportedEncodingException {

        String adress = URL_BASE + "Pages/searchRecepies.php?Keyword=" + URLEncoder.encode(keyword, "UTF-8");

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getRecipe(int Id_Food, int Id_User) {

        String adress = URL_BASE + "Pages/getRecipe.php?Id_Food=" + Id_Food + "&Id_User=" + Id_User;


        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getIngredients(int id) {

        String adress = URL_BASE + "Pages/getIngredients.php?Id_Food=" + id;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String FirstPicsQueries(String Method, int number, String IdUser) throws UnsupportedEncodingException {

        String adress = URL_BASE + "Pages/firstPagePicsQueries.php?Method=" + Method + "&Count=" + number + "&Id_User=" + IdUser;

        String text = "";
        URL url = null;

        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getFriendsFollowings(int UserID, int FriendID) {


        String adress = URL_BASE + "Pages/getFriendsFollowings.php?Id_User=" + UserID + "&Id_Friend=" + FriendID;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getFriendsFollowers(int UserID, int FriendID) {

        String adress = URL_BASE + "Pages/getFriendsFollowers.php?Id_User=" + UserID + "&Id_Friend=" + FriendID;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getUserGallery(int UserID, int Id_Viewer) {

        String adress = URL_BASE + "Pages/getUserGallery.php?Id_User=" + UserID + "&Id_Viewer=" + Id_Viewer;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getUserRecipes(int UserID) {

        String adress = URL_BASE + "Pages/getUserRecipes.php?Id_User=" + UserID;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static void editProfileUser(String username, String password, String name) throws IOException, InterruptedException {

        String adress = URL_BASE + "Pages/editProfile.php?userName=" + username + "&" + "Password=" + password + "&" + "name=" + URLEncoder.encode(name, "UTF-8") + "&file=";


        HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(adress).openConnection();
        httpUrlConnection.setDoOutput(true);
        httpUrlConnection.setRequestMethod("POST");
        OutputStream os = httpUrlConnection.getOutputStream();
        Thread.sleep(1000);
        BufferedInputStream fis = new BufferedInputStream(new FileInputStream("tmpfile.tmp"));


        os.close();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        httpUrlConnection.getInputStream()));

        String s = null;
        while ((s = in.readLine()) != null) {
            System.out.println(s);
        }
        in.close();
        fis.close();
    }

    public static String gallerylike(int User1, int IdPicture) {

        String adress = URL_BASE + "Pages/galleryLike.php?Id_User=" + User1 + "&Id_Picture=" + IdPicture;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;
    }

    public static String galleryUnLike(int User1, int IdPicture) {

        String adress = URL_BASE + "Pages/galleryUnLike.php?Id_User=" + User1 + "&Id_Picture=" + IdPicture;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;
    }

    public static String getNumbers(int Id_User) {

        String adress = URL_BASE + "Pages/getNumbers.php?Id_User=" + Id_User;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getRecipeComments(int id, int ID_User) {

        String adress = URL_BASE + "Pages/getRecipeComments.php?ID_Recipe=" + id + "&Id_User=" + ID_User;


        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String sendCommentRecipe(int User, String comment, int ID_Recipe) throws UnsupportedEncodingException {

        String adress = URL_BASE + "Pages/sendCommentRecipe.php?Id_User=" + User + "&Comment=" + URLEncoder.encode(comment, "UTF-8") + "&ID_Recipe=" + ID_Recipe;


        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String recipeLike(int User1, int Id_Recipe) {

        String adress = URL_BASE + "Pages/recipeLike.php?Id_User=" + User1 + "&Id_Recipe=" + Id_Recipe;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;
    }

    public static String recipeUnLike(int User1, int Id_Recipe) {

        String adress = URL_BASE + "Pages/recipeUnLike.php?Id_User=" + User1 + "&Id_Recipe=" + Id_Recipe;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;
    }

    public static String recipeFavorite(int User1, int Id_Recipe) {

        String adress = "http://ashpazchili.ir/Chili/Pages/addToFavorites.php?Id_User=" + User1 + "&Id_Food=" + Id_Recipe;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;
    }

    public static String recipeDisplayFavorite(int User1) {

        String adress = URL_BASE + "Pages/getFavorites.php?Id_User=" + User1;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;
    }

    public static String recipeUnFavorite(int User1, int Id_Recipe) {

        String adress = URL_BASE + "Pages/removeFromFavorites.php?Id_User=" + User1 + "&Id_Food=" + Id_Recipe;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;
    }

    public static String FirstPageUsersQueries(String Method, int number, int Id_User) throws UnsupportedEncodingException {

        String adress = URL_BASE + "Pages/firstPageUsersQueries.php?Method=" + Method + "&Count=" + number + "&Id_User=" + Id_User;


        String text = "";
        URL url = null;


        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String FirstPageFoodsQueries(String Method, int number) throws UnsupportedEncodingException {

        String adress = URL_BASE + "Pages/firstPageFoodsQueries.php?Method=" + Method + "&Count=" + number;


        String text = "";
        URL url = null;


        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String deleteCommentGallery(int id_comment) throws UnsupportedEncodingException {

        String adress = URL_BASE + "Pages/deleteCommentGallery.php?ID_Comment=" + id_comment;


        String text = "";
        URL url = null;

        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String deleteCommentRecipe(int id_comment) {

        String adress = URL_BASE + "Pages/deleteCommentRecipe.php?ID_Comment=" + id_comment;

        String text = "";
        URL url = null;

        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getNotes(int Id_Cat) {

        String adress = URL_BASE + "Pages/getNotes.php?Id_Cat=" + Id_Cat;


        String text = "";
        URL url = null;

        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getCategoricalDesign(int id_Cat) {

        String adress = URL_BASE + "Pages/getCategoricalDesign.php?id_Cat=" + id_Cat;

        String text = "";
        URL url = null;


        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getDesign(int Id_Design, int UserID) {

        String adress = URL_BASE + "Pages/getDesign.php?ID=" + Id_Design + "&ID_User=" + UserID;


        String text = "";
        URL url = null;

        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getAdvertise(int Position_adv) {

        String adress = URL_BASE + "Pages/getAdverties.php?Position=" + Position_adv;


        String text = "";
        URL url = null;

        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String reportGallery(int ID_Pic, String Report) {

        String adress = null;
        try {
            adress = URL_BASE + "Pages/reportGallery.php?ID_Pic=" + ID_Pic + "&Report=" + URLEncoder.encode(Report, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String text = "";
        URL url = null;

        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String reportRecipe(int ID_Recipe, String Report) {

        String adress = null;
        try {
            adress = URL_BASE + "Pages/reportRecipe.php?ID_Recipe=" + ID_Recipe + "&Report=" + URLEncoder.encode(Report, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String text = "";
        URL url = null;

        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String reportCommentsRecipe(int ID_Comment, String Report) throws UnsupportedEncodingException {

        String adress = URL_BASE + "Pages/reportCommentsRecipe.php?ID_Comment=" + ID_Comment + "&Report=" + URLEncoder.encode(Report, "UTF-8");

        String text = "";
        URL url = null;

        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String reportCommentsGallery(int ID_Comment, String Report) {

        String adress = null;
        try {
            adress = URL_BASE + "Pages/reportCommentsGallery.php?ID_Comment=" + ID_Comment + "&Report=" + URLEncoder.encode(Report, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String text = "";
        URL url = null;

        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String getGalleryLikes(int ID_Pic, int Id_User) {

        String adress = URL_BASE + "Pages/getGalleryDataLikes.php?PictureID=" + ID_Pic + "&UserID =" + Id_User;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;
    }

    public static String getGalleryDateLikes(int ID_Pic, int Id_User) {

        String adress = URL_BASE + "Pages/getGalleryDataLikes.php?ID_Pic=" + ID_Pic + "&Id_User=" + Id_User;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;
    }

    public static String setCurrentTime(int Id_User) {

        String adress = URL_BASE + "Pages/setCurrentTime.php?Id_User=" + Id_User;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;
    }

    public static String getUsersStatus(int Id_User) {

        String adress = URL_BASE + "Pages/getUsersStatus.php?Id_User=" + Id_User;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;
    }

    public static int RecoveryPassword(String Username) {

        String adress = URL_BASE + "Pages/resetPassEmail.php?email=" + Username;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line);
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return Integer.parseInt(text);
    }

    public static String SendValidationEmail(String Username) {

        String adress = URL_BASE + "Pages/sendValidationEmail.php?UserName=" + Username;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;
    }

    public static String getFoodsCategory(int id_cat, String method) {

        String adress = URL_BASE + "Pages/getFoodsCategory.php?Id_Cat=" + id_cat + "&" + "Method=" + method;

        String text = "";
        URL url = null;


        try {


            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }


        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;

    }

    public static String galleryBookmark(int User1, int ID_Pic) {

        String adress = URL_BASE + "Pages/galleryBookmark.php?Id_User=" + User1 + "&Id_Picture=" + ID_Pic;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;
    }

    public static String galleryUnBookmark(int User1, int ID_Pic) {

        String adress = URL_BASE + "Pages/galleryUnBookmark.php?Id_User=" + User1 + "&Id_Picture=" + ID_Pic;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;
    }

    public static String getBookmarkedGallery(int method, int Id_User) {

        String adress = URL_BASE + "Pages/getBookmarkedGallery.php?Method=" + method + "&Id_User=" + Id_User;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;
    }

    public static String getGallery(int method, int Id_User) {

        String adress = URL_BASE + "Pages/getGallery.php?Method=" + method + "&Id_User=" + Id_User;

        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;
    }

    public static String designBookmark(int Id_User, int Id_Design) {

        String adress = URL_BASE + "Pages/designBookmark.php?Id_User=" + Id_User + "&Id_Design=" + Id_Design;
        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;
    }

    public static String designUnBookmark(int Id_User, int Id_Design) {

        String adress = URL_BASE + "Pages/designUnBookmark.php?Id_User=" + Id_User + "&Id_Design=" + Id_Design;
        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;
    }

    public static String getBookmarkedDesign(int Id_User) {

        String adress = URL_BASE + "Pages/getBookmarkedDesign.php?Id_User=" + Id_User;
        String text = "";
        URL url = null;

        try {

            url = new URL(adress);

        } catch (MalformedURLException m) {
            m.printStackTrace();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder stringBuilder = new StringBuilder();


        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonParam = new JSONObject();

            // Allow Inputs
            urlConnection.setDoInput(true);
            // Allow Outputs
            urlConnection.setDoOutput(true);

            // Get the server response
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();

        } catch (IOException o) {
            o.printStackTrace();
        }

        return text;
    }


}
