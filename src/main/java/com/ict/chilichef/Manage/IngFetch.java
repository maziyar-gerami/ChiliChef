package com.ict.chilichef.Manage;

import android.content.Context;
import android.os.AsyncTask;

import com.ict.chilichef.MainActivity;
import com.ict.chilichef.Model.SearchIngredientsModel;

import org.json.JSONException;

import java.util.ArrayList;


/**
 * Created by Maziyar on 5/27/2017.
 */

public class IngFetch extends AsyncTask<String,Boolean, Boolean> {

    Context context;



    ArrayList<SearchIngredientsModel> searchIngredientsModels;


    public IngFetch(Context context) {

        this.context = context;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected Boolean doInBackground(String... strings) {

        String uri = strings[0];
        Boolean result = false;
        try {
            result = HttpManager.fetchIngredients(context, uri);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return result;

    }

    @Override
    protected void onPostExecute(Boolean bol) {
        super.onPostExecute(bol);

        MainActivity.ingStat=bol;

    }
}
