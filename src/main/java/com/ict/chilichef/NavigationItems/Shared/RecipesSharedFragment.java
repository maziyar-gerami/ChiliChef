package com.ict.chilichef.NavigationItems.Shared;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.Adapter.Recycler_UserRecipes_Adapter;
import com.ict.chilichef.JsonParser.JsonParserNumbers;
import com.ict.chilichef.JsonParser.JsonParserUserRecipe;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.FoodsUserModel;
import com.ict.chilichef.Model.NumbersModel;
import com.ict.chilichef.R;
import com.ict.chilichef.RecipesActivity;
import com.ict.chilichef.Sessions.UserSessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipesSharedFragment extends Fragment {

    private RecyclerView recyclerUserRec;
    private Recycler_UserRecipes_Adapter RecAdapter;
    private List<FoodsUserModel> itemListRecUser = new ArrayList<>();

    UserSessionManager session;
    private ProgressBar progressBar;
    private RelativeLayout relProgress;
    boolean retry = false;
    ImageButton retryBTN;
    LinearLayout checkNetLayout;
    TextView noData;


    public RecipesSharedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recipes_shared, container, false);

        session = new UserSessionManager(getActivity());

        relProgress = (RelativeLayout) v.findViewById(R.id.relProgress);
        checkNetLayout = (LinearLayout) v.findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) v.findViewById(R.id.retry);
        noData= (TextView) v.findViewById(R.id.noRecipes);

        HashMap<String, String> user = session.getUserDetails();

        String id = user.get(UserSessionManager.KEY_ID_USER);

        itemListRecUser = new ArrayList<>();
        recyclerUserRec = (RecyclerView) v.findViewById(R.id.recycler_recipes_shared);

        if(HttpManager.checkNetwork(getActivity())) {

            RecUserFetch recUserFetch = new RecUserFetch(getActivity());
            recUserFetch.execute(Integer.valueOf(id));

        }else {

            TestInternet();

        }

        return v;
    }

    private void TestInternet() {

        checkNetLayout.setVisibility(View.VISIBLE);
        relProgress.setVisibility(View.GONE);

        retryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(HttpManager.checkNetwork(getActivity())) {

                    HashMap<String, String> user = session.getUserDetails();
                    String id = user.get(UserSessionManager.KEY_ID_USER);

                    RecUserFetch recUserFetch = new RecUserFetch(getActivity());
                    recUserFetch.execute(Integer.valueOf(id));

                    checkNetLayout.setVisibility(View.GONE);

                    retry = true;

                } else {

                    retry = false;

                    TestInternet();
                }
            }
        });
    }

    private class RecUserFetch extends AsyncTask<Integer, String, String> implements Recycler_UserRecipes_Adapter.ClickListener {

        Context context;

        public RecUserFetch(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            if (HttpManager.checkNetwork(getActivity())) {

                relProgress.setVisibility(View.VISIBLE);
                checkNetLayout.setVisibility(View.GONE);

            } else {

                TestInternet();
            }

        }

        @Override
        protected String doInBackground(Integer... integers) {

            try {

                String jsonText = HttpManager.getUserRecipes(integers[0]);

                return jsonText;

            } catch (Exception e) {
                return "error";

            }
        }

        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);

            if (result.length() > 6) {

                JsonParserUserRecipe jsonParserUserRecipe = new JsonParserUserRecipe();
                ArrayList<FoodsUserModel> foodsUserModel = jsonParserUserRecipe.parseJson(result);

                for (FoodsUserModel GM : foodsUserModel) {
                    itemListRecUser.add(GM);
                }

                RecAdapter = new Recycler_UserRecipes_Adapter(itemListRecUser, getActivity());
                RecAdapter.setClickListener(this);
                RecyclerView.LayoutManager ELayoutManger = new GridLayoutManager(getActivity(), 3);
                recyclerUserRec.setLayoutManager(ELayoutManger);
                recyclerUserRec.setItemAnimator(new DefaultItemAnimator());
                recyclerUserRec.setAdapter(RecAdapter);

                relProgress.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

                if(itemListRecUser.isEmpty()){

                    noData.setVisibility(View.VISIBLE);

                }

            } else if (result.equals("[]\n")) {

                Toast.makeText(getActivity(), R.string.emptyList, Toast.LENGTH_SHORT).show();

                relProgress.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            } else if (result.equals("error") || result.isEmpty()) {

                TestInternet();
            }
        }

        @Override
        public void itemSelectClicked(View view, int position) {

            Intent recipes = new Intent(getActivity(), RecipesActivity.class);
            startActivity(recipes);

        }
    }


}
