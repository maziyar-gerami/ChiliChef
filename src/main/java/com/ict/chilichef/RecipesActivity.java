package com.ict.chilichef;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ict.chilichef.Adapter.Recycler_Ingredient_Adapter;
import com.ict.chilichef.JsonParser.JsonParserIngredients;
import com.ict.chilichef.JsonParser.JsonParserRecipe;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Manage.StringManager;
import com.ict.chilichef.Model.DateModel;
import com.ict.chilichef.Model.DateParser;
import com.ict.chilichef.Model.IngredientsModel;
import com.ict.chilichef.Model.RecipeModel;
import com.ict.chilichef.NavigationItems.Shared.SharedActivity;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.ict.chilichef.UsersManage.UsersActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RecipesActivity extends AppCompatActivity {


    UserSessionManager session;


    String TITLE, IMAGE;
    int IDFood;
    Toolbar toolbar;
    ImageView Recipes_Food_Image;

    ImageButton menu, back, retryBTN;
    private EditText report_txt;
    private Button report_btn;

    ///Fragment recipes

    private RecyclerView recyclerIngredient;
    private Recycler_Ingredient_Adapter ingAdapter;
    private ArrayList<IngredientsModel> itemListIngredients;

    private ImageView Likes, UnLikes, Save, UnSave, user_comment, imageUser, Related_Image;

    private TextView userName, foodName, DateRecipes, Cat_l, Cat_p,
             Ingredients, NumberOfLikes, hard, time, meal, nViews, CaloriesCount;

    private RelativeLayout relProgress;
    boolean retry = false;
    LinearLayout checkNetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        itemListIngredients = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();

        TITLE = bundle.getString("ItemName");
        IMAGE = bundle.getString("ItemImage");
        IDFood = bundle.getInt("ItemIDFood");

        toolbar = (Toolbar) findViewById(R.id.Toolbar_Public);
        setSupportActionBar(toolbar);
        ((TextView) findViewById(R.id.PublicTitle_toolbar)).setText(TITLE);

        back = (ImageButton) findViewById(R.id.PublicLeft_icon_id);
        back.setImageResource(R.drawable.ic_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RecipesActivity.this.finish();
            }
        });

        CaloriesCount = (TextView) findViewById(R.id.CaloriesCount);

        Recipes_Food_Image = (ImageView) findViewById(R.id.Recipes_Food_Image);
        recyclerIngredient = (RecyclerView) findViewById(R.id.recipes_activity_ingredient);

        relProgress = (RelativeLayout) findViewById(R.id.relProgress);
        checkNetLayout = (LinearLayout) findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) findViewById(R.id.retry);
        Ingredients = (TextView) findViewById(R.id.FetchRecipe);

        session = new UserSessionManager(RecipesActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        final String userID = user.get(UserSessionManager.KEY_ID_USER);

        Picasso.with(getApplicationContext()).load(IMAGE).into(Recipes_Food_Image);

        back = (ImageButton) findViewById(R.id.PublicLeft_icon_id);
        back.setImageResource(R.drawable.ic_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipesActivity.this.finish();
            }
        });

        if (HttpManager.checkNetwork(RecipesActivity.this)) {

            RecipeFetch rf = new RecipeFetch(RecipesActivity.this);
            rf.execute(IDFood, Integer.valueOf(userID));

            IngredientsFetch iff = new IngredientsFetch(RecipesActivity.this);
            iff.execute(IDFood);

        } else {

            TestInternet();

        }


        menu = (ImageButton) findViewById(R.id.report_recipes);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(RecipesActivity.this, R.style.Dialog);
                dialog.setContentView(R.layout.dialog_report);
                dialog.show();

                report_txt = (EditText) dialog.findViewById(R.id.dialog_report_ET);
                report_btn = (Button) dialog.findViewById(R.id.dialog_report_BTN);

                report_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String Report_reason = report_txt.getText().toString();
                        if ( Report_reason.length()>5) {

                            Send_Report SR = new Send_Report(RecipesActivity.this);
                            SR.execute(String.valueOf(IDFood), Report_reason);

                            dialog.dismiss();
                        }else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RecipesActivity.this);
                            // Creates textview for centre title
                            TextView myMsg = new TextView(RecipesActivity.this);
                            myMsg.setText("خطا!");
                            myMsg.setGravity(Gravity.RIGHT);
                            myMsg.setPadding(10,20,50,0);

                            myMsg.setTextSize(20);
                            myMsg.setTextColor(ContextCompat.getColor(RecipesActivity.this, (R.color.colorAccent)));
                            //set custom title
                            builder.setCustomTitle(myMsg);
                            builder.setMessage("لطفا علت گزارش را کامل بیان کنید");
                            builder.setPositiveButton("باشه", null);
                            builder.show();

                        }


                    }
                });

            }
        });


        imageUser = (ImageView) findViewById(R.id.UserImage);
        foodName = (TextView) findViewById(R.id.food_name);
        userName = (TextView) findViewById(R.id.UserName_text);
        DateRecipes = (TextView) findViewById(R.id.DateRecipes);
        NumberOfLikes = (TextView) findViewById(R.id.recipes_activity_like);
        UnLikes = (ImageView) findViewById(R.id.unlike_image);
        Likes = (ImageView) findViewById(R.id.like_image);
        Save = (ImageView) findViewById(R.id.IV_recipe_save);
        UnSave = (ImageView) findViewById(R.id.IV_recipe_unsave);
        time = (TextView) findViewById(R.id.time0);
        hard = (TextView) findViewById(R.id.recipes_activity_hard);
        meal = (TextView) findViewById(R.id.recipes_activity_meale);
        Cat_l = (TextView) findViewById(R.id.recipes_activity_categories_L);
        Cat_p = (TextView) findViewById(R.id.recipes_activity_categories_P);
        user_comment = (ImageView) findViewById(R.id.recipes_comment);
        Related_Image = (ImageView) findViewById(R.id.related_image);
        nViews = (TextView) findViewById(R.id.recipes_activity_view);


        UnLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!session.isUserLoggedIn()) {

                    Toast.makeText(RecipesActivity.this, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();


                } else {
                    Likes.setVisibility(View.VISIBLE);
                    UnLikes.setVisibility(View.INVISIBLE);

                    HashMap<String, String> user = session.getUserDetails();
                    String IdUser = user.get(UserSessionManager.KEY_ID_USER);
                    Like like = new Like(RecipesActivity.this);
                    like.execute(Integer.valueOf(IdUser), IDFood);

                    String getLikes = NumberOfLikes.getText().toString();
                    int intLikes = Integer.parseInt(getLikes);
                    int ab = intLikes + 1;
                    String StringLikes = String.valueOf(ab);
                    NumberOfLikes.setText(StringLikes);

                }
            }
        });

        Likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!session.isUserLoggedIn()) {

                    Toast.makeText(RecipesActivity.this, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();


                } else {

                    UnLikes.setVisibility(View.VISIBLE);
                    Likes.setVisibility(View.INVISIBLE);

                    HashMap<String, String> user = session.getUserDetails();
                    String IdUser = user.get(UserSessionManager.KEY_ID_USER);

                    UnLike unLike = new UnLike(RecipesActivity.this);
                    unLike.execute(Integer.valueOf(IdUser), IDFood);

                    String getLikes = NumberOfLikes.getText().toString();
                    int intLikes = Integer.parseInt(getLikes);
                    int ab = intLikes - 1;
                    String StringLikes = String.valueOf(ab);
                    NumberOfLikes.setText(StringLikes);


                }

            }
        });

        UnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!session.isUserLoggedIn()) {

                    Toast.makeText(RecipesActivity.this, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();


                } else {
                    Save.setVisibility(View.VISIBLE);
                    UnSave.setVisibility(View.INVISIBLE);

                    HashMap<String, String> user = session.getUserDetails();
                    String IdUser = user.get(UserSessionManager.KEY_ID_USER);
                    Save save = new Save(RecipesActivity.this);
                    save.execute(Integer.valueOf(IdUser), IDFood);

                }
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!session.isUserLoggedIn()) {

                    Toast.makeText(RecipesActivity.this, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();


                } else {

                    UnSave.setVisibility(View.VISIBLE);
                    Save.setVisibility(View.INVISIBLE);

                    HashMap<String, String> user = session.getUserDetails();
                    String IdUser = user.get(UserSessionManager.KEY_ID_USER);

                    UnSave unSave = new UnSave(RecipesActivity.this);
                    unSave.execute(Integer.valueOf(IdUser), IDFood);

                }
            }
        });


    }


    private class RecipeFetch extends AsyncTask<Integer, String, String> {

        Context context;

        public RecipeFetch(Context context) {

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(Integer... integers) {

            String jsonText = HttpManager.getRecipe(integers[0], integers[1]);

            return jsonText;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JsonParserRecipe jsonParserRecipe = new JsonParserRecipe();
            final RecipeModel recipeModel = jsonParserRecipe.parseJson(result);

            Ingredients.setText(recipeModel.getRecepies());
            NumberOfLikes.setText(StringManager.convertEnglishNumbersToPersian(String.valueOf(recipeModel.getLikes())));
            nViews.setText(StringManager.convertEnglishNumbersToPersian(String.valueOf(recipeModel.getViews())));
            time.setText(StringManager.convertEnglishNumbersToPersian(String.valueOf(recipeModel.getTime())));
            meal.setText(StringManager.convertEnglishNumbersToPersian(String.valueOf(recipeModel.getMeal())));
            userName.setText(recipeModel.getWriterName());
            foodName.setText(recipeModel.getTitle());
            Cat_l.setText(recipeModel.getCat_l());
            Cat_p.setText(recipeModel.getCat_p());

            switch (recipeModel.getHard()) {
                case 1:
                    hard.setText("آسان");
                    break;

                case 2:
                    hard.setText("متوسط");
                    break;

                case 3:
                    hard.setText("سخت");
                    break;
            }

            Picasso.with(context).load(recipeModel.getWriter_Pic_URL()).into((imageUser));

            DateParser dp = new DateParser(recipeModel.getSubmittedTime());
            DateModel dm = dp.dateAndTimeParser();

            String temp = dm.toString();
            DateRecipes.setText(StringManager.convertEnglishNumbersToPersian(temp));

            Related_Image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent related = new Intent(RecipesActivity.this, RelatedImageActivity.class);

                    related.putExtra("ItemName", recipeModel.getTitle());
                    related.putExtra("ItemID", recipeModel.getId_Food());

                    startActivity(related);
                }
            });


            userName.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View view) {


                    HashMap<String, String> user = session.getUserDetails();
                    String IdUser = user.get(UserSessionManager.KEY_ID_USER);

                    if (IdUser.equals(String.valueOf(recipeModel.getWriter()))) {

                        Intent i = new Intent(context, SharedActivity.class);
                        i.putExtra("Offer", "0");
                        startActivity(i);

                    } else {


                        Intent i = new Intent(context, UsersActivity.class);
                        i.putExtra("IdUSER", recipeModel.getWriter());
                        i.putExtra("PIC_URL", recipeModel.getWriter_Pic_URL());
                        i.putExtra("isFollowing", recipeModel.getIs_Following());
                        context.startActivity(i);
                    }
                }
            });

            imageUser.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View view) {
                    HashMap<String, String> user = session.getUserDetails();
                    String IdUser = user.get(UserSessionManager.KEY_ID_USER);

                    if (IdUser.equals(String.valueOf(recipeModel.getWriter()))) {

                        Intent i = new Intent(context, SharedActivity.class);
                        i.putExtra("Offer", "0");
                        startActivity(i);

                    } else {


                        Intent i = new Intent(context, UsersActivity.class);
                        i.putExtra("IdUSER", recipeModel.getWriter());
                        i.putExtra("PIC_URL", recipeModel.getWriter_Pic_URL());
                        i.putExtra("isFollowing", recipeModel.getIs_Following());
                        context.startActivity(i);
                    }
                }
            });

            user_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!session.isUserLoggedIn()) {

                        Toast.makeText(RecipesActivity.this, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();

                    } else {

                        Intent comment = new Intent(RecipesActivity.this, CommentRecipesActivity.class);

                        comment.putExtra("ItemName", recipeModel.getTitle());
                        comment.putExtra("ItemImage", recipeModel.getPic_URL());
                        comment.putExtra("ItemIDFood", recipeModel.getId_Food());
                        comment.putExtra("ItemImageUser", recipeModel.getWriter_Pic_URL());
                        comment.putExtra("ItemUserName", recipeModel.getWriterName());
                        comment.putExtra("ItemLike", recipeModel.getLikes());
                        comment.putExtra("nComments", recipeModel.getnComments());
                        comment.putExtra("Saved", recipeModel.getSaved());

                        DateParser dp = new DateParser(recipeModel.getSubmittedTime());
                        DateModel dm = dp.dateAndTimeParser();

                        String temp = dm.toString();
                        comment.putExtra("Date", temp);

                        startActivity(comment);

                    }
                }
            });


            int isLiked = recipeModel.getLiked();

            if (isLiked == 0) {
                UnLikes.setVisibility(View.VISIBLE);
                Likes.setVisibility(View.INVISIBLE);
            } else {
                UnLikes.setVisibility(View.INVISIBLE);
                Likes.setVisibility(View.VISIBLE);
            }


            int isSaved = recipeModel.getSaved();

            if (isSaved == 0) {
                UnSave.setVisibility(View.VISIBLE);
                Save.setVisibility(View.INVISIBLE);

            } else {
                UnSave.setVisibility(View.INVISIBLE);
                Save.setVisibility(View.VISIBLE);

            }

        }

    }

    private class IngredientsFetch extends AsyncTask<Integer, String, String> {

        Context context;

        public IngredientsFetch(Context context) {

            this.context = context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            int size = itemListIngredients.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    itemListIngredients.remove(0);
                }

                ingAdapter.notifyItemRangeRemoved(0, size);
            }

            if (HttpManager.checkNetwork(RecipesActivity.this)) {

                relProgress.setVisibility(View.VISIBLE);
                checkNetLayout.setVisibility(View.GONE);

            } else {

                TestInternet();

            }

        }

        @Override
        protected String doInBackground(Integer... integers) {

            String jsonText = HttpManager.getIngredients(integers[0]);

            return jsonText;
        }

        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
            if (result.length() > 6) {

                JsonParserIngredients jsonParserIngredients = new JsonParserIngredients();
                ArrayList<IngredientsModel> ingredientsModel = jsonParserIngredients.parseJson(result);

                for (IngredientsModel IM : ingredientsModel) {
                    itemListIngredients.add(IM);
                }

                ingAdapter = new Recycler_Ingredient_Adapter(itemListIngredients, RecipesActivity.this);

                RecyclerView.LayoutManager fLayoutManger = new GridLayoutManager(RecipesActivity.this, 1);
                recyclerIngredient.setLayoutManager(fLayoutManger);
                recyclerIngredient.setItemAnimator(new DefaultItemAnimator());
                recyclerIngredient.setAdapter(ingAdapter);

                relProgress.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            } else if (result.equals("error") || result.isEmpty()) {

                TestInternet();
            }

        }

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

            String jsonText = HttpManager.recipeLike(integers[0], integers[1]);

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

            String jsonText = HttpManager.recipeUnLike(integers[0], integers[1]);

            return jsonText;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

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

            String jsonText = HttpManager.recipeFavorite(integers[0], integers[1]);

            return jsonText;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

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

            String jsonText = HttpManager.recipeUnFavorite(integers[0], integers[1]);

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

            String jsonText = HttpManager.reportRecipe(Integer.parseInt(strings[0]), strings[1]);

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

                if (HttpManager.checkNetwork(RecipesActivity.this)) {

                    HashMap<String, String> user = session.getUserDetails();
                    String UserID = user.get(UserSessionManager.KEY_ID_USER);

                    RecipeFetch rf = new RecipeFetch(RecipesActivity.this);
                    rf.execute(IDFood, Integer.valueOf(UserID));

                    IngredientsFetch iff = new IngredientsFetch(RecipesActivity.this);
                    iff.execute(IDFood);

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
