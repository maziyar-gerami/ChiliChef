package com.ict.chilichef.BottomBar.SearchTab;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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

import com.ict.chilichef.Adapter.Recycler_SearchUser_Adapter;
import com.ict.chilichef.Adapter.ViewPagerAdapter;
import com.ict.chilichef.JsonParser.JsonParserSearchUser;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.SearchUsersModel;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.ict.chilichef.BottomBar.SearchTab.BbSearchFragment.KEYWORD;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchUsersFragment extends Fragment {


    //    public static String KEYWORD = "";
    private UserSessionManager session;

    private RecyclerView recyclerUser;
    private Recycler_SearchUser_Adapter FAdapter;
    private List<SearchUsersModel> itemListUser = new ArrayList<>();
    private TextView noneUsers;
    ImageButton retryBTN;
    private ProgressBar progressBar;
    boolean retry = false;
    LinearLayout checkNetLayout;

    public SearchUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search_users, container, false);


        recyclerUser = (RecyclerView) v.findViewById(R.id.recycler_SearchUser);

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        checkNetLayout = (LinearLayout) v.findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) v.findViewById(R.id.retry);
        noneUsers = (TextView) v.findViewById(R.id.noneUsers);
        session = new UserSessionManager(getContext());
        HashMap<String, String> user = session.getUserDetails();
        String userID = user.get(UserSessionManager.KEY_ID_USER);
        if (userID == null) userID = "0";

        if (HttpManager.checkNetwork(getActivity())) {

            UserSearchFetch us = new UserSearchFetch(getContext());
            us.execute(KEYWORD, userID);

        } else {

            TestInternet();
        }
        return v;


    }

    public class UserSearchFetch extends AsyncTask<String, String, String> implements Recycler_SearchUser_Adapter.ClickListener {

        Context context;

        public UserSearchFetch(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... string) {

            try {
                String jsonText = null;

                jsonText = HttpManager.searchUsers(string[0], Integer.parseInt(string[1]));

                return jsonText;
            } catch (Exception e) {

                return "error";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            int size = itemListUser.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    itemListUser.remove(0);
                }

                FAdapter.notifyItemRangeRemoved(0, size);
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

            if (result.length() > 6) {

                itemListUser = new ArrayList<>();
                JsonParserSearchUser jsonParserSearchUser = new JsonParserSearchUser();
                ArrayList<SearchUsersModel> searchUsersModels = jsonParserSearchUser.parseJson(result);

                for (SearchUsersModel FD : searchUsersModels) {
                    itemListUser.add(FD);
                }
                Collections.reverse(itemListUser);
                FAdapter = new Recycler_SearchUser_Adapter(itemListUser, getActivity());
                RecyclerView.LayoutManager FLayoutManger = new GridLayoutManager(getActivity(), 1);
                recyclerUser.setLayoutManager(FLayoutManger);
                recyclerUser.setItemAnimator(new DefaultItemAnimator());
                recyclerUser.setAdapter(FAdapter);

                progressBar.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            } else if (result.equals("error") || result.isEmpty()) {

                TestInternet();

            } else if (result.equals("[]\n")) {

                progressBar.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);
                noneUsers.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public void itemSelectClicked(View view, int position) {

        }
    }

    private void TestInternet() {

        checkNetLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        retryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (HttpManager.checkNetwork(getActivity())) {

                    HashMap<String, String> user = session.getUserDetails();
                    String userID = user.get(UserSessionManager.KEY_ID_USER);

                    if (userID == null) userID = "0";

                    UserSearchFetch us = new UserSearchFetch(getContext());
                    us.execute(KEYWORD, userID);

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
