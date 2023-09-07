package com.ict.chilichef;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ict.chilichef.Adapter.Recycler_Food_Adapter;
import com.ict.chilichef.JsonParser.JsonParserFoods;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.FoodsCategoricalModel;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FoodActivity extends AppCompatActivity implements Recycler_Food_Adapter.ClickListener {

    private ArrayList<FoodsCategoricalModel> itemListFood = new ArrayList<>();
    private RecyclerView recyclerFood;

    Toolbar toolbar;
    ImageButton back, retryBTN;
    String TITLE, Method;
    int ID;

    private ProgressBar progressBar;
    boolean retry = false;
    LinearLayout checkNetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        Bundle bundle = getIntent().getExtras();

        TITLE = bundle.getString("ItemName");
        ID = bundle.getInt("ItemID");
        Method = bundle.getString("Method");

        toolbar = (Toolbar) findViewById(R.id.Toolbar_Public);
        setSupportActionBar(toolbar);
        ((TextView) findViewById(R.id.PublicTitle_toolbar)).setText(TITLE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        checkNetLayout = (LinearLayout) findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) findViewById(R.id.retry);

        back = (ImageButton) findViewById(R.id.PublicLeft_icon_id);
        back.setImageResource(R.drawable.ic_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FoodActivity.this.finish();
            }
        });

        recyclerFood = (RecyclerView) findViewById(R.id.recycler_food);

        if (HttpManager.checkNetwork(FoodActivity.this)) {

            FoodsFetch ff = new FoodsFetch(FoodActivity.this);
            ff.execute(String.valueOf(ID), Method);

        } else {

            TestInternet();
        }

    }

    private void TestInternet() {

        checkNetLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        retryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (HttpManager.checkNetwork(FoodActivity.this)) {

                    FoodsFetch ff = new FoodsFetch(FoodActivity.this);
                    ff.execute(String.valueOf(ID), Method);

                    checkNetLayout.setVisibility(View.GONE);
                    retry = true;

                } else {

                    retry = false;

                    TestInternet();
                }
            }
        });
    }


    @Override
    public void itemClicked(View view, int position) {

    }


    private class FoodsFetch extends AsyncTask<String, String, String> implements Recycler_Food_Adapter.ClickListener {

        Context context;

        public FoodsFetch(Context context) {

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (HttpManager.checkNetwork(FoodActivity.this)) {

                progressBar.setVisibility(View.VISIBLE);
                checkNetLayout.setVisibility(View.GONE);

            } else {

                checkNetLayout.setVisibility(View.VISIBLE);
                TestInternet();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                String jsonText = HttpManager.getFoodsCategory(Integer.parseInt(strings[0]), strings[1]);

                return jsonText;

            } catch (Exception e) {

                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            if (result.length() > 6) {

                JsonParserFoods jsonParserFoods = new JsonParserFoods();
                ArrayList<FoodsCategoricalModel> foodsCategoricalModels = jsonParserFoods.parseJson(result);

                for (FoodsCategoricalModel fdm : foodsCategoricalModels) {
                    itemListFood.add(fdm);
                }

                Recycler_Food_Adapter foodAdapter = new Recycler_Food_Adapter(itemListFood, getApplicationContext());
                foodAdapter.setClickListener(this);

                RecyclerView.LayoutManager fLayoutManger = new GridLayoutManager(getApplicationContext(), 2);
                recyclerFood.setLayoutManager(fLayoutManger);
                recyclerFood.setItemAnimator(new DefaultItemAnimator());
                recyclerFood.setAdapter(foodAdapter);

                progressBar.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            } else if (result.equals("error") || result.isEmpty()) {

                TestInternet();
            }
        }

        @Override
        public void itemClicked(View view, int position) {

        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }


    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
