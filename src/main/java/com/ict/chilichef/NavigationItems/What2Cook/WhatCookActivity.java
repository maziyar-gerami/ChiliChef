package com.ict.chilichef.NavigationItems.What2Cook;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.Adapter.Recycler_ProposeAddIng_Adapter;
import com.ict.chilichef.Adapter.Recycler_ProposeSearchIng_Adapter;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.AddIngredientsModel;
import com.ict.chilichef.Model.SearchIngredientsModel;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class WhatCookActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button foodListBtn, AddIngBtn, dialog_AddIng;
    ImageButton AddIng, dialogCloseIcon, back;
    LinearLayout Layout_showFood;

    EditText Ingredient_Name;
    UserSessionManager session;
    TextView IngText;
    String Ingredient;

    List<Integer> ListIng = new LinkedList<>();


    String path = "/data/data/com.ict.chilichef/databases/foodapp.db";
    private static SQLiteDatabase mydatabase;


    public static final String UPLOAD_URL = "http://ashpazchili.ir/Chili/Pages/whatToCook.php";

    private ArrayList<SearchIngredientsModel> itemListSearchIng = new ArrayList<>();
    private RecyclerView SearchIng_RecyclerView;
    private Recycler_ProposeSearchIng_Adapter Search_IngAdapter;


    private ArrayList<AddIngredientsModel> itemListAddIng = new ArrayList<>();
    private RecyclerView AddIng_RecyclerView;
    private Recycler_ProposeAddIng_Adapter Add_IngAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_cook);

        toolbar = (Toolbar) findViewById(R.id.Toolbar_Public);
        setSupportActionBar(toolbar);
        ((TextView) findViewById(R.id.PublicTitle_toolbar)).setText(R.string.drawer_offer);

        back = (ImageButton) findViewById(R.id.PublicLeft_icon_id);
        back.setImageResource(R.drawable.ic_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhatCookActivity.this.finish();
            }
        });

        session = new UserSessionManager(WhatCookActivity.this);

        AddIng = (ImageButton) findViewById(R.id.whatCook_addIng_roundBtn);
        AddIngBtn = (Button) findViewById(R.id.whatCook_addIng_firstBtn);
        IngText = (TextView) findViewById(R.id.add_ingredient_Text);
        Layout_showFood = (LinearLayout) findViewById(R.id.Layout_showFood);

        if (itemListAddIng.size() != 0) {
            IngText.setText("");

        }

        AddIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAddIngDialog();
            }
        });

        AddIngBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddIngDialog();
            }
        });


        foodListBtn = (Button) findViewById(R.id.whatCook_seeFoodList_Btn);

        if (itemListAddIng.size() == 0) {

            Layout_showFood.setVisibility(View.INVISIBLE);
            AddIngBtn.setVisibility(View.VISIBLE);


        } else if (itemListAddIng.size() > 0) {
            Layout_showFood.setVisibility(View.VISIBLE);
            AddIngBtn.setVisibility(View.INVISIBLE);

        }


        foodListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemListAddIng.size() > 8) {

                    Toast.makeText(WhatCookActivity.this, "شما مجاز به انتخاب حداکثر هشت ماده غذایی هستید", Toast.LENGTH_SHORT).show();
                } else if (itemListAddIng.size() <= 8) {

                    if (itemListAddIng.size() < 4) {
                        Toast.makeText(WhatCookActivity.this, "شما مجاز به انتخاب حداقل چهار ماده غذایی هستید", Toast.LENGTH_SHORT).show();

                    } else if (itemListAddIng.size() >= 4) {


                        for (int y = 0; y < 8; y++) {

                            if (y < itemListAddIng.size())

                                ListIng.add(itemListAddIng.get(y).getID());

                            else

                                ListIng.add(0);
                        }

                        if (HttpManager.checkNetwork(WhatCookActivity.this)) {

                            IngredientSend IS = new IngredientSend(WhatCookActivity.this);
                            IS.execute();

                        } else {

                            Toast.makeText(WhatCookActivity.this, R.string.Disconnect, Toast.LENGTH_SHORT).show();
                        }

                    }

                }


            }
        });
    }


    class IngredientSend extends AsyncTask<Void, Void, String> {

        private ProgressDialog loading;
        private Context context;

        public IngredientSend(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(context, "لطفا صبر کنید", "در حال جستجو ...", true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();

            WhatCookActivity.this.finish();

            Intent i = new Intent(WhatCookActivity.this, ProposeFoodActivity.class);
            i.putExtra("result", s);
            startActivity(i);

        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, String> user = session.getUserDetails();
            String IdUser = user.get(UserSessionManager.KEY_ID_USER);

            HashMap<String, Integer> data = new HashMap<>();
            data.put("I_1", ListIng.get(0));
            data.put("I_2", ListIng.get(1));
            data.put("I_3", ListIng.get(2));
            data.put("I_4", ListIng.get(3));
            data.put("I_5", ListIng.get(4));
            data.put("I_6", ListIng.get(5));
            data.put("I_7", ListIng.get(6));
            data.put("I_8", ListIng.get(7));
            data.put("Id_User", Integer.parseInt(IdUser));


            String result = null;
            try {
                result = HttpManager.whatToCook(data);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;


        }
    }

    private void openAddIngDialog() {

        final Dialog AddIng_dialog = new Dialog(WhatCookActivity.this, R.style.Dialog);
        AddIng_dialog.setContentView(R.layout.dialog_whatcook_addingredient);
        AddIng_dialog.show();

        Ingredient_Name = (EditText) AddIng_dialog.findViewById(R.id.dialog_ETX_search_IngName);

        dialogCloseIcon = (ImageButton) AddIng_dialog.findViewById(R.id.dialog_closeIcon);
        dialogCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AddIng_dialog.dismiss();

            }
        });


        ImageButton Search_Ing = (ImageButton) AddIng_dialog.findViewById(R.id.dialog_search_IngName_ImgBtn);

        Search_Ing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Ingredient_Name.getText().length() > 1) {

                    SearchIng_RecyclerView = (RecyclerView) AddIng_dialog.findViewById(R.id.Propose_Ingredient_Recycler);
                    dialog_AddIng = (Button) AddIng_dialog.findViewById(R.id.Dialog_Add_Ing);


                    int size = itemListSearchIng.size();
                    if (size > 0) {
                        for (int i = 0; i < size; i++) {
                            itemListSearchIng.remove(0);
                        }

                        Search_IngAdapter.notifyItemRangeRemoved(0, size);
                    }


                    mydatabase = getApplicationContext().openOrCreateDatabase(path, MODE_PRIVATE, null);


                    Cursor cursor = mydatabase.rawQuery("SELECT * FROM ingredients  WHERE Name LIKE  '%" + Ingredient_Name.getText().toString() + "%' AND Status=1", null);

                    cursor.moveToFirst();

                    while (cursor.isAfterLast() == false) {
                        String Name = cursor.getString(cursor.getColumnIndex("Name"));
                        int id = cursor.getInt(cursor.getColumnIndex("Id_Ing"));
                        itemListSearchIng.add(new SearchIngredientsModel(id, Name));
                        cursor.moveToNext();
                    }
                    cursor.close();


                    Search_IngAdapter = new Recycler_ProposeSearchIng_Adapter(itemListSearchIng, getApplicationContext());
                    Search_IngAdapter.setClickListener(this);

                    RecyclerView.LayoutManager fLayoutManger = new GridLayoutManager(getApplicationContext(), 1);
                    SearchIng_RecyclerView.setLayoutManager(fLayoutManger);
                    SearchIng_RecyclerView.setItemAnimator(new DefaultItemAnimator());
                    SearchIng_RecyclerView.setAdapter(Search_IngAdapter);

                    if (itemListSearchIng.size() != 0) {
                        dialog_AddIng.setVisibility(View.VISIBLE);
                    }


                    dialog_AddIng.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AddIng_RecyclerView = (RecyclerView) findViewById(R.id.Final_Propose_Ingredient_Recycler);


                            for (int i = 0; i < Search_IngAdapter.Checked_itemList.size(); i++) {

                                int ing_id = Search_IngAdapter.Checked_itemList.get(i).getID();
                                String ing_name = Search_IngAdapter.Checked_itemList.get(i).getName();

                                AddIngredientsModel item = new AddIngredientsModel(ing_name, ing_id);
                                itemListAddIng.add(item);
                            }

                            Add_IngAdapter = new Recycler_ProposeAddIng_Adapter(itemListAddIng, getApplicationContext());
                            RecyclerView.LayoutManager mLayoutManger = new GridLayoutManager(getApplicationContext(), 1);
                            AddIng_RecyclerView.setLayoutManager(mLayoutManger);
                            AddIng_RecyclerView.setItemAnimator(new DefaultItemAnimator());
                            AddIng_RecyclerView.setAdapter(Add_IngAdapter);

                            if (itemListAddIng.size() == 0) {

                                Layout_showFood.setVisibility(View.INVISIBLE);
                                AddIngBtn.setVisibility(View.VISIBLE);


                            } else if (itemListAddIng.size() > 0) {
                                Layout_showFood.setVisibility(View.VISIBLE);
                                AddIngBtn.setVisibility(View.INVISIBLE);

                            }

                            AddIng_dialog.dismiss();

                        }
                    });


                } else {
                    Toast.makeText(WhatCookActivity.this, "لطفا حداقل یک ماده اضافه کنید", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }
}
