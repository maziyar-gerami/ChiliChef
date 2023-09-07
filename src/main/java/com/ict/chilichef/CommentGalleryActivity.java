package com.ict.chilichef;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
import com.ict.chilichef.JsonParser.JsonParserGalleryLike;
import com.ict.chilichef.JsonParser.JsonParserRecipe;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Manage.StringManager;
import com.ict.chilichef.Model.DateModel;
import com.ict.chilichef.Model.DateParser;
import com.ict.chilichef.Model.GalleryCommentsModel;
import com.ict.chilichef.Model.GalleryLikeModel;
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

public class CommentGalleryActivity extends AppCompatActivity {

    private Recycler_GalleryComment_Adapter commentAdapter;
    private List<GalleryCommentsModel> itemListComment;

    private RecyclerView recyclerComment;
    AlertDialog.Builder builder;
    private RecyclerView.LayoutManager uLayoutManger;

    private UserSessionManager session;
    private String cm, FoodPicUrl, FoodPicName, date, nLikes;
    int FoodPicId, nComments, isLiked;
    private TextView numComments, numLikes, userName, foodName, dateTV;
    EditText ET_comment;
    ImageButton send_comment, back, menu, retryBTN, likeBtn;
    private EditText report_txt;
    private Button report_btn;

    ImageView foodPic;
    CircleImageView userImageUrl;
    String userImage, name, UserID;
    Toolbar toolbar;
    String numberOfLike;
    ScrollView scroll;

    private RelativeLayout relProgress;
    boolean retry = false;
    LinearLayout checkNetLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        session = new UserSessionManager(getApplicationContext());
        itemListComment = new ArrayList<>();

        final HashMap<String, String> user = session.getUserDetails();
        final String userID = user.get(UserSessionManager.KEY_ID_USER);

        Bundle bundle = getIntent().getExtras();

        FoodPicUrl = bundle.getString("PicFoodUrl");
        FoodPicId = bundle.getInt("PicFoodId");
        FoodPicName = bundle.getString("PicFoodName");
        nComments = bundle.getInt("nComments");
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
                CommentGalleryActivity.this.finish();
            }
        });

        relProgress = (RelativeLayout) findViewById(R.id.relProgress);
        checkNetLayout = (LinearLayout) findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) findViewById(R.id.retry);
        likeBtn = (ImageButton) findViewById(R.id.like_image);
        scroll=(ScrollView)findViewById(R.id.scroll);

        dataFetch df = new dataFetch(CommentGalleryActivity.this);
        df.execute(FoodPicId, Integer.valueOf(userID));


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

        Picasso.with(CommentGalleryActivity.this).load(userImage).into(userImageUrl);
        Picasso.with(CommentGalleryActivity.this).load(FoodPicUrl).into(foodPic);
        numComments.setText(StringManager.convertEnglishNumbersToPersian(String.valueOf(nComments)));
        userName.setText(name);
        foodName.setText(FoodPicName);


        userImageUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> user = session.getUserDetails();
                final String userID = user.get(UserSessionManager.KEY_ID_USER);

                if (userID.equals(UserID)) {

                    Intent i = new Intent(CommentGalleryActivity.this, SharedActivity.class);
                    CommentGalleryActivity.this.startActivity(i);

                } else {

                    Intent i = new Intent(CommentGalleryActivity.this, UsersActivity.class);
                    i.putExtra("UserName", name);
                    i.putExtra("IdUSER", UserID);
                    i.putExtra("PIC_URL", userImage);
                    CommentGalleryActivity.this.startActivity(i);
                }
            }
        });


        if (HttpManager.checkNetwork(CommentGalleryActivity.this)) {


            if (session.isUserLoggedIn()) {

                CommentsFetch ff = new CommentsFetch(CommentGalleryActivity.this);
                ff.execute(FoodPicId, Integer.valueOf(userID));


            } else {

                Toast.makeText(CommentGalleryActivity.this, R.string.plzLogIn, Toast.LENGTH_SHORT).show();
            }

        } else {

            TestInternet();
        }

        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (HttpManager.checkNetwork(CommentGalleryActivity.this)) {

                    cm = ET_comment.getText().toString();

                    if (!session.isUserLoggedIn()) {

                        builder = new AlertDialog.Builder(CommentGalleryActivity.this);
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
                        Toast.makeText(CommentGalleryActivity.this, "لطفا قبل از ارسال نظر خود را بنویسید", Toast.LENGTH_SHORT).show();
                    } else {

                        CommentsSend ff = new CommentsSend(CommentGalleryActivity.this);
                        ff.execute(userID, cm, String.valueOf(FoodPicId));

                        ET_comment.setText(null);
                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(ET_comment.getWindowToken(), 0);

                    }
                } else {

                    Toast.makeText(CommentGalleryActivity.this, R.string.Disconnect, Toast.LENGTH_LONG).show();
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

            if (HttpManager.checkNetwork(CommentGalleryActivity.this)) {

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


                commentAdapter = new Recycler_GalleryComment_Adapter(itemListComment, CommentGalleryActivity.this, recyclerComment);
                commentAdapter.setClickListener(this);
                recyclerComment.setLayoutManager(uLayoutManger);
                recyclerComment.setHasFixedSize(true);
                recyclerComment.setItemAnimator(new DefaultItemAnimator());
                recyclerComment.setAdapter(commentAdapter);
                numComments.setText(StringManager.convertEnglishNumbersToPersian(String.valueOf(itemListComment.size())));
                numLikes.setText(StringManager.convertEnglishNumbersToPersian(numberOfLike));
                scroll.fullScroll(View.FOCUS_DOWN);

                relProgress.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            } else if (result.equals("error") || result.isEmpty()) {

                TestInternet();

            } else if (result.equals("[]\n")) {

                Toast.makeText(CommentGalleryActivity.this, R.string.noComment, Toast.LENGTH_SHORT).show();
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

                CommentsFetch ff = new CommentsFetch(CommentGalleryActivity.this);
                ff.execute(FoodPicId, Integer.valueOf(userID));

            } else {

                Toast.makeText(context, R.string.Disconnect, Toast.LENGTH_SHORT).show();
            }

        }

    }


    private void TestInternet() {

        checkNetLayout.setVisibility(View.VISIBLE);
        relProgress.setVisibility(View.GONE);

        retryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (HttpManager.checkNetwork(CommentGalleryActivity.this)) {

                    HashMap<String, String> user = session.getUserDetails();
                    String userID = user.get(UserSessionManager.KEY_ID_USER);

                    CommentsFetch ff = new CommentsFetch(CommentGalleryActivity.this);
                    ff.execute(FoodPicId, Integer.valueOf(userID));

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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }


    private class Like extends AsyncTask<Integer, String, String> {

        Context context;


        JSONObject object = new JSONObject();

        public Like(Context context) {
            object = new JSONObject();

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... integers) {

            String jsonText = HttpManager.gallerylike(integers[0], integers[1]);

            return jsonText;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


        }

    }

    private class UnLike extends AsyncTask<Integer, String, String> {

        Context context;


        JSONObject object = new JSONObject();

        public UnLike(Context context) {
            object = new JSONObject();

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(Integer... integers) {

            String jsonText = HttpManager.galleryUnLike(integers[0], integers[1]);

            return jsonText;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


        }


    }

    private class dataFetch extends AsyncTask<Integer, String, String> {

        Context context;

        public dataFetch(Context context) {

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(Integer... integers) {

            String jsonText = HttpManager.getGalleryDateLikes(integers[0], integers[1]);

            return jsonText;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JsonParserGalleryLike jsonParserLike = new JsonParserGalleryLike();
            final GalleryLikeModel LikeModel = jsonParserLike.parseJson(result);

            isLiked = LikeModel.getIsLiked();
            if (isLiked == 1) {

                likeBtn.setImageResource(R.drawable.ic_likea);


            } else {

                likeBtn.setImageResource(R.drawable.ic_like);

            }


            numberOfLike = String.valueOf(LikeModel.getnLikes());


            numLikes.setText(StringManager.convertEnglishNumbersToPersian(numberOfLike));

            DateParser dp = new DateParser(LikeModel.getDate());
            DateModel dm = dp.dateAndTimeParser();

            String temp = dm.toString();
            dateTV.setText(StringManager.convertEnglishNumbersToPersian(temp));





            HashMap<String, String> user = session.getUserDetails();
            final String userID = user.get(UserSessionManager.KEY_ID_USER);

            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isLiked == 1) {

                        likeBtn.setImageResource(R.drawable.ic_like);
                        UnLike unLike = new UnLike(CommentGalleryActivity.this);
                        unLike.execute(Integer.valueOf(userID), FoodPicId);

                        String a = numLikes.getText().toString();
                        int A = Integer.parseInt(a);
                        int ab = A - 1;

                        isLiked = 0;

                        numberOfLike = String.valueOf(ab);

                        numLikes.setText(StringManager.convertEnglishNumbersToPersian(numberOfLike));
                    } else {
                        likeBtn.setImageResource(R.drawable.ic_likea);

                        Like like = new Like(CommentGalleryActivity.this);
                        like.execute(Integer.valueOf(userID), FoodPicId);

                        String a = numLikes.getText().toString();
                        int A = Integer.parseInt(a);
                        int ab = A + 1;
                        numberOfLike = String.valueOf(ab);
                        numLikes.setText(StringManager.convertEnglishNumbersToPersian(numberOfLike));
                        isLiked = 1;
                    }

                }

            });


        }

    }

}
