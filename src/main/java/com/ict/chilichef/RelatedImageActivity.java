package com.ict.chilichef;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ict.chilichef.Adapter.Recycler_SearchImage_Adapter;
import com.ict.chilichef.Adapter.Recycler_View_Adapter;
import com.ict.chilichef.JsonParser.JsonParserSearchPictures;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.SearchPicturesModel;
import com.ict.chilichef.Sessions.UserSessionManager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RelatedImageActivity extends AppCompatActivity implements Recycler_View_Adapter.ClickListener {

    UserSessionManager session;
    Toolbar toolbar;

    private RecyclerView recyclerRelatedPic;
    private Recycler_SearchImage_Adapter EAdapter;
    private ArrayList<SearchPicturesModel> itemListRelatedGallery;

    ImageView RelatedImage;

    ImageButton back, retryBTN;
    String TITLE, Method;
    int ID;

    private ProgressBar progressBar;
    boolean retry = false;
    LinearLayout checkNetLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related_image);

        itemListRelatedGallery = new ArrayList<>();

        RelatedImage = (ImageView) findViewById(R.id.show_image);
        Bundle bundle = getIntent().getExtras();

        TITLE = bundle.getString("ItemName");
        ID = bundle.getInt("ItemID");

        toolbar = (Toolbar) findViewById(R.id.Toolbar_Public);
        setSupportActionBar(toolbar);
        ((TextView) findViewById(R.id.PublicTitle_toolbar)).setText(TITLE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        checkNetLayout = (LinearLayout) findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) findViewById(R.id.retry);

        back = (ImageButton)findViewById(R.id.PublicLeft_icon_id);
        back.setImageResource(R.drawable.ic_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelatedImageActivity.this.finish();
            }
        });


        session = new UserSessionManager(RelatedImageActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        final String userID = user.get(UserSessionManager.KEY_ID_USER);

        recyclerRelatedPic = (RecyclerView) findViewById(R.id.recycler_related);

        if (HttpManager.checkNetwork(RelatedImageActivity.this)) {

            GalleryRelatedFetch galleryFetch = new GalleryRelatedFetch(RelatedImageActivity.this);
            galleryFetch.execute(userID, TITLE);

        }
        else {

            TestInternet();
        }
    }

    private void TestInternet() {

        checkNetLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        retryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                session = new UserSessionManager(RelatedImageActivity.this);
                HashMap<String, String> user = session.getUserDetails();
                final String userID = user.get(UserSessionManager.KEY_ID_USER);

                if (HttpManager.checkNetwork(RelatedImageActivity.this)) {

                    GalleryRelatedFetch galleryFetch = new GalleryRelatedFetch(RelatedImageActivity.this);
                    galleryFetch.execute(userID, TITLE);

                    checkNetLayout.setVisibility(View.GONE);
                    retry = true;

                } else {

                    retry = false;

                    TestInternet();
                }
            }
        });
    }


    private class GalleryRelatedFetch extends AsyncTask<String, String, String> {

        Context context;


        public GalleryRelatedFetch(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... string) {


            String jsonText = null;
            try {
                jsonText = HttpManager.getRelatedPic(Integer.parseInt(string[0]), string[1]);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return jsonText;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (HttpManager.checkNetwork(RelatedImageActivity.this)) {

                progressBar.setVisibility(View.VISIBLE);
                checkNetLayout.setVisibility(View.GONE);

            } else {

                TestInternet();
            }


        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            JsonParserSearchPictures jsonParserSearchPictures = new JsonParserSearchPictures();
            ArrayList<SearchPicturesModel> searchPicturesModels = jsonParserSearchPictures.parseJson(result);

            for (SearchPicturesModel FD : searchPicturesModels) {
                itemListRelatedGallery.add(FD);
            }

            EAdapter = new Recycler_SearchImage_Adapter(itemListRelatedGallery, RelatedImageActivity.this);
            RecyclerView.LayoutManager ELayoutManger = new GridLayoutManager(RelatedImageActivity.this, 2);
            recyclerRelatedPic.setLayoutManager(ELayoutManger);
            recyclerRelatedPic.setItemAnimator(new DefaultItemAnimator());
            recyclerRelatedPic.setAdapter(EAdapter);


            progressBar.setVisibility(View.GONE);
            checkNetLayout.setVisibility(View.GONE);

        }
    }




    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public void itemClicked(View view, int position) {

        Intent comment_activity = new Intent(RelatedImageActivity.this, CommentRecipesActivity.class);
        startActivity(comment_activity);

    }
}

