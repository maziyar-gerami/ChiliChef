package com.ict.chilichef.NavigationItems.Notes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ict.chilichef.Adapter.Recycler_View_Adapter;
import com.ict.chilichef.FoodActivity;
import com.ict.chilichef.Model.Data;
import com.ict.chilichef.NavigationItems.Categories.TabFoodActivity;
import com.ict.chilichef.R;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NotesCatActivity extends AppCompatActivity implements Recycler_View_Adapter.ClickListener {


    private List<Data> itemList;
    private RecyclerView recyclerview;
    private Recycler_View_Adapter mAdapter;
    Toolbar toolbar;
    ImageButton back;
    int ID = 25;
    private Cursor cursor;
    private static SQLiteDatabase mydatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_cat);

        toolbar = (Toolbar) findViewById(R.id.Toolbar_Public);
        ((TextView) findViewById(R.id.PublicTitle_toolbar)).setText(R.string.recipe_offer_notes);

        itemList = new ArrayList<>();


        back = (ImageButton) findViewById(R.id.PublicLeft_icon_id);
        back.setImageResource(R.drawable.ic_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotesCatActivity.this.finish();
            }
        });


        recyclerview = (RecyclerView) findViewById(R.id.Recycler_notesCat);
        prepareItem();
        mAdapter = new Recycler_View_Adapter(itemList, this);
        mAdapter.setClickListener(this);

        RecyclerView.LayoutManager mLayoutManger = new GridLayoutManager(getApplicationContext(), 3);
        recyclerview.setLayoutManager(mLayoutManger);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(mAdapter);
    }

    private void prepareItem() {

        itemList = new ArrayList<>();

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


    @Override

    public void itemClicked(View view, int position) {

        Cursor newCursor = mydatabase.rawQuery("SELECT * FROM categories WHERE Parent=+" +ID, null);

        if (newCursor.getCount() != 0) {

            Intent intent = new Intent(NotesCatActivity.this, TabNotesActivity.class);
            intent.putExtra("ItemID", ID);
            intent.putExtra("ItemName", itemList.get(position).getTitle());
            intent.putExtra("position", position);
            startActivity(intent);

        } else {

            Intent intent = new Intent(NotesCatActivity.this,FoodActivity.class);
            intent.putExtra("ItemID", itemList.get(position).getId_cat());
            intent.putExtra("ItemName", itemList.get(position).getTitle());
            intent.putExtra("Parent", ID);
            intent.putExtra("position", position);
            startActivity(intent);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}