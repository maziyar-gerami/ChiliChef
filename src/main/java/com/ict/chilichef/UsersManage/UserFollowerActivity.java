package com.ict.chilichef.UsersManage;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.ict.chilichef.Adapter.Recycler_Follower_Adapter;
import com.ict.chilichef.JsonParser.JsonParserFriends;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.FriendsModel;
import com.ict.chilichef.NavigationItems.Shared.FollowerActivity;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UserFollowerActivity extends AppCompatActivity {

    String NameUser;
    int ID, UserID2;
    private RecyclerView recyclerFollow;
    private Recycler_Follower_Adapter FAdapter;
    private List<FriendsModel> itemListFriends = new ArrayList<>();
    private Bundle bundle;
    private Toolbar toolbar;
    private UserSessionManager session;
    ImageButton back, retryBTN;

    private ProgressBar progressBar;
    boolean retry = false;
    LinearLayout checkNetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);

        session = new UserSessionManager(getApplicationContext());

        bundle = getIntent().getExtras();
        UserID2 = bundle.getInt("IdUSER");

        toolbar = (Toolbar) findViewById(R.id.Toolbar_Public);
        setSupportActionBar(toolbar);
        ((TextView) findViewById(R.id.PublicTitle_toolbar)).setText(R.string.followers);

        back = (ImageButton) findViewById(R.id.PublicLeft_icon_id);
        back.setImageResource(R.drawable.ic_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFollowerActivity.this.finish();
            }
        });

        recyclerFollow = (RecyclerView) findViewById(R.id.recycler_follow);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        checkNetLayout = (LinearLayout) findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) findViewById(R.id.retry);

        if (HttpManager.checkNetwork(UserFollowerActivity.this)) {

            HashMap<String, String> user = session.getUserDetails();
            String UserID = user.get(UserSessionManager.KEY_ID_USER);


            FriendsFetch ff = new FriendsFetch(UserFollowerActivity.this);
            ff.execute(Integer.valueOf(UserID), UserID2);

        } else {

            TestInternet();
        }
    }

    private void TestInternet() {

        checkNetLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        retryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (HttpManager.checkNetwork(UserFollowerActivity.this)) {

                    HashMap<String, String> user = session.getUserDetails();
                    String UserID = user.get(UserSessionManager.KEY_ID_USER);

                    FriendsFetch ff = new FriendsFetch(UserFollowerActivity.this);
                    ff.execute(Integer.valueOf(UserID), UserID2);

                    checkNetLayout.setVisibility(View.GONE);

                    retry = true;

                } else {

                    retry = false;

                    TestInternet();
                }
            }
        });
    }

    public class FriendsFetch extends AsyncTask<Integer, String, String> {

        Context context;

        private int ID;


        public FriendsFetch(Context context) {

        }

        @Override
        protected void onPostExecute(String s) {
//            super.onPostExecute(s);

            if (s.length() > 6) {
                ArrayList<FriendsModel> friendsModels = new JsonParserFriends().parseJson(s);

                FAdapter = new Recycler_Follower_Adapter(itemListFriends, getApplicationContext());
                RecyclerView.LayoutManager FLayoutManger = new GridLayoutManager(getApplicationContext(), 1);
                recyclerFollow.setLayoutManager(FLayoutManger);
                recyclerFollow.setItemAnimator(new DefaultItemAnimator());
                recyclerFollow.setAdapter(FAdapter);
//
                progressBar.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            } else if (s.equals("[]\n")) {

                Toast.makeText(UserFollowerActivity.this, R.string.emptyList, Toast.LENGTH_SHORT).show();

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

                String jsonText = HttpManager.getFriendsFollowers(integers[0], integers[1]);

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
        protected void onPreExecute() {
            super.onPreExecute();

            if (HttpManager.checkNetwork(UserFollowerActivity.this)) {

                progressBar.setVisibility(View.VISIBLE);
                checkNetLayout.setVisibility(View.GONE);

            } else {

                TestInternet();
            }


        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
