package com.ict.chilichef.NavigationItems.Categories;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ict.chilichef.Adapter.Recycler_Food_Adapter;
import com.ict.chilichef.JsonParser.JsonParserFoods;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.FoodsCategoricalModel;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.ict.chilichef.UsersManage.UserFollowerActivity;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment implements Recycler_Food_Adapter.ClickListener {

    int ID;
    private RecyclerView recyclerFood;
    private ArrayList<FoodsCategoricalModel> itemListFood;
    private Recycler_Food_Adapter foodAdapter;

    ImageButton retryBTN;
    private ProgressBar progressBar;
    boolean retry = false;
    LinearLayout checkNetLayout;
    private Bundle bundle;

    public CategoriesFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_categories, container, false);

        bundle = getActivity().getIntent().getExtras();

        int ID_Cat = getArguments().getInt("Id_Cat", 0);
        ID = bundle.getInt("ItemID");

        itemListFood = new ArrayList<>();

        recyclerFood = (RecyclerView) v.findViewById(R.id.recycler_food_tab);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        checkNetLayout = (LinearLayout) v.findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) v.findViewById(R.id.retry);

        if (HttpManager.checkNetwork(getActivity())) {

            FoodsFetch ff = new FoodsFetch(getActivity());
            ff.execute(ID_Cat);

        } else {

            TestInternet();
        }

        return v;
    }

    @Override
    public void itemClicked(View view, int position) {

    }

    private class FoodsFetch extends AsyncTask<Integer, String, String> implements Recycler_Food_Adapter.ClickListener {

        Context context;

        private FoodsFetch(Context context) {

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (HttpManager.checkNetwork(getActivity())) {

                progressBar.setVisibility(View.VISIBLE);
                checkNetLayout.setVisibility(View.GONE);

                int size = itemListFood.size();
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        itemListFood.remove(0);
                    }

                    foodAdapter.notifyItemRangeRemoved(0, size);
                }
            } else {
                TestInternet();
            }
        }

        @Override
        protected String doInBackground(Integer... integers) {

            try {
                String jsonText = HttpManager.getFoodsDataCategoryLegacy(integers[0]);

                return jsonText;

            } catch (Exception e) {

                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.length() > 6) {

                JsonParserFoods jsonParserFoods = new JsonParserFoods();
                ArrayList<FoodsCategoricalModel> foodsCategoricalModels = jsonParserFoods.parseJson(result);

                for (FoodsCategoricalModel fdm : foodsCategoricalModels) {
                    itemListFood.add(fdm);
                }

                foodAdapter = new Recycler_Food_Adapter(itemListFood, getActivity());
                foodAdapter.setClickListener(this);

                RecyclerView.LayoutManager fLayoutManger = new GridLayoutManager(getActivity(), 2);
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

    private void TestInternet() {

        checkNetLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        retryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (HttpManager.checkNetwork(getActivity())) {

                    bundle = getActivity().getIntent().getExtras();

                    int ID_Cat = getArguments().getInt("Id_Cat", 0);

                    FoodsFetch ff = new FoodsFetch(getActivity());
                    ff.execute(ID_Cat);

                    checkNetLayout.setVisibility(View.GONE);

                    retry = true;

                } else {

                    retry = false;

                    TestInternet();
                }
            }
        });
    }


}
