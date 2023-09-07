package com.ict.chilichef;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.Adapter.Recycler_RecipesComment_Adapter;
import com.ict.chilichef.JsonParser.JsonParserGalleryComments;
import com.ict.chilichef.JsonParser.JsonParserRecipeComments;
import com.ict.chilichef.Manage.HttpManager;

import com.ict.chilichef.Manage.StringManager;
import com.ict.chilichef.Model.RecipeCommentsModel;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CommentRecipesActivity extends AppCompatActivity {


    EditText ET_comment;
    ImageButton back, send_comment, retryBTN;
    ImageView Imagefood, Saved, UnSaved;
    TextView name_user, image_like, recipescomment, dateTV;
    public String cm;
    ScrollView scroll;

    private Recycler_RecipesComment_Adapter commentAdapter;
    private List<RecipeCommentsModel> itemListComment;

    private RecyclerView recyclerComment;
    AlertDialog.Builder builder;

    private UserSessionManager session;

    private CircleImageView UserImage;

    Toolbar toolbar;

    private RelativeLayout relProgress;
    boolean retry = false;
    LinearLayout checkNetLayout;

    private String TITLE, IMAGE_USER, IMAGE_FOOD, UserName, IMAGE_LIKE, IDFood, nComment, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        toolbar = (Toolbar) findViewById(R.id.Toolbar_Public);
        setSupportActionBar(toolbar);
        ((TextView) findViewById(R.id.PublicTitle_toolbar)).setText(R.string.sendComment);

        back = (ImageButton) findViewById(R.id.PublicLeft_icon_id);
        back.setImageResource(R.drawable.ic_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentRecipesActivity.this.finish();
            }
        });


        Bundle bundle = getIntent().getExtras();

        TITLE = bundle.getString("ItemName");
        IMAGE_FOOD = bundle.getString("ItemImage");
        IDFood = bundle.getString("ItemIDFood");
        IMAGE_USER = bundle.getString("ItemImageUser");
        UserName = bundle.getString("ItemUserName");
        IMAGE_LIKE = String.valueOf(bundle.getInt("ItemLike"));
        nComment = String.valueOf(bundle.getInt("nComments"));
        date = bundle.getString("Date");

        itemListComment = new ArrayList<>();
        session = new UserSessionManager(CommentRecipesActivity.this);

        relProgress = (RelativeLayout) findViewById(R.id.relProgress);
        checkNetLayout = (LinearLayout) findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) findViewById(R.id.retry);
        scroll = (ScrollView) findViewById(R.id.scroll);

        UserImage = (CircleImageView) findViewById(R.id.UserImage);
        Imagefood = (ImageView) findViewById(R.id.comment_image_food);
        name_user = (TextView) findViewById(R.id.UserName_text);
        image_like = (TextView) findViewById(R.id.comment_activity_like);
        recyclerComment = (RecyclerView) findViewById(R.id.comment_recycler);
        recipescomment = (TextView) findViewById(R.id.recipes_comment);
        ET_comment = (EditText) findViewById(R.id.content_comment);
        send_comment = (ImageButton) findViewById(R.id.send_comment);
        dateTV = (TextView) findViewById(R.id.dateTV);

        if (session.isUserLoggedIn()) {

            HashMap<String, String> user = session.getUserDetails();
            final String userID = user.get(UserSessionManager.KEY_ID_USER);

            Picasso.with(getApplicationContext()).load(IMAGE_USER).into(UserImage);
            Picasso.with(getApplicationContext()).load(IMAGE_FOOD).into(Imagefood);

            name_user.setText(UserName);
            image_like.setText(StringManager.convertEnglishNumbersToPersian(IMAGE_LIKE));
            recipescomment.setText(StringManager.convertEnglishNumbersToPersian(nComment));
            dateTV.setText(StringManager.convertEnglishNumbersToPersian(date));

            if (HttpManager.checkNetwork(CommentRecipesActivity.this)) {
                if (session.isUserLoggedIn()) {

                    RecCommentsFetch ff = new RecCommentsFetch(CommentRecipesActivity.this);
                    ff.execute(Integer.valueOf(IDFood), Integer.valueOf(userID));

                } else {

                    Toast.makeText(CommentRecipesActivity.this, R.string.plzLogIn, Toast.LENGTH_SHORT).show();
                }

            } else {

                TestInternet();
            }
        }

        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (HttpManager.checkNetwork(CommentRecipesActivity.this)) {

                    cm = ET_comment.getText().toString();

                    if (!session.isUserLoggedIn()) {

                        builder = new AlertDialog.Builder(CommentRecipesActivity.this);
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

                        Toast.makeText(CommentRecipesActivity.this, "لطفا قبل از ارسال نظر خود را بنویسید", Toast.LENGTH_SHORT).show();

                    } else {


                        HashMap<String, String> user = session.getUserDetails();
                        final String userID = user.get(UserSessionManager.KEY_ID_USER);

                        RecCommentsSend ff = new RecCommentsSend(CommentRecipesActivity.this);
                        ff.execute(userID, cm, String.valueOf(IDFood));

                        ET_comment.setText(null);

                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(ET_comment.getWindowToken(), 0);

                    }
                } else {

                    Toast.makeText(CommentRecipesActivity.this, R.string.Disconnect, Toast.LENGTH_LONG).show();
                }
            }

        });


    }

    private class RecCommentsFetch extends AsyncTask<Integer, String, String> {

        Context context;

        ArrayList<RecipeCommentsModel> recipeCommentsModels;


        public RecCommentsFetch(Context context) {

            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (HttpManager.checkNetwork(CommentRecipesActivity.this)) {

                relProgress.setVisibility(View.VISIBLE);
                checkNetLayout.setVisibility(View.GONE);

            } else {

                TestInternet();
            }

        }

        @Override
        protected String doInBackground(Integer... integers) {

            try {

                String jsonText = HttpManager.getRecipeComments(integers[0], integers[1]);

                return jsonText;

            } catch (Exception e) {

                return "error";
            }

        }

        @Override
        protected void onPostExecute(String result) {

            if (result.length() > 6) {

                JsonParserRecipeComments jsonParserRecipeComments = new JsonParserRecipeComments();
                try {

                    recipeCommentsModels = jsonParserRecipeComments.parseJson(result);

                } catch (ParseException e) {

                    e.printStackTrace();
                }

                for (RecipeCommentsModel fdm : recipeCommentsModels) {
                    itemListComment.add(fdm);
                }

                commentAdapter = new Recycler_RecipesComment_Adapter(itemListComment, CommentRecipesActivity.this, recyclerComment);
                final RecyclerView.LayoutManager uLayoutManger = new GridLayoutManager(CommentRecipesActivity.this, 1);
                recyclerComment.setLayoutManager(uLayoutManger);
                recyclerComment.setItemAnimator(new DefaultItemAnimator());
                recyclerComment.setAdapter(commentAdapter);

                scroll.fullScroll(View.FOCUS_DOWN);

                relProgress.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            } else if (result.equals("error") || result.isEmpty()) {

                TestInternet();
            } else if (result.equals("[]\n")) {

                Toast.makeText(CommentRecipesActivity.this, R.string.noComment, Toast.LENGTH_SHORT).show();
                relProgress.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);
            }

        }
    }

    private class RecCommentsSend extends AsyncTask<String, String, String> {

        Context context;

        ArrayList<RecipeCommentsModel> itemListComments = new ArrayList<>();
        ArrayList<RecipeCommentsModel> recipeCommentsModels;


        public RecCommentsSend(Context context) {

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
                jsonText = HttpManager.sendCommentRecipe(Integer.parseInt(strings[0]), strings[1], Integer.parseInt((strings[2])));
                return jsonText;

            } catch (Exception e) {

                return "error";
            }

        }

        @Override
        protected void onPostExecute(String result) {

            if (result.length() > 6) {

                JsonParserRecipeComments jsonParserRecipeCommnents = new JsonParserRecipeComments();
                try {

                    recipeCommentsModels = jsonParserRecipeCommnents.parseJson(result);
                } catch (ParseException e) {

                    e.printStackTrace();
                }

                for (RecipeCommentsModel fdm : recipeCommentsModels) {
                    itemListComments.add(fdm);
                }

                itemListComment = itemListComments;

                session = new UserSessionManager(getApplicationContext());

                HashMap<String, String> user = session.getUserDetails();
                final String userID = user.get(UserSessionManager.KEY_ID_USER);

                RecCommentsFetch ff = new RecCommentsFetch(CommentRecipesActivity.this);
                ff.execute(Integer.valueOf(IDFood), Integer.valueOf(userID));


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

                if (HttpManager.checkNetwork(CommentRecipesActivity.this)) {

                    HashMap<String, String> user = session.getUserDetails();
                    String userID = user.get(UserSessionManager.KEY_ID_USER);

                    RecCommentsFetch ff = new RecCommentsFetch(CommentRecipesActivity.this);
                    ff.execute(Integer.valueOf(IDFood), Integer.valueOf(userID));

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
}
