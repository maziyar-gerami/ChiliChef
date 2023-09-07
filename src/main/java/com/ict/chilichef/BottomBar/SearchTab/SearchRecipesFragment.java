package com.ict.chilichef.BottomBar.SearchTab;


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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.Adapter.Recycler_SearchRecipes_Adapter;
import com.ict.chilichef.Adapter.ViewPagerAdapter;
import com.ict.chilichef.JsonParser.JsonParserSearchRecepies;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.SearchRecipesModel;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ict.chilichef.BottomBar.SearchTab.BbSearchFragment.KEYWORD;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchRecipesFragment extends Fragment {

//    public static String KEYWORD = "";

    private RecyclerView recyclerSearchRec;
    private Recycler_SearchRecipes_Adapter RecAdapter;
    private ArrayList<SearchRecipesModel> itemListRecSearch;
    private ArrayList<SearchRecipesModel> itemListRecSearchTemp = new ArrayList<>();

    ImageButton retryBTN;
    private TextView noneRec;
    private ProgressBar progressBar;
    boolean retry = false;
    LinearLayout checkNetLayout;

    public SearchRecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search_recipes, container, false);

        itemListRecSearch = new ArrayList<>();
        recyclerSearchRec = (RecyclerView) v.findViewById(R.id.recycler_Rec_search);
        noneRec= (TextView) v.findViewById(R.id.noneRec);

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        checkNetLayout = (LinearLayout) v.findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) v.findViewById(R.id.retry);

        if (HttpManager.checkNetwork(getActivity())) {

            RecSearchFetch RS = new RecSearchFetch(getContext());
            RS.execute(KEYWORD);

        } else {

            TestInternet();
        }


        return v;
    }


    private class RecSearchFetch extends AsyncTask<String, String, String> {

        Context context;

        public RecSearchFetch(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... string) {

            String jsonText = null;

            try {

                jsonText = HttpManager.searchRecepies(string[0]);

                return jsonText;
            } catch (Exception e) {
                return "error";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            int size = itemListRecSearch.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    itemListRecSearch.remove(0);
                }

                RecAdapter.notifyItemRangeRemoved(0, size);
            }

            if (HttpManager.checkNetwork(getActivity())) {

                progressBar.setVisibility(View.VISIBLE);
                checkNetLayout.setVisibility(View.GONE);

            } else {

                TestInternet();
            }

        }


        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);

            if (result.length() > 6) {
                JsonParserSearchRecepies jsonParserSearchRecepies = new JsonParserSearchRecepies();
                ArrayList<SearchRecipesModel> searchRecipesModels = jsonParserSearchRecepies.parseJson(result);

                for (SearchRecipesModel FD : searchRecipesModels) {
                    itemListRecSearch.add(FD);
                }

                itemListRecSearchTemp = itemListRecSearch;


                RecAdapter = new Recycler_SearchRecipes_Adapter(itemListRecSearch, getActivity());
                RecyclerView.LayoutManager ELayoutManger = new GridLayoutManager(getActivity(), 2);
                recyclerSearchRec.setLayoutManager(ELayoutManger);
                recyclerSearchRec.setItemAnimator(new DefaultItemAnimator());
                recyclerSearchRec.setAdapter(RecAdapter);

                progressBar.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            } else if (result.equals("error") || result.isEmpty()) {

                TestInternet();

            }  else if (result.equals("[]\n")) {

                progressBar.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);
                noneRec.setVisibility(View.VISIBLE);
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

                    RecSearchFetch RS = new RecSearchFetch(getContext());
                    RS.execute(KEYWORD);

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
