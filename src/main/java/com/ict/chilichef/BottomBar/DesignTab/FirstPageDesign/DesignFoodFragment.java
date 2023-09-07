package com.ict.chilichef.BottomBar.DesignTab.FirstPageDesign;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ict.chilichef.Adapter.Recycler_Design_Adapter;
import com.ict.chilichef.BottomBar.DesignTab.TabDesignActivity;
import com.ict.chilichef.Model.Data;
import com.ict.chilichef.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class DesignFoodFragment extends Fragment implements Recycler_Design_Adapter.ClickListener{

    private List<Data> itemList;
    private RecyclerView recyclerview;
    private Recycler_Design_Adapter mAdapter;
    int ID = 126;
    private Cursor cursor;
    private static SQLiteDatabase mydatabase;


    public DesignFoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_design_food, container, false);


        itemList = new ArrayList<>();


        recyclerview = (RecyclerView) v.findViewById(R.id.recycler_food_Design);
        prepareItem();
        mAdapter = new Recycler_Design_Adapter(itemList, getActivity());
        mAdapter.setClickListener(this);

        RecyclerView.LayoutManager mLayoutManger = new GridLayoutManager(getContext(), 1);
        recyclerview.setLayoutManager(mLayoutManger);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(mAdapter);

        return v;
    }


    private void prepareItem() {

        itemList = new ArrayList<>();

        mydatabase = getContext().openOrCreateDatabase(getString(R.string.databasePath), MODE_PRIVATE, null);

        cursor = mydatabase.rawQuery("SELECT * FROM categories WHERE Parent=+" + ID, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            String Title = cursor.getString(cursor.getColumnIndex("Name"));
            int id_cat = cursor.getInt(cursor.getColumnIndex("Id_Cat"));
            String des = cursor.getString(cursor.getColumnIndex("Description"));
            int resId = getResources().getIdentifier("id_cat" + id_cat, "drawable", getActivity().getPackageName());
            Data item = new Data(Title, resId, id_cat, des);
            itemList.add(item);
            cursor.moveToNext();
        }

        cursor.close();
        //mAdapter.notifyDataSetChanged();
    }

    @Override
    public void itemClicked(View view, int position) {
        Cursor newCursor = mydatabase.rawQuery("SELECT * FROM categories WHERE Parent=+" + ID, null);

        if (newCursor.getCount() != 0) {

            Intent intent = new Intent(getContext(), TabDesignActivity.class);
            intent.putExtra("ItemID", ID);
            intent.putExtra("ItemName", itemList.get(position).getTitle());
            intent.putExtra("position", position);
            startActivity(intent);
        }
    }
}


