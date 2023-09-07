package com.ict.chilichef.NavigationItems.Shared;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.ict.chilichef.Adapter.Recycler_Following_Adapter;
import com.ict.chilichef.JsonParser.JsonParserFriends;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.FriendsModel;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FollowingActivity extends AppCompatActivity {
    private RecyclerView recyclerFollow;
    private Recycler_Following_Adapter FAdapter;
    private List<FriendsModel> itemListFriends = new ArrayList<>();
    UserSessionManager session;
    Toolbar toolbar;
    ImageButton back, retryBTN;
    private ProgressBar progressBar;
    private boolean retry = false;
    LinearLayout checkNetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);


        session = new UserSessionManager(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
        String UserID = user.get(UserSessionManager.KEY_ID_USER);

        recyclerFollow = (RecyclerView) findViewById(R.id.recycler_follow);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        checkNetLayout = (LinearLayout) findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) findViewById(R.id.retry);

        toolbar = (Toolbar) findViewById(R.id.Toolbar_Public);
        setSupportActionBar(toolbar);
        ((TextView) findViewById(R.id.PublicTitle_toolbar)).setText("دنبال شونده ها");

        back = (ImageButton) findViewById(R.id.PublicLeft_icon_id);
        back.setImageResource(R.drawable.ic_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowingActivity.this.finish();
            }
        });

        if (HttpManager.checkNetwork(FollowingActivity.this)) {

            FriendsFetch ff = new FriendsFetch(FollowingActivity.this);
            ff.execute(Integer.valueOf(UserID));

        } else {

            TestInternet();
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private class FriendsFetch extends AsyncTask<Integer, String, String> implements Recycler_Following_Adapter.ClickListener {

        Context context;


        public FriendsFetch(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (HttpManager.checkNetwork(FollowingActivity.this)) {
//
                progressBar.setVisibility(View.VISIBLE);
                checkNetLayout.setVisibility(View.GONE);

            } else {

                TestInternet();
            }

        }

        @Override
        protected void onPostExecute(String s) {
//            super.onPostExecute(s);

            if (s.length() > 6) {
                ArrayList<FriendsModel> friendsModels = new JsonParserFriends().parseJson(s);

                FAdapter = new Recycler_Following_Adapter(itemListFriends, getApplicationContext());
                FAdapter.setClickListener(this);
                RecyclerView.LayoutManager FLayoutManger = new GridLayoutManager(getApplicationContext(), 1);
                recyclerFollow.setLayoutManager(FLayoutManger);
                recyclerFollow.setItemAnimator(new DefaultItemAnimator());
                recyclerFollow.setAdapter(FAdapter);
//
                progressBar.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            } else if (s.equals("[]\n")) {

                Toast.makeText(FollowingActivity.this, R.string.emptyList, Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            } else if (s.equals("error") || s.isEmpty()) {

                TestInternet();
            }
        }

        @Override
        protected String doInBackground(Integer... integers) {


            try {
                itemListFriends = new ArrayList<>();
                String jsonText = HttpManager.getFollowings(integers[0]);

                JsonParserFriends jsonParserFriends = new JsonParserFriends();
                ArrayList<FriendsModel> friendsModels = jsonParserFriends.parseJson(jsonText);

                for (FriendsModel fdm : friendsModels) {
                    itemListFriends.add(fdm);
                }

                return jsonText;
            } catch (Exception e) {
                return "error";
            }
        }

        @Override
        public void itemSelectClicked(View view, int position) {

        }
    }

    private void TestInternet() {

        checkNetLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        checkNetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (HttpManager.checkNetwork(FollowingActivity.this)) {

                    HashMap<String, String> user = session.getUserDetails();
                    String UserID = user.get(UserSessionManager.KEY_ID_USER);


                    FriendsFetch ff = new FriendsFetch(FollowingActivity.this);
                    ff.execute(Integer.valueOf(UserID));


                    checkNetLayout.setVisibility(View.GONE);

                    retry = true;

                } else {

                    retry = false;

                    TestInternet();
                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        HashMap<String, String> user = session.getUserDetails();
        String UserID = user.get(UserSessionManager.KEY_ID_USER);

        if (HttpManager.checkNetwork(FollowingActivity.this)) {

            FriendsFetch friendsFetch = new FriendsFetch(this);
            friendsFetch.execute(Integer.valueOf(UserID));

        } else {

            TestInternet();
        }
    }

}
