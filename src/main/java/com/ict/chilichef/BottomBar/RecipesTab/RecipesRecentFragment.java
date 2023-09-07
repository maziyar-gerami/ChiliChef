package com.ict.chilichef.BottomBar.RecipesTab;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ict.chilichef.Adapter.Recycler_SearchRecipes_Adapter;
import com.ict.chilichef.Adapter.Recycler_SearchUser_Adapter;
import com.ict.chilichef.Adapter.ViewPagerAdapter;
import com.ict.chilichef.JsonParser.JsonParserSearchRecepies;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.SearchRecipesModel;
import com.ict.chilichef.Model.SearchUsersModel;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipesRecentFragment extends Fragment {


    private UserSessionManager session;

    public static final String Recent_recMethod = "NEWRECIPES";
    public static final String Recent_recCount = "10";

    private RecyclerView recyclerRecentRec;
    private Recycler_SearchRecipes_Adapter RecentRecAdapter;

    private ArrayList<SearchRecipesModel> itemListRecentRec;
    ImageButton retryBTN;
    private ProgressBar progressBar;
    boolean retry = false;
    LinearLayout checkNetLayout;

    public RecipesRecentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_recipes_recent, container, false);

        itemListRecentRec = new ArrayList<>();
        session = new UserSessionManager(getContext());

        recyclerRecentRec = (RecyclerView) v.findViewById(R.id.recyclerRecentRec);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        checkNetLayout = (LinearLayout) v.findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) v.findViewById(R.id.retry);

        if (HttpManager.checkNetwork(getActivity())) {

            RecentRec fetchRecentRec = new RecentRec(getContext());
            fetchRecentRec.execute(Recent_recMethod, Recent_recCount);

        } else {

            TestInternet();
        }

        return v;
    }

    private class RecentRec extends AsyncTask<String, String, String> {

        Context context;

        public RecentRec(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... string) {


            String jsonText = null;
            try {

                jsonText = HttpManager.FirstPageFoodsQueries(string[0], Integer.parseInt(string[1]));
                return jsonText;

            } catch (Exception e) {
                return "error";
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (HttpManager.checkNetwork(getActivity())) {

                progressBar.setVisibility(View.VISIBLE);
                checkNetLayout.setVisibility(View.GONE);

            } else {

                TestInternet();
            }

        }


        @Override
        protected void onPostExecute(String result) {

            if (result.length() > 6) {

                JsonParserSearchRecepies jsonParserSearchRecepies = new JsonParserSearchRecepies();
                ArrayList<SearchRecipesModel> searchRecipesModels = jsonParserSearchRecepies.parseJson(result);

                for (SearchRecipesModel FD : searchRecipesModels) {
                    itemListRecentRec.add(FD);
                }

                RecentRecAdapter = new Recycler_SearchRecipes_Adapter(itemListRecentRec, getActivity());
                RecyclerView.LayoutManager ELayoutManger = new GridLayoutManager(getActivity(), 2);
                recyclerRecentRec.setLayoutManager(ELayoutManger);
                recyclerRecentRec.setItemAnimator(new DefaultItemAnimator());
                recyclerRecentRec.setAdapter(RecentRecAdapter);

                progressBar.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            } else if (result.equals("error") || result.isEmpty()) {

                TestInternet();
            }
        }
    }

    private void TestInternet() {

        checkNetLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        retryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (HttpManager.checkNetwork(getActivity())) {

                    RecentRec fetchRecentRec = new RecentRec(getContext());
                    fetchRecentRec.execute(Recent_recMethod, Recent_recCount);

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
