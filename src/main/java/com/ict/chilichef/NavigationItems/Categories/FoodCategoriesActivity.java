package com.ict.chilichef.NavigationItems.Categories;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ict.chilichef.Adapter.Recycler_View_Adapter;
import com.ict.chilichef.FoodActivity;
import com.ict.chilichef.Model.Data;
import com.ict.chilichef.R;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FoodCategoriesActivity extends AppCompatActivity implements Recycler_View_Adapter.ClickListener {

    private ArrayList<Data> itemList;
    private RecyclerView recyclerview;
    private Recycler_View_Adapter mAdapter;
    private Toolbar toolbar, catToolbar;
    private ImageView back;
    private int parentID;
    private String method;
    TextView tabOne, tabTwo,tabThree;
    private Cursor cursor;
    private static SQLiteDatabase mydatabase;

    LinearLayout FoodType, valueBase, events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_categories);
        itemList = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.Toolbar_Public);
        catToolbar = (Toolbar) findViewById(R.id.Toolbar_Categories);
        setSupportActionBar(toolbar);
        setSupportActionBar(catToolbar);

        tabOne = (TextView) findViewById(R.id.TV_typeFoodType);
        tabTwo = (TextView) findViewById(R.id.TV_typeFood);
        tabThree = (TextView) findViewById(R.id.TV_events);

        back = (ImageView) findViewById(R.id.food_categories_close);
        back.setImageResource(R.drawable.ic_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FoodCategoriesActivity.this.finish();
            }
        });

        FoodType = (LinearLayout) findViewById(R.id.LinFoodType);
        valueBase = (LinearLayout) findViewById(R.id.LinValueBase);
        events = (LinearLayout) findViewById(R.id.LinEvents);

        recyclerview = (RecyclerView) findViewById(R.id.recycler_view);
        method = "LEGACY";
        prepareItem(1);

        TabOne();


        fill();

        FoodType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                itemList.clear();

                int ID = 1;
                prepareItem(ID);
                fill();
                method = "LEGACY";

                TabOne();

            }
        });

        valueBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                itemList.clear();

                int ID = 2;
                prepareItem(ID);
                fill();
                method = "VALUE";

                tabOne.setTextColor(ContextCompat.getColor(FoodCategoriesActivity.this, (R.color.colorAccent)));
                setTextViewDrawableColor(tabOne, ContextCompat.getColor(FoodCategoriesActivity.this, (R.color.colorAccent)));

                tabTwo.setTextColor(ContextCompat.getColor(FoodCategoriesActivity.this, (R.color.colorPrimary)));
                setTextViewDrawableColor(tabTwo, ContextCompat.getColor(FoodCategoriesActivity.this, (R.color.colorPrimary)));

                tabThree.setTextColor(ContextCompat.getColor(FoodCategoriesActivity.this, (R.color.colorAccent)));
                setTextViewDrawableColor(tabThree, ContextCompat.getColor(FoodCategoriesActivity.this, (R.color.colorAccent)));

            }
        });

        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                itemList.clear();

                int ID = 105;
                prepareItem(ID);
                fill();
                method = "EVENT";

                tabOne.setTextColor(ContextCompat.getColor(FoodCategoriesActivity.this, (R.color.colorAccent)));
                setTextViewDrawableColor(tabOne, ContextCompat.getColor(FoodCategoriesActivity.this, (R.color.colorAccent)));

                tabTwo.setTextColor(ContextCompat.getColor(FoodCategoriesActivity.this, (R.color.colorAccent)));
                setTextViewDrawableColor(tabTwo, ContextCompat.getColor(FoodCategoriesActivity.this, (R.color.colorAccent)));

                tabThree.setTextColor(ContextCompat.getColor(FoodCategoriesActivity.this, (R.color.colorPrimary)));
                setTextViewDrawableColor(tabThree, ContextCompat.getColor(FoodCategoriesActivity.this, (R.color.colorPrimary)));
            }
        });

    }

    private void prepareItem(int ID) {

        itemList = new ArrayList<>();

        parentID = ID;

        mydatabase = getApplicationContext().openOrCreateDatabase(getString(R.string.databasePath), MODE_PRIVATE, null);

        cursor = mydatabase.rawQuery("SELECT * FROM categories WHERE Parent=+" + ID, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String Title = cursor.getString(cursor.getColumnIndex("Name"));
            int id_cat = cursor.getInt(cursor.getColumnIndex("Id_Cat"));
            int resId = getResources().getIdentifier("id_cat" + id_cat, "drawable", getPackageName());
            Data item = new Data(resId, Title, id_cat);
            itemList.add(item);
            cursor.moveToNext();

        }

        cursor.close();

        //mAdapter.notifyDataSetChanged();
    }

    private void fill() {

        mAdapter = new Recycler_View_Adapter(itemList, this);
        mAdapter.setClickListener(this);

        RecyclerView.LayoutManager mLayoutManger = new GridLayoutManager(getApplicationContext(), 2);
        recyclerview.setLayoutManager(mLayoutManger);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(mAdapter);
    }

    @Override

    public void itemClicked(View view, int position) {

        Cursor newCursor = mydatabase.rawQuery("SELECT * FROM categories WHERE Parent=+" + itemList.get(position).getId_cat(), null);

        if (newCursor.getCount() != 0) {

            Intent intent = new Intent(FoodCategoriesActivity.this, TabFoodActivity.class);
            intent.putExtra("ItemID", itemList.get(position).getId_cat());
            intent.putExtra("ItemName", itemList.get(position).getTitle());
//            intent.putExtra("position", position);
            startActivity(intent);

        } else {

            Intent intent = new Intent(FoodCategoriesActivity.this, FoodActivity.class);
            intent.putExtra("ItemID", itemList.get(position).getId_cat());
            intent.putExtra("ItemName", itemList.get(position).getTitle());
            intent.putExtra("Method", method);
            intent.putExtra("Parent", parentID);
            intent.putExtra("position", position);
            startActivity(intent);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {

        FoodCategoriesActivity.this.finish();

    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
            }
        }
    }

    private void TabOne(){

        tabOne.setTextColor(ContextCompat.getColor(FoodCategoriesActivity.this, (R.color.colorPrimary)));
        setTextViewDrawableColor(tabOne, ContextCompat.getColor(FoodCategoriesActivity.this, (R.color.colorPrimary)));

        tabTwo.setTextColor(ContextCompat.getColor(FoodCategoriesActivity.this, (R.color.colorAccent)));
        setTextViewDrawableColor(tabTwo, ContextCompat.getColor(FoodCategoriesActivity.this, (R.color.colorAccent)));

        tabThree.setTextColor(ContextCompat.getColor(FoodCategoriesActivity.this, (R.color.colorAccent)));
        setTextViewDrawableColor(tabThree, ContextCompat.getColor(FoodCategoriesActivity.this, (R.color.colorAccent)));
    }
}
