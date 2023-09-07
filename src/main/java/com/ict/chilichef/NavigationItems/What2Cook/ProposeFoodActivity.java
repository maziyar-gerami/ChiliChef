package com.ict.chilichef.NavigationItems.What2Cook;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ict.chilichef.Adapter.Recycler_Food_Adapter;
import com.ict.chilichef.JsonParser.JsonParserFoods;
import com.ict.chilichef.Model.FoodsCategoricalModel;
import com.ict.chilichef.R;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProposeFoodActivity extends AppCompatActivity implements Recycler_Food_Adapter.ClickListener{

    private ArrayList<FoodsCategoricalModel> itemListFood = new ArrayList<>();
    private RecyclerView recyclerFood;
    private Recycler_Food_Adapter FoodAdapter;

    Toolbar toolbar;
    ImageButton back;
    String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propose_food);
        Bundle bundle = getIntent().getExtras();

        uri = bundle.getString("result");

        toolbar = (Toolbar) findViewById(R.id.Toolbar_Public);
        setSupportActionBar(toolbar);
        ((TextView)findViewById(R.id.PublicTitle_toolbar)).setText("پیشنهاد های سرآشپز چیلی");

        back = (ImageButton) findViewById(R.id.PublicLeft_icon_id);
        TextView    non = (TextView) findViewById(R.id.non);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProposeFoodActivity.this.finish();
            }
        });
        recyclerFood = (RecyclerView) findViewById(R.id.recycler_food);

        JsonParserFoods jsonParserFoods = new JsonParserFoods();
        ArrayList<FoodsCategoricalModel> foodsCategoricalModels = jsonParserFoods.parseJson(uri);

        for (FoodsCategoricalModel fdm: foodsCategoricalModels){
            itemListFood.add(fdm);
        }

        FoodAdapter = new Recycler_Food_Adapter(itemListFood, getApplicationContext());
        FoodAdapter.setClickListener(this);

        RecyclerView.LayoutManager fLayoutManger = new GridLayoutManager(getApplicationContext(), 2);
        recyclerFood.setLayoutManager(fLayoutManger);
        recyclerFood.setItemAnimator(new DefaultItemAnimator());
        recyclerFood.setAdapter(FoodAdapter);

        if(itemListFood.size()==0){
            non.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void itemClicked(View view, int position) {

    }

}



