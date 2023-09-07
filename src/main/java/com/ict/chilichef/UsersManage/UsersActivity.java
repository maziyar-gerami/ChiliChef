package com.ict.chilichef.UsersManage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ict.chilichef.Adapter.ViewPagerAdapter;
import com.ict.chilichef.JsonParser.JsonParserNumbers;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Manage.StringManager;
import com.ict.chilichef.Model.NumbersModel;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UsersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton back, retryBTN;
    private int ID;
    private String Picture_URL, NameUser;
    private CircleImageView UserPic;
    private Context context;
    private TextView follower, following, score_tv, userName;
    private UserSessionManager session;
    private int nFolowers;
    private int nFolowings;
    private int Score;

    String PicNumber;
    String RecNumber;

    LinearLayout FiveStar, FourStar, ThreeStar, TwoStar, OneStar, ZeroStar;

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    TabLayout tabLayout;

    private ProgressBar progressBar;
    private RelativeLayout relProgress;
    boolean retry = false;
    LinearLayout checkNetLayout;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        final Bundle bundle = getIntent().getExtras();
        session = new UserSessionManager(getApplicationContext());

        NameUser = bundle.getString("UserName");
        ID = bundle.getInt("IdUSER");
        Picture_URL = bundle.getString("PIC_URL");


        back = (ImageButton) findViewById(R.id.user_back);
        back.setImageResource(R.drawable.ic_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UsersActivity.this.finish();
            }
        });

        relProgress = (RelativeLayout) findViewById(R.id.relProgress);
        checkNetLayout = (LinearLayout) findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) findViewById(R.id.retry);

        follower = (TextView) findViewById(R.id.User_follower);
        following = (TextView) findViewById(R.id.User_following);
        score_tv = (TextView) findViewById(R.id.User_Posts);
        userName = (TextView) findViewById(R.id.user_name);
        UserPic = (CircleImageView) findViewById(R.id.user_Image);

        FiveStar = (LinearLayout) findViewById(R.id.fiveStar);
        FourStar = (LinearLayout) findViewById(R.id.fourStar);
        ThreeStar = (LinearLayout) findViewById(R.id.threeStar);
        TwoStar = (LinearLayout) findViewById(R.id.twoStar);
        OneStar = (LinearLayout) findViewById(R.id.oneStar);
        ZeroStar = (LinearLayout) findViewById(R.id.zeroStar);

        PicNumber = String.valueOf(bundle.getInt("NumberOfPic"));
        RecNumber = String.valueOf(bundle.getInt("NumberOfRec"));

        viewPager = (ViewPager) findViewById(R.id.Users_viewPager);
        tabLayout = (TabLayout) findViewById(R.id.Users_tabLayout);


        if (HttpManager.checkNetwork(UsersActivity.this)) {

            GetNumbers getNumbers = new GetNumbers(UsersActivity.this);
            getNumbers.execute(String.valueOf(ID));

        } else {

            TestInternet();

        }

        Picasso.with(UsersActivity.this).load(Picture_URL).into(UserPic);
        userName.setText(NameUser);


        follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(UsersActivity.this, UserFollowerActivity.class);
                i.putExtra("IdUSER", ID);
                startActivity(i);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(UsersActivity.this, UserFollowingActivity.class);
                i.putExtra("IdUSER", ID);
                startActivity(i);
            }
        });

    }

    private void TestInternet() {

        checkNetLayout.setVisibility(View.VISIBLE);
        relProgress.setVisibility(View.GONE);

        retryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (HttpManager.checkNetwork(UsersActivity.this)) {

                    GetNumbers getNumbers = new GetNumbers(UsersActivity.this);
                    getNumbers.execute(String.valueOf(ID));

                    checkNetLayout.setVisibility(View.GONE);

                    retry = true;

                } else {

                    retry = false;

                    TestInternet();
                }
            }
        });
    }


    private class GetNumbers extends AsyncTask<String, String, String> {

        Context context;

        public GetNumbers(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... string) {

            try {

                String jsonText = HttpManager.getNumbers(Integer.valueOf(string[0]));


                return jsonText;
            } catch (Exception e) {
                return "error";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);

            if (result.length() > 6) {

                JsonParserNumbers jsonParserNumbers = new JsonParserNumbers();
                NumbersModel numbersModel = jsonParserNumbers.parseJson(result);

                nFolowers = numbersModel.getNumberOfFollowers();
                nFolowings = numbersModel.getNumberOfFollowings();
                PicNumber = String.valueOf(numbersModel.getNumberOfPictures());
                RecNumber = String.valueOf(numbersModel.getNumberOfRecipes());
                Score = numbersModel.getScore();

                follower.setText(StringManager.convertEnglishNumbersToPersian(Integer.toString(nFolowers)));
                following.setText(StringManager.convertEnglishNumbersToPersian(Integer.toString(nFolowings)));
                score_tv.setText(StringManager.convertEnglishNumbersToPersian(Integer.toString(Score)));

                int Level = numbersModel.getLevel();

                if (Level == 0) {
                    ZeroStar.setVisibility(View.VISIBLE);
                } else if (Level == 1) {
                    OneStar.setVisibility(View.VISIBLE);
                } else if (Level == 2) {
                    TwoStar.setVisibility(View.VISIBLE);
                } else if (Level == 3) {
                    ThreeStar.setVisibility(View.VISIBLE);
                } else if (Level == 4) {
                    FourStar.setVisibility(View.VISIBLE);
                } else if (Level > 4) {
                    FiveStar.setVisibility(View.VISIBLE);
                }


                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                viewPagerAdapter.addFragments(new GalleryUsersFragment(), "تصاویر ارسالی" + " (" + StringManager.convertEnglishNumbersToPersian(PicNumber) + ")");
                viewPagerAdapter.addFragments(new RecipesUsersFragment(), "دستور پخت" + " (" + StringManager.convertEnglishNumbersToPersian(RecNumber) + ")");

                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);

                TabLayout.Tab tab = tabLayout.getTabAt(1);
                tab.select();

                Typeface tf = Typeface.createFromAsset(getAssets(), "font/IRANSans.ttf");
                ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
                int tabsCount = vg.getChildCount();
                for (int j = 0; j < tabsCount; j++) {
                    ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
                    int tabChildsCount = vgTab.getChildCount();
                    for (int i = 0; i < tabChildsCount; i++) {
                        View tabViewChild = vgTab.getChildAt(i);
                        if (tabViewChild instanceof TextView) {
                            ((TextView) tabViewChild).setTypeface(tf);
                        }
                    }
                }

                relProgress.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            } else if (result.equals("error") || result.isEmpty()) {

                TestInternet();
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}