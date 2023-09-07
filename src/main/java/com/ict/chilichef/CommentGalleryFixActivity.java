package com.ict.chilichef;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.Adapter.Recycler_GalleryComment_Adapter;
import com.ict.chilichef.JsonParser.JsonParserGalleryComments;
import com.ict.chilichef.JsonParser.JsonParserRecipe;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Manage.StringManager;
import com.ict.chilichef.Model.GalleryCommentsModel;
import com.ict.chilichef.Model.RecipeModel;
import com.ict.chilichef.NavigationItems.ImageSendActivity;
import com.ict.chilichef.NavigationItems.Shared.SharedActivity;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.ict.chilichef.UsersManage.UsersActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CommentGalleryFixActivity extends AppCompatActivity {
    private Recycler_GalleryComment_Adapter commentAdapter;
    private List<GalleryCommentsModel> itemListComment;

    private RecyclerView recyclerComment;
    AlertDialog.Builder builder;
    private RecyclerView.LayoutManager uLayoutManger;

    private UserSessionManager session;
    public String cm, FoodPicUrl, FoodPicName, date;
    int FoodPicId, nComments;
    private TextView numComments, numLikes, userName, foodName, dateTV;
String nLikes;
    EditText ET_comment;
    ImageButton send_comment, back, menu, retryBTN;
    private EditText report_txt;
    private Button report_btn;

    ImageView foodPic;
    CircleImageView userImageUrl;
    String userImage, name, UserID;
    Toolbar toolbar;
    ScrollView scroll;

    private RelativeLayout relProgress;
    boolean retry = false;
    LinearLayout checkNetLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_gallery_fix);

        session = new UserSessionManager(getApplicationContext());
        itemListComment = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();

        FoodPicUrl = bundle.getString("PicFoodUrl");
        FoodPicId = bundle.getInt("PicFoodId");
        FoodPicName = bundle.getString("PicFoodName");
        nComments = bundle.getInt("nComments");
        nLikes = bundle.getString("nLikes");
        userImage = bundle.getString("UserImage");
        name = bundle.getString("UserName");
        date = bundle.getString("Date");
        UserID = bundle.getString("userID");

        toolbar = (Toolbar) findViewById(R.id.Toolbar_Public);
        setSupportActionBar(toolbar);
        ((TextView) findViewById(R.id.PublicTitle_toolbar)).setText(R.string.sendComment);

        back = (ImageButton) findViewById(R.id.PublicLeft_icon_id);
        back.setImageResource(R.drawable.ic_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentGalleryFixActivity.this.finish();
            }
        });

        relProgress = (RelativeLayout) findViewById(R.id.relProgress);
        checkNetLayout = (LinearLayout) findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) findViewById(R.id.retry);
        scroll=(ScrollView)findViewById(R.id.scroll);

        numLikes = (TextView) findViewById(R.id.comment_activity_like);
        numComments = (TextView) findViewById(R.id.recipes_comment);
        ET_comment = (EditText) findViewById(R.id.content_comment);
        send_comment = (ImageButton) findViewById(R.id.send_comment);
        recyclerComment = (RecyclerView) findViewById(R.id.comment_recycler);
        userImageUrl = (CircleImageView) findViewById(R.id.UserImage);
        userName = (TextView) findViewById(R.id.UserName_text);
        foodPic = (ImageView) findViewById(R.id.comment_image_food);
        foodName = (TextView) findViewById(R.id.foodName);
        dateTV = (TextView) findViewById(R.id.dateTV);

        uLayoutManger = new LinearLayoutManager(this);

        Picasso.with(CommentGalleryFixActivity.this).load(userImage).into(userImageUrl);
        Picasso.with(CommentGalleryFixActivity.this).load(FoodPicUrl).into(foodPic);
        numComments.setText(StringManager.convertEnglishNumbersToPersian(String.valueOf(nComments)));
        numLikes.setText(StringManager.convertEnglishNumbersToPersian(nLikes));
        userName.setText(name);
        foodName.setText(FoodPicName);
        dateTV.setText(StringManager.convertEnglishNumbersToPersian(date));

        HashMap<String, String> user = session.getUserDetails();
        final String userID = user.get(UserSessionManager.KEY_ID_USER);

        if (HttpManager.checkNetwork(CommentGalleryFixActivity.this)) {

            if (session.isUserLoggedIn()) {

                CommentsFetch ff = new CommentsFetch(CommentGalleryFixActivity.this);
                ff.execute(FoodPicId, Integer.valueOf(userID));

            } else {

                Toast.makeText(CommentGalleryFixActivity.this, R.string.plzLogIn, Toast.LENGTH_SHORT).show();
            }

        } else {

            TestInternet();
        }

        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (HttpManager.checkNetwork(CommentGalleryFixActivity.this)) {

                    cm = ET_comment.getText().toString();

                    if (!session.isUserLoggedIn()) {

                        builder = new AlertDialog.Builder(CommentGalleryFixActivity.this);
                        builder.setTitle("خطا ");
                        builder.setMessage("لطفا قبل از ارسال نظر به برنامه وارد شوید!");
                        builder.setPositiveButton("بستن", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    } else if (cm.equals("")) {
                        Toast.makeText(CommentGalleryFixActivity.this, "لطفا قبل از ارسال نظر خود را بنویسید", Toast.LENGTH_SHORT).show();
                    } else {

                        CommentsSend ff = new CommentsSend(CommentGalleryFixActivity.this);
                        ff.execute(userID, cm, String.valueOf(FoodPicId));

                        ET_comment.setText(null);
                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(ET_comment.getWindowToken(), 0);

                    }
                } else {

                    Toast.makeText(CommentGalleryFixActivity.this, R.string.Disconnect, Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    private class CommentsFetch extends AsyncTask<Integer, String, String> implements Recycler_GalleryComment_Adapter.ClickListener {

        Context context;

        ArrayList<GalleryCommentsModel> galleryCommentsModels;


        public CommentsFetch(Context context) {

            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (HttpManager.checkNetwork(CommentGalleryFixActivity.this)) {

                relProgress.setVisibility(View.VISIBLE);
                checkNetLayout.setVisibility(View.GONE);

            } else {

                TestInternet();
            }

        }

        @Override
        protected String doInBackground(Integer... integers) {

            try {

                String jsonText = HttpManager.getGalleryComments(integers[0], integers[1]);

                return jsonText;

            } catch (Exception e) {

                return "error";
            }

        }

        @Override
        protected void onPostExecute(String result) {

            if (result.length() > 6) {

                JsonParserGalleryComments jsonParserGalleryCommnents = new JsonParserGalleryComments();
                try {

                    galleryCommentsModels = jsonParserGalleryCommnents.parseJson(result);

                } catch (ParseException e) {

                    e.printStackTrace();
                }

                for (GalleryCommentsModel fdm : galleryCommentsModels) {
                    itemListComment.add(fdm);
                }

                commentAdapter = new Recycler_GalleryComment_Adapter(itemListComment, CommentGalleryFixActivity.this, recyclerComment);
                commentAdapter.setClickListener(this);
                recyclerComment.setLayoutManager(uLayoutManger);
                recyclerComment.setHasFixedSize(true);
                recyclerComment.setItemAnimator(new DefaultItemAnimator());
                recyclerComment.setAdapter(commentAdapter);
                numComments.setText(StringManager.convertEnglishNumbersToPersian(String.valueOf(itemListComment.size())));
                numLikes.setText(StringManager.convertEnglishNumbersToPersian(nLikes));
                scroll.fullScroll(View.FOCUS_DOWN);

                relProgress.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            } else if (result.equals("error") || result.isEmpty()) {

                TestInternet();
            } else if (result.equals("[]\n")) {

                Toast.makeText(CommentGalleryFixActivity.this, R.string.noComment, Toast.LENGTH_SHORT).show();
                relProgress.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);
            }

        }

        @Override
        public void itemOnClicked(View view, int position) {

        }
    }

    private class CommentsSend extends AsyncTask<String, String, String> {

        Context context;

        ArrayList<GalleryCommentsModel> itemListComments = new ArrayList<>();
        ArrayList<GalleryCommentsModel> galleryCommentsModels;


        public CommentsSend(Context context) {

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {


            String jsonText = null;
            try {
                jsonText = HttpManager.sendCommentGallery(Integer.parseInt(strings[0]), strings[1], Integer.parseInt((strings[2])));

                return jsonText;

            } catch (Exception e) {

                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            if (result.length() > 6) {


                JsonParserGalleryComments jsonParserGalleryCommnents = new JsonParserGalleryComments();
                try {

                    galleryCommentsModels = jsonParserGalleryCommnents.parseJson(result);
                } catch (ParseException e) {

                    e.printStackTrace();
                }

                for (GalleryCommentsModel fdm : galleryCommentsModels) {

                    itemListComments.add(fdm);
                }

                itemListComment = itemListComments;

                session = new UserSessionManager(getApplicationContext());

                HashMap<String, String> user = session.getUserDetails();
                final String userID = user.get(UserSessionManager.KEY_ID_USER);

                CommentsFetch ff = new CommentsFetch(CommentGalleryFixActivity.this);
                ff.execute(FoodPicId, Integer.valueOf(userID));

            } else {

                Toast.makeText(context, R.string.Disconnect, Toast.LENGTH_SHORT).show();
            }

        }

    }

    private class getBookmarkedGallery extends AsyncTask<Integer, String, String> {

        Context context;


        JSONObject object = new JSONObject();

        public getBookmarkedGallery(Context context) {
            object = new JSONObject();

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(Integer... integers) {

            String jsonText = HttpManager.galleryBookmark(integers[0], integers[1]);

            return jsonText;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }

    }

    private class Send_Report extends AsyncTask<String, String, String> {

        Context context;


        JSONObject object = new JSONObject();

        public Send_Report(Context context) {
            object = new JSONObject();

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... strings) {

            String jsonText = HttpManager.reportGallery(Integer.parseInt(strings[0]), strings[1]);

            return jsonText;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            int res = Integer.parseInt(String.valueOf(result.charAt(0)));

            if (res == 1) {
                Toast.makeText(context, "با تشکر از همکاری شما ", Toast.LENGTH_SHORT).show();

            } else if (res == 0) {

                Toast.makeText(context, "خطا! لطفا دوباره امتحان کنید", Toast.LENGTH_SHORT).show();

            }

        }


    }

    private void TestInternet() {

        checkNetLayout.setVisibility(View.VISIBLE);
        relProgress.setVisibility(View.GONE);

        retryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (HttpManager.checkNetwork(CommentGalleryFixActivity.this)) {

                    HashMap<String, String> user = session.getUserDetails();
                    String userID = user.get(UserSessionManager.KEY_ID_USER);

                    CommentsFetch ff = new CommentsFetch(CommentGalleryFixActivity.this);
                    ff.execute(FoodPicId, Integer.valueOf(userID));

//                    getBookmarkedGallery gg = new getBookmarkedGallery(CommentGalleryActivity.this);
//                    gg.execute(Integer.valueOf(userID), FoodPicId);

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

//        session = new UserSessionManager(getApplicationContext());
//
//        HashMap<String, String> user = session.getUserDetails();
//        String userID = user.get(UserSessionManager.KEY_ID_USER);
//
//        getBookmarkedGallery gb = new getBookmarkedGallery(CommentGalleryActivity.this);
//        gb.execute(1, Integer.valueOf(userID));


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }


}
