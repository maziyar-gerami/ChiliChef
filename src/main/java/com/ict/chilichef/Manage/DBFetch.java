package com.ict.chilichef.Manage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


import com.ict.chilichef.MainActivity;
import com.ict.chilichef.Model.CategoriesModel;

import java.util.ArrayList;


/**
 * Created by Maziyar on 5/27/2017.
 */

public class DBFetch extends AsyncTask<String,Boolean, Boolean> {

    Context context;

//    ProgressDialog progress;

    ArrayList<CategoriesModel> categoriesModels;


    public DBFetch(Context context) {

        this.context = context;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

//        progress = ProgressDialog.show(context, "Fetching Data",
//                "Please Wait...", true);

    }


    @Override
    protected Boolean doInBackground(String... strings) {

        String uri = strings[0];

        boolean result = HttpManager.fetchCategories(context, uri);



        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        MainActivity.catStat = result;

//        progress.dismiss();
    }
}
