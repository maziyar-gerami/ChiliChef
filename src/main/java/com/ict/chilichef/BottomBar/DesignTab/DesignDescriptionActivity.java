package com.ict.chilichef.BottomBar.DesignTab;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.Adapter.DesignPic_ViewPager_Adapter;
import com.ict.chilichef.Adapter.Recycler_Image_Adapter;
import com.ict.chilichef.Adapter.Recycler_View_Adapter;
import com.ict.chilichef.JsonParser.JsonParserDesign;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.Data;
import com.ict.chilichef.Model.DesignModel;
import com.ict.chilichef.R;
import com.ict.chilichef.RecipesActivity;
import com.ict.chilichef.Sessions.UserSessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DesignDescriptionActivity extends AppCompatActivity {


    int ID;
    Toolbar toolbar;
    ImageButton back, Save, UnSave;
    TextView Title_fetch, Description_fetch;
    TabLayout tabLayout;
    ViewPager viewPager;
    DesignPic_ViewPager_Adapter pagerAdapter;
    String Toolbar_title;

    ImageButton retryBTN;
    private RelativeLayout relDesign;
    boolean retry = false;
    LinearLayout checkNetLayout;
    private Bundle bundle;

    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_description);

        session = new UserSessionManager(DesignDescriptionActivity.this);
        Bundle bundle = getIntent().getExtras();

        ID = bundle.getInt("ItemID");
        Toolbar_title = bundle.getString("ItemTitle");


        Title_fetch = (TextView) findViewById(R.id.design_title);
        Description_fetch = (TextView) findViewById(R.id.design_description);
        viewPager = (ViewPager) findViewById(R.id.DesignPic_ViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabDots);
        Save = (ImageButton) findViewById(R.id.design_save);
        UnSave = (ImageButton) findViewById(R.id.design_unsave);

        toolbar = (Toolbar) findViewById(R.id.Toolbar_Public);
        setSupportActionBar(toolbar);
        ((TextView) findViewById(R.id.PublicTitle_toolbar)).setText(Toolbar_title);

        back = (ImageButton) findViewById(R.id.PublicLeft_icon_id);
        back.setImageResource(R.drawable.ic_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DesignDescriptionActivity.this.finish();
            }
        });

        relDesign = (RelativeLayout) findViewById(R.id.relDesign);
        checkNetLayout = (LinearLayout) findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) findViewById(R.id.retry);

        HashMap<String, String> user = session.getUserDetails();
        String UserID = user.get(UserSessionManager.KEY_ID_USER);

        if (HttpManager.checkNetwork(DesignDescriptionActivity.this)) {

            DesignFetch df = new DesignFetch(DesignDescriptionActivity.this);
            df.execute(ID, Integer.valueOf(UserID));

        } else {

            TestInternet();
        }


        UnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!session.isUserLoggedIn()) {

                    Toast.makeText(DesignDescriptionActivity.this, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();


                } else {
                    Save.setVisibility(View.VISIBLE);
                    UnSave.setVisibility(View.INVISIBLE);

                    HashMap<String, String> user = session.getUserDetails();
                    String UserID = user.get(UserSessionManager.KEY_ID_USER);

                    Save s = new Save(DesignDescriptionActivity.this);
                    s.execute(Integer.valueOf(UserID), ID);


                }
            }
        });


        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!session.isUserLoggedIn()) {

                    Toast.makeText(DesignDescriptionActivity.this, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();


                } else {

                    UnSave.setVisibility(View.VISIBLE);
                    Save.setVisibility(View.INVISIBLE);

                    HashMap<String, String> user = session.getUserDetails();
                    String IdUser = user.get(UserSessionManager.KEY_ID_USER);

                    UnSave unSave = new UnSave(DesignDescriptionActivity.this);
                    unSave.execute(Integer.valueOf(IdUser), ID);


                }
            }
        });


    }

    private class DesignFetch extends AsyncTask<Integer, String, String> {
        Context context;

        public DesignFetch(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (HttpManager.checkNetwork(DesignDescriptionActivity.this)) {

                relDesign.setVisibility(View.VISIBLE);
                checkNetLayout.setVisibility(View.GONE);

            } else {


                TestInternet();
            }


        }

        @Override
        protected String doInBackground(Integer... integers) {

            try {
                String jsonText = HttpManager.getDesign(integers[0], integers[1]);


                return jsonText;

            } catch (Exception e) {

                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (!(result.equals("error"))) {

                JsonParserDesign jsonParserDesign = new JsonParserDesign();
                DesignModel designModel = jsonParserDesign.parseJson(result);

                if (designModel.getSaved() == 1)
                    Save.setVisibility(View.VISIBLE);

                Title_fetch.setText(designModel.getTitle());
                Description_fetch.setText(designModel.getDescription());

                List<String> im = designModel.getPic_Url();

                pagerAdapter = new DesignPic_ViewPager_Adapter(DesignDescriptionActivity.this, im);
                viewPager.setAdapter(pagerAdapter);
                tabLayout.setupWithViewPager(viewPager, true);

                relDesign.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            } else if (result.equals("error") || result.isEmpty()) {

                TestInternet();
            }

        }
    }

    private class Save extends AsyncTask<Integer, String, String> {

        Context context;


        JSONObject object = new JSONObject();

        public Save(Context context) {
            object = new JSONObject();

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(Integer... integers) {

            String jsonText = HttpManager.designBookmark(integers[0], integers[1]);

            return jsonText;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //progress.dismiss();

        }

    }

    private class UnSave extends AsyncTask<Integer, String, String> {

        Context context;


        JSONObject object = new JSONObject();

        public UnSave(Context context) {
            object = new JSONObject();

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... integers) {

            String jsonText = HttpManager.designUnBookmark(integers[0], integers[1]);

            return jsonText;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }


    }

    private void TestInternet() {

        checkNetLayout.setVisibility(View.VISIBLE);
        relDesign.setVisibility(View.GONE);

        retryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> user = session.getUserDetails();
                String UserID = user.get(UserSessionManager.KEY_ID_USER);

                if (HttpManager.checkNetwork(DesignDescriptionActivity.this)) {

                    DesignFetch df = new DesignFetch(DesignDescriptionActivity.this);
                    df.execute(ID, Integer.valueOf(UserID));
                    checkNetLayout.setVisibility(View.GONE);

                    retry = true;

                } else

                {

                    retry = false;

                    TestInternet();
                }
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }
}

