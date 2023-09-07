package com.ict.chilichef;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.ict.chilichef.BottomBar.BbGalleryFragment;
import com.ict.chilichef.BottomBar.BbHomeFragment;
import com.ict.chilichef.BottomBar.DesignTab.FirstPageDesign.BbDesignFragment;
import com.ict.chilichef.BottomBar.RecipesTab.BbRecipesFragment;
import com.ict.chilichef.BottomBar.SearchTab.BbSearchFragment;
import com.ict.chilichef.JsonParser.JsonParserNumbers;
import com.ict.chilichef.JsonParser.JsonParserUsers;
import com.ict.chilichef.JsonParser.JsonParserUsersPrompted;
import com.ict.chilichef.Manage.BottomNavigationViewHelper;
import com.ict.chilichef.Manage.DBFetch;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Manage.ImageUploadHandler;
import com.ict.chilichef.Manage.IngFetch;
import com.ict.chilichef.Manage.StringManager;
import com.ict.chilichef.Model.NumbersModel;
import com.ict.chilichef.Model.UserModel;
import com.ict.chilichef.Model.UsersPromptedModel;
import com.ict.chilichef.NavigationItems.ARMActivity;
import com.ict.chilichef.NavigationItems.Bookmark.BookmarkActivity;
import com.ict.chilichef.NavigationItems.Categories.FoodCategoriesActivity;
import com.ict.chilichef.NavigationItems.ImageSendActivity;
import com.ict.chilichef.NavigationItems.Notes.NotesCatActivity;
import com.ict.chilichef.NavigationItems.RecipesSendActivity;
import com.ict.chilichef.NavigationItems.Shared.FollowerActivity;
import com.ict.chilichef.NavigationItems.Shared.FollowingActivity;
import com.ict.chilichef.NavigationItems.Shared.SharedActivity;
import com.ict.chilichef.NavigationItems.SupportActivity;
import com.ict.chilichef.NavigationItems.What2Cook.WhatCookActivity;
import com.ict.chilichef.Sessions.UserSessionManager;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.ronash.pushe.Pushe;
import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static com.ict.chilichef.Manage.HttpManager.getDataUser;
import static com.ict.chilichef.Manage.HttpManager.md5;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;      //عریف جهت باز و نسته شدن منوی کشویی
    ActionBarDrawerToggle actionBarDrawerToggle;        //معرعی آیکن همبرگری منوی کشویی
    NavigationView navigationView;      //مشخص کردن اکش آیکن های منوی کشویی
    Toolbar toolbar;

    private BbSearchFragment fragment1;
    private BbGalleryFragment fragment2;
    private BbHomeFragment fragment3;
    private BbRecipesFragment fragment4;
    private BbDesignFragment fragment5;
    private ViewGroup frameLayout;
    private BottomNavigationView bottomBar;
    private ImageView profilePicture;
    private ImageButton editProfilePen, showLoginPass, dialogClose;
    private TextView enter, registerRuleTV, forgetPassword, registerTV, loginTV,
            follower, following, Score_tx;

    private EditText registerName, registerEmail, registerPassword;
    private CheckBox regisrerRule;
    private Button btn_rules;
    private Button loginButton;
    private Target loadtarget;
    private LinearLayout rulesLayout;
    private AlertDialog.Builder builder;
    private RelativeLayout nameRelative;
    private int VisitTime, status;
    private int nFolowers, nFolowings, Score;
    private LinearLayout ZeroStar, FiveStar, FourStar, ThreeStar, TwoStar, OneStar;

    Boolean StateLogin = true;

    private UserSessionManager session;

    public static final String HOSTNAME = "http://Ashpazchili.ir/";
    public static final String URL_BASE = HOSTNAME + "Chili/";
    public static final String URL_PAGES = URL_BASE + "Pages/";
    public static final String URL_ProfileImages = URL_BASE + "Images/ProfilesImages/";
    public static final String URL_FoodsImages200 = URL_BASE + "Images/FoodsImages/200/";
    public static final String URL_FoodsImages500 = URL_BASE + "Images/FoodsImages/500/";
    public static final String URL_Gallery = URL_BASE + "Images/Gallery/";
    public static final String URL_NotesPics = URL_BASE + "Images/NotesImages/";
    public static final String URL_DesignPics = URL_BASE + "Images/Design/";
    public static final String URL_AdvertisePics = URL_BASE + "Images/Advertises/";
    public static final String UPLOAD_URL = "http://ashpazchili.ir/Chili/Pages/editProfile.php";
    public static final String UPLOAD_KEY = "image";

    public static boolean catStat = false;
    public static boolean ingStat = false;

    DBFetch task = new DBFetch(MainActivity.this);
    IngFetch ingTask = new IngFetch(MainActivity.this);


    String selectedimagePath;
    Bitmap rotatedBitmap = null;
    Bitmap bitmap = null;
    static final int gallery = 20;
    static final int camera = 50;
    Uri fileUri;
    Uri uri;

    private CircleImageView dialogProfileImage;
    private ImageButton closeEditDialog, changeProfileImage, showPassword, showNewPassword;
    private EditText ETXprofileName, ETXprofilePhone, ETXprofileWeight, ETXprofileLength, ETXprofilePassword, ETXprofileNewPassword;
    private TextView TVprofileEmail;
    private Button editDoneBTN;
    private CheckBox fatBox, sugarBox;
    private String get_name, get_curPass, get_newPass, get_telephone, get_length, get_weight, get_email, get_fat, get_sugar;

    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Clear_Cache();
        Pushe.initialize(this, true);
        session = new UserSessionManager(MainActivity.this);

        if ((session.isUserLoggedIn())) {

        } else {
            hideItem();
        }

        //معرعی تولبار در اکتیویتی
        toolbar = (Toolbar) findViewById(R.id.Toolbar_Public);
        setSupportActionBar(toolbar);

        //عریف جهت باز و نسته شدن منوی کشویی
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.isDrawerIndicatorEnabled();

        //مشخص کردن اکش آیکن های منوی کشویی
        navigationView = (NavigationView) findViewById(R.id.Navigation_View);

        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {


                switch (item.getItemId()) {


                    case R.id.nav_Categories:

                        Intent a = new Intent(MainActivity.this, FoodCategoriesActivity.class);
                        a.putExtra("TITLE", getString(R.string.categories));
                        startActivity(a);
                        drawerLayout.closeDrawers();

                        break;


                    case R.id.nav_favorites:

                        if (session.isUserLoggedIn()) {

                            Intent b = new Intent(MainActivity.this, BookmarkActivity.class);
                            b.putExtra("TITLE", getString(R.string.recipe_favorite));
                            startActivity(b);
                            drawerLayout.closeDrawers();
                        } else {
                            Toast.makeText(MainActivity.this, R.string.plzLogIn, Toast.LENGTH_SHORT).show();
                        }

                        break;


                    case R.id.nav_recipe_notes:

                        if (session.isUserLoggedIn()) {

                            Intent c = new Intent(MainActivity.this, NotesCatActivity.class);
                            c.putExtra("TITLE", getString(R.string.recipe_offer_notes));
                            startActivity(c);
                            drawerLayout.closeDrawers();

                        } else {
                            Toast.makeText(MainActivity.this, R.string.plzLogIn, Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case R.id.nav_chef_wht:

                        if (session.isUserLoggedIn()) {
                            Intent d = new Intent(MainActivity.this, WhatCookActivity.class);
                            d.putExtra("TITLE", getString(R.string.chef_wht));
                            startActivity(d);
                            drawerLayout.closeDrawers();

                        } else {
                            Toast.makeText(MainActivity.this, R.string.plzLogIn, Toast.LENGTH_SHORT).show();
                        }


                        break;


                    case R.id.nav_send_recipe:

                        if (session.isUserLoggedIn()) {
                            Intent e = new Intent(MainActivity.this, RecipesSendActivity.class);
                            e.putExtra("TITLE", getString(R.string.send_recipe));
                            startActivity(e);
                            drawerLayout.closeDrawers();

                        } else {
                            Toast.makeText(MainActivity.this, R.string.plzLogIn, Toast.LENGTH_SHORT).show();
                        }

                        break;


                    case R.id.nav_send_pic:

                        if (session.isUserLoggedIn()) {

                            Intent f = new Intent(MainActivity.this, ImageSendActivity.class);
                            f.putExtra("TITLE", getString(R.string.send_pic));
                            startActivity(f);
                            drawerLayout.closeDrawers();

                        } else {
                            Toast.makeText(MainActivity.this, R.string.plzLogIn, Toast.LENGTH_SHORT).show();
                        }

                        break;


                    case R.id.nav_share_app:

                        if (session.isUserLoggedIn()) {

                            Intent g = new Intent(MainActivity.this, SharedActivity.class);
                            g.putExtra("TITLE", getString(R.string.shared));
                            startActivity(g);
                            drawerLayout.closeDrawers();

                        } else {
                            Toast.makeText(MainActivity.this, R.string.plzLogIn, Toast.LENGTH_SHORT).show();
                        }

                        break;


                    case R.id.nav_support:

                        Intent h = new Intent(MainActivity.this, SupportActivity.class);
                        h.putExtra("TITLE", getString(R.string.support));
                        startActivity(h);
                        drawerLayout.closeDrawers();

                        break;


                    case R.id.nav_ARM:

                        Intent l = new Intent(MainActivity.this, ARMActivity.class);
                        l.putExtra("TITLE", getString(R.string.support));
                        startActivity(l);
                        drawerLayout.closeDrawers();

                        break;


                    case R.id.nav_logout:


                        if (session.isUserLoggedIn()) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            // Creates textview for centre title
                            TextView myMsg = new TextView(MainActivity.this);
                            myMsg.setText("هشدار!");
                            myMsg.setGravity(Gravity.RIGHT);
                            myMsg.setPadding(10, 20, 50, 0);

                            myMsg.setTextSize(20);
                            myMsg.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorAccent)));
                            //set custom title
                            builder.setCustomTitle(myMsg);
                            builder.setMessage("آیا برای خروج از حساب کاربری خود مطمئن هستید؟ ");
                            builder.setPositiveButton("خیر", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setNegativeButton("بله", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    session.logoutUser();
                                    enter.setText(R.string.LoginOrRegister);
                                    profilePicture.setImageResource(R.drawable.login_chef);
                                    editProfilePen.setVisibility(View.GONE);
                                    nameRelative.setVisibility(View.GONE);

                                    defaultStart();

                                    FiveStar.setVisibility(View.GONE);
                                    FourStar.setVisibility(View.GONE);
                                    ThreeStar.setVisibility(View.GONE);
                                    TwoStar.setVisibility(View.GONE);
                                    OneStar.setVisibility(View.GONE);
                                    ZeroStar.setVisibility(View.GONE);

                                    Toast.makeText(MainActivity.this,
                                            "شما از حساب کاربری خود خارج شدید!",
                                            Toast.LENGTH_LONG).show();

                                    hideItem();
                                    drawerLayout.closeDrawer(GravityCompat.START);

                                }

                            });

                            builder.show();
                        }

                        break;
                }

                return true;
            }

        });

        frameLayout = (ViewGroup) findViewById(R.id.frameLayout);
        fragment1 = new BbSearchFragment();
        fragment2 = new BbGalleryFragment();
        fragment3 = new BbHomeFragment();
        fragment4 = new BbRecipesFragment();
        fragment5 = new BbDesignFragment();

        bottomBar = (BottomNavigationView) findViewById(R.id.navigation);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = item -> {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

//            transaction.add(R.id.frameLayout, fragment1);
//            transaction.commit();


            switch (item.getItemId()) {


                case R.id.tab_home:

                    transaction.replace(R.id.frameLayout, fragment3);
                    transaction.commit();

                    break;

                case R.id.tab_search:

                    transaction.replace(R.id.frameLayout, fragment1);
                    transaction.commit();

                    break;


                case R.id.tab_image:
                    transaction.replace(R.id.frameLayout, fragment2);
                    transaction.commit();
                    break;


                case R.id.tab_rec:

                    transaction.replace(R.id.frameLayout, fragment4);
                    transaction.commit();
                    break;


                case R.id.tab_info:
                    transaction.replace(R.id.frameLayout, fragment5);
                    transaction.commit();
                    break;
            }

            return true;

        };

        bottomBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        bottomBar.setSelectedItemId(R.id.tab_home);
        BottomNavigationViewHelper.disableShiftMode(bottomBar);

        session = new UserSessionManager(getApplicationContext());

        View header = navigationView.inflateHeaderView(R.layout.navigation_drawer_header);

        profilePicture = (ImageView) header.findViewById(R.id.navigation_Image);
        editProfilePen = (ImageButton) header.findViewById(R.id.navigation_editButton);
        enter = (TextView) header.findViewById(R.id.navigation_name);
        nameRelative = (RelativeLayout) header.findViewById(R.id.navigation_scoreRelative);

        FiveStar = (LinearLayout) header.findViewById(R.id.fiveStar);
        FourStar = (LinearLayout) header.findViewById(R.id.fourStar);
        ThreeStar = (LinearLayout) header.findViewById(R.id.threeStar);
        TwoStar = (LinearLayout) header.findViewById(R.id.twoStar);
        OneStar = (LinearLayout) header.findViewById(R.id.oneStar);
        ZeroStar = (LinearLayout) header.findViewById(R.id.zeroStar);

        HashMap<String, String> user = session.getUserDetails();

        String id = user.get(UserSessionManager.KEY_ID_USER);

        if (!(session.isUserLoggedIn())) {

            profilePicture.setImageResource(R.drawable.login_chef);
            editProfilePen.setVisibility(View.GONE);
            nameRelative.setVisibility(View.GONE);
            FiveStar.setVisibility(View.GONE);
            FourStar.setVisibility(View.GONE);
            ThreeStar.setVisibility(View.GONE);
            TwoStar.setVisibility(View.GONE);
            OneStar.setVisibility(View.GONE);
            ZeroStar.setVisibility(View.GONE);
        }


        follower = (TextView) header.findViewById(R.id.User_follower);
        following = (TextView) header.findViewById(R.id.User_following);
        Score_tx = (TextView) header.findViewById(R.id.User_Score);

        follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, FollowerActivity.class);
                startActivity(i);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, FollowingActivity.class);
                startActivity(i);
            }
        });

        if (HttpManager.checkNetwork(MainActivity.this)) {

            if (session.isUserLoggedIn()) {

                GetNumbers getNumbers = new GetNumbers(this);
                getNumbers.execute(id);

            } else {

            }
        }

        if (HttpManager.checkNetwork(MainActivity.this)) {

            editProfilePen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Clear the User session data
                    // and redirect chef_icon to LoginActivity
                    editProfile();
                }

            });
        } else {
            Toast.makeText(this, R.string.checkNetwork, Toast.LENGTH_SHORT).show();
        }

        HashMap<String, String> User = session.getUserDetails();

        final String username = User.get(UserSessionManager.KEY_NAME);
        final String userImage = User.get(UserSessionManager.KEY_IMAGE);
        final String UserID = User.get(UserSessionManager.KEY_ID_USER);
        final String level = User.get(UserSessionManager.KEY_Level);


        if (session.isUserLoggedIn()) {

            Picasso.with(getApplicationContext()).load(userImage).into(profilePicture);
            loadBitmap(userImage);

            enter.setText(username);

            int IntLevel = Integer.parseInt(level);

            if (IntLevel == 0) {
                ZeroStar.setVisibility(View.VISIBLE);
            } else if (IntLevel == 1) {
                OneStar.setVisibility(View.VISIBLE);
            } else if (IntLevel == 2) {
                TwoStar.setVisibility(View.VISIBLE);
            } else if (IntLevel == 3) {
                ThreeStar.setVisibility(View.VISIBLE);
            } else if (IntLevel == 4) {
                FourStar.setVisibility(View.VISIBLE);
            } else if (IntLevel > 4) {
                FiveStar.setVisibility(View.VISIBLE);
            }

        }

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!session.isUserLoggedIn()) {

                    UserLogin();

                } else {

                    if (HttpManager.checkNetwork(MainActivity.this)) {

                        enter.setText(username);

                        StatusFetch statusFetch = new StatusFetch(MainActivity.this);
                        statusFetch.execute(Integer.valueOf(UserID));

                    } else {

                    }

                }

            }
        });

    }

    private void hideItem() {

        navigationView = (NavigationView) findViewById(R.id.Navigation_View);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_logout).setVisible(false);
    }

    private void showItem() {

        navigationView = (NavigationView) findViewById(R.id.Navigation_View);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_logout).setVisible(true);
    }

    private void editProfile() {


        final Dialog dialog = new Dialog(MainActivity.this, R.style.Dialog);
        dialog.setContentView(R.layout.dialog_edit_profile);
        dialog.show();

        dialogProfileImage = (CircleImageView) dialog.findViewById(R.id.editProfile_ImageView);
        closeEditDialog = (ImageButton) dialog.findViewById(R.id.dialog_closeIcon);
        changeProfileImage = (ImageButton) dialog.findViewById(R.id.editProfile_ImageButton);

        ImageView true_email = (ImageView) dialog.findViewById(R.id.true_email);
        ImageView false_email = (ImageView) dialog.findViewById(R.id.false_email);
        TextView CheckEmail = (TextView) dialog.findViewById(R.id.CheckEmail);
        Button ResentLink = (Button) dialog.findViewById(R.id.ResentLink);

        ///

        ETXprofileName = (EditText) dialog.findViewById(R.id.editProfile_editName);
        TVprofileEmail = (TextView) dialog.findViewById(R.id.editProfile_editEmail);
        ETXprofilePhone = (EditText) dialog.findViewById(R.id.editProfile_PhoneNumber);
        ETXprofileWeight = (EditText) dialog.findViewById(R.id.editProfile_WeightETX);
        ETXprofileLength = (EditText) dialog.findViewById(R.id.editProfile_lengthETX);
        ETXprofilePassword = (EditText) dialog.findViewById(R.id.editProfile_Password);
        ETXprofileNewPassword = (EditText) dialog.findViewById(R.id.editProfile_newPassword);

        editDoneBTN = (Button) dialog.findViewById(R.id.editProfile_doneBTN);
        sugarBox = (CheckBox) dialog.findViewById(R.id.editProfile_sugar_checkBox);
        fatBox = (CheckBox) dialog.findViewById(R.id.editProfile_Fat_checkBox);
        showPassword = (ImageButton) dialog.findViewById(R.id.ic_show_key_Password);
        showNewPassword = (ImageButton) dialog.findViewById(R.id.ic_show_key_NewPassword);

        ETXprofileName.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));
        setTextViewDrawableColor(ETXprofileName, ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));

//        Drawable drawable = ETXprofileName.getBackground(); // get current EditText drawable
//        drawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); // change the drawable color
//
//        if(Build.VERSION.SDK_INT > 16) {
//            ETXprofileName.setBackground(drawable); // set the new drawable to EditText
//        }else{
//            ETXprofileName.setBackgroundDrawable(drawable); // use setBackgroundDrawable because setBackground required API 16
//        }


        TVprofileEmail.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
        setTextViewDrawableColor(TVprofileEmail, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

        ETXprofilePhone.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
        setTextViewDrawableColor(ETXprofilePhone, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

        ETXprofileWeight.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
        setTextViewDrawableColor(ETXprofileWeight, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

        ETXprofileLength.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
        setTextViewDrawableColor(ETXprofileLength, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

        ETXprofilePassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
        setTextViewDrawableColor(ETXprofilePassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

        ETXprofileNewPassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
        setTextViewDrawableColor(ETXprofileNewPassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

        showPassword.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
        setTextViewDrawableColor(ETXprofilePassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

        showNewPassword.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
        setTextViewDrawableColor(ETXprofileNewPassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

        ETXprofileName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                ETXprofileName.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));
                setTextViewDrawableColor(ETXprofileName, ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));

                TVprofileEmail.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(TVprofileEmail, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofilePhone.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofilePhone, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofileWeight.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileWeight, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofileLength.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileLength, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofilePassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofilePassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofileNewPassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileNewPassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                showPassword.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofilePassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                showNewPassword.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileNewPassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

            }
        });

        TVprofileEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                ETXprofileName.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileName, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                TVprofileEmail.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));
                setTextViewDrawableColor(TVprofileEmail, ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));

                ETXprofilePhone.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofilePhone, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofileWeight.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileWeight, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofileLength.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileLength, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofilePassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofilePassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofileNewPassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileNewPassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                showPassword.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofilePassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                showNewPassword.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileNewPassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

            }
        });

        ETXprofilePhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                ETXprofileName.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileName, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                TVprofileEmail.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(TVprofileEmail, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofilePhone.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));
                setTextViewDrawableColor(ETXprofilePhone, ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));

                ETXprofileWeight.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileWeight, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofileLength.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileLength, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofilePassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofilePassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofileNewPassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileNewPassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                showPassword.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofilePassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                showNewPassword.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileNewPassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

            }
        });

        ETXprofileWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                ETXprofileName.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileName, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                TVprofileEmail.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(TVprofileEmail, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofilePhone.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofilePhone, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofileWeight.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));
                setTextViewDrawableColor(ETXprofileWeight, ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));

                ETXprofileLength.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileLength, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofilePassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofilePassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofileNewPassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileNewPassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                showPassword.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofilePassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                showNewPassword.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileNewPassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

            }
        });

        ETXprofileLength.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                ETXprofileName.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileName, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                TVprofileEmail.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(TVprofileEmail, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofilePhone.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofilePhone, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofileWeight.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileWeight, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofileLength.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));
                setTextViewDrawableColor(ETXprofileLength, ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));

                ETXprofilePassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofilePassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofileNewPassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileNewPassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                showPassword.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofilePassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                showNewPassword.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileNewPassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

            }
        });

        ETXprofilePassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                ETXprofileName.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileName, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                TVprofileEmail.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(TVprofileEmail, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofilePhone.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofilePhone, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofileWeight.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileWeight, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofileLength.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileLength, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofilePassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));
                setTextViewDrawableColor(ETXprofilePassword, ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));

                ETXprofileNewPassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileNewPassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                showPassword.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));
                setTextViewDrawableColor(ETXprofilePassword, ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));

                showNewPassword.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileNewPassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
            }
        });

        ETXprofileNewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                ETXprofileName.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileName, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                TVprofileEmail.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(TVprofileEmail, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofilePhone.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofilePhone, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofileWeight.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileWeight, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofileLength.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofileLength, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofilePassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofilePassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                ETXprofileNewPassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));
                setTextViewDrawableColor(ETXprofileNewPassword, ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));

                showPassword.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(ETXprofilePassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                showNewPassword.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));
                setTextViewDrawableColor(ETXprofileNewPassword, ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));

            }
        });

        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        String name = user.get(UserSessionManager.KEY_NAME);
        String email = user.get(UserSessionManager.KEY_EMAIL);
        String image = user.get(UserSessionManager.KEY_IMAGE);
        String tel = (user.get(UserSessionManager.KEY_TEL_USER));
        String length = (user.get(UserSessionManager.KEY_LENGHT_USER));
        String weight = (user.get(UserSessionManager.KEY_WEIGHT_USER));
        String fatt = (user.get(UserSessionManager.KEY_FAT_USER));
        String sugarr = (user.get(UserSessionManager.KEY_SUGAR_USER));
        String Status = user.get(UserSessionManager.KEY_Status);
        String id = user.get(UserSessionManager.KEY_ID_USER);

        if ((HttpManager.checkNetwork(MainActivity.this)) && (session.isUserLoggedIn())) {

            StatusFetch statusFetch = new StatusFetch(MainActivity.this);
            statusFetch.execute(Integer.valueOf(id));
        }

        ETXprofileName.setText(name);
        TVprofileEmail.setText(email);
        ETXprofilePhone.setText(StringManager.convertEnglishNumbersToPersian(tel));
        ETXprofileLength.setText(StringManager.convertEnglishNumbersToPersian(length));
        ETXprofileWeight.setText(StringManager.convertEnglishNumbersToPersian(weight));

        status = Integer.parseInt(Status);

        if (status == 1) {

            true_email.setVisibility(View.VISIBLE);
            false_email.setVisibility(View.GONE);

        } else {

            CheckEmail.setVisibility(View.VISIBLE);
            ResentLink.setVisibility(View.VISIBLE);
        }

        if (HttpManager.checkNetwork(MainActivity.this)) {

            ResentLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    HashMap<String, String> user = session.getUserDetails();
                    String email = user.get(UserSessionManager.KEY_EMAIL);

                    SendValidationEmail sendValidationEmail = new SendValidationEmail(MainActivity.this);
                    sendValidationEmail.execute(email);

                    dialog.dismiss();

                }
            });

        }

        switch (fatt) {
            case "1":
                fatBox.setChecked(true);

                break;
            case "2":
                fatBox.setChecked(false);
                break;
        }
//
        switch (sugarr) {
            case "1":
                sugarBox.setChecked(true);
                break;
            case "2":
                sugarBox.setChecked(false);
                break;
        }

        Picasso.with(MainActivity.this).load(image).into(dialogProfileImage);
        loadBitmap(image);

        /////

        showPassword.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        ETXprofilePassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;

                    case (MotionEvent.ACTION_UP):
                        ETXprofilePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;


                }

                return true;
            }
        });

        showNewPassword.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        ETXprofileNewPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;

                    case (MotionEvent.ACTION_UP):
                        ETXprofileNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;

                }

                return true;
            }
        });


        closeEditDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        changeProfileImage.setOnClickListener(new View.OnClickListener() {

            Button btncam, btngal;


            @Override
            public void onClick(View view) {

                final Dialog dialog1 = new Dialog(MainActivity.this, R.style.Dialog);
                dialog1.setContentView(R.layout.dialog_choose_photo);
                dialog1.show();

                btncam = (Button) dialog1.findViewById(R.id.img_camera);
                btngal = (Button) dialog1.findViewById(R.id.img_gallery);


                btncam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                                image_profile.setImageDrawable(null);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        // start the image capture Intent
                        startActivityForResult(intent, camera);
                        dialog1.dismiss();
                    }
                });

                btngal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                                image_profile.setImageDrawable(null);
                        Intent galleryPickerIntent = new Intent();
                        galleryPickerIntent.setType("image/*");
                        galleryPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(galleryPickerIntent, "انتخاب تصویر"), gallery);
                        dialog1.dismiss();
                    }
                });
            }

        });

        get_fat = fatt;
        get_sugar = sugarr;


        fatBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (!fatBox.isChecked()) {

                    get_fat = "2";
                    Toast.makeText(MainActivity.this, "isNOTChecked", Toast.LENGTH_SHORT).show();

                } else {

                    get_fat = "1";
                    Toast.makeText(MainActivity.this, "isChecked", Toast.LENGTH_SHORT).show();

                }

            }
        });


        sugarBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {

                    get_sugar = "2";

                } else {

                    get_sugar = "1";
                }
            }
        });


        editDoneBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                get_name = ETXprofileName.getText().toString();
                get_curPass = ETXprofilePassword.getText().toString();
                get_newPass = ETXprofileNewPassword.getText().toString();
                get_telephone = ETXprofilePhone.getText().toString();
                get_length = ETXprofileLength.getText().toString();
                get_weight = ETXprofileWeight.getText().toString();
                get_email = TVprofileEmail.getText().toString();

                session = new UserSessionManager(getApplicationContext());
                HashMap<String, String> user = session.getUserDetails();

                String pass = user.get(UserSessionManager.KEY_PASS);

                if (!(pass.equals(md5(get_curPass)))) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    // Creates textview for centre title
                    TextView myMsg = new TextView(MainActivity.this);
                    myMsg.setText("خطا!");
                    myMsg.setGravity(Gravity.START);
                    myMsg.setPadding(10, 20, 50, 0);

                    myMsg.setTextSize(20);
                    myMsg.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorAccent)));
                    //set custom title
                    builder.setCustomTitle(myMsg);
                    builder.setMessage("رمز عبور فعلی را صحیح وارد کنید ");
                    builder.setPositiveButton("بستن", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            ETXprofilePassword.setText("");
                        }
                    });

                    AlertDialog dialog = builder.show();
                    //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);

                } else {

                    if (HttpManager.checkNetwork(MainActivity.this)) {

                        EditProfile_AsyncTask ui = new EditProfile_AsyncTask(MainActivity.this);
                        ui.execute(bitmap);
                        dialog.dismiss();

                        UserFetch userFetch = new UserFetch(MainActivity.this);
                        userFetch.execute(get_email, get_curPass);

                    } else {
                        Toast.makeText(MainActivity.this, R.string.Disconnect, Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

    }

    class EditProfile_AsyncTask extends AsyncTask<Bitmap, Void, String> {

        //        private ProgressDialog loading;
        private Context context;

        public EditProfile_AsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading = ProgressDialog.show(context, "PLZ Wait", "PLZ Wait...", true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //Toast.makeText(context, s, Toast.LENGTH_LONG).show();
//            loading.dismiss();

            Toast.makeText(MainActivity.this, "اطلاعات با موفقیت ثبت شد", Toast.LENGTH_LONG).show();

        }

        @Override
        protected String doInBackground(Bitmap... params) {
            Bitmap bitmapP = params[0];

//            String uploadImage = null;
//            try {
//                uploadImage = getStringImage(bitmapP);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            UserSessionManager session = new UserSessionManager(context);

            HashMap<String, String> user = session.getUserDetails();
            String IdUser = user.get(UserSessionManager.KEY_ID_USER);
            String name = user.get(UserSessionManager.KEY_NAME);
            String email = user.get(UserSessionManager.KEY_EMAIL);
            String image = user.get(UserSessionManager.KEY_IMAGE);
            String password = user.get(UserSessionManager.KEY_PASS);
            String tel = user.get(UserSessionManager.KEY_TEL_USER);
            String length = user.get(UserSessionManager.KEY_LENGHT_USER);
            String weight = user.get(UserSessionManager.KEY_WEIGHT_USER);
            String fat = user.get(UserSessionManager.KEY_FAT_USER);
            String sugar = user.get(UserSessionManager.KEY_SUGAR_USER);

            ImageUploadHandler rh = new ImageUploadHandler();

            HashMap<String, String> data = new HashMap<>();

            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            if ((!(bitmap == null))) {
                data.put("image", String.valueOf(bitmap));
                try {
                    String uploadImage = null;
                    uploadImage = getStringImage(bitmapP);
                    data.put(UPLOAD_KEY, uploadImage);

                    String get_image = URL_ProfileImages + IdUser + timeStamp + ".jpg";

                    //  session.setImage(get_image);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            data.put("Id_User", IdUser);

            if ((!(name.equals(get_name)))) {
                data.put("Name", get_name);
//                session.setUser(get_name);
            }

            if ((!(password.equals(get_newPass))) && (!(get_newPass.equals("")))) {
                data.put("Password",md5(get_newPass));
//                session.setPass(get_newPass);
            }

            if ((!(tel.equals(get_telephone)))) {
                data.put("UserTel", get_telephone);
//                session.setUserTel(Integer.valueOf(get_telephone));
            }

            if ((!(length.equals(get_length)))) {
                data.put("UserLenght", get_length);
//                session.setUserLenght(Integer.valueOf(get_lenght));
            }

            if ((!(weight.equals(get_weight)))) {
                data.put("UserWeight", get_weight);
//                session.setUserWeight(Integer.valueOf(get_weight));
            }

            if ((!(fat.equals(get_fat)))) {
                data.put("UserFat", get_fat);
//                session.setUserFat(get_fat);
            }

            if ((!(sugar.equals(get_sugar)))) {
                data.put("UserSugar", get_sugar);
//                session.setUserSugar(get_sugar);
            }


            session.logoutUser();

            String result = rh.sendPostRequest(UPLOAD_URL, data);
            return result;

        }
    }

    //متدی برای تعیین وضعیت آیکن منوی کشویی
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_slide_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {


        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true);
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "برای خروج  کلید بازگشت را فشار دهید", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    public void loadBitmap(String url) {

        if (loadtarget == null) loadtarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // do something with the Bitmap
                handleLoadedBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        Picasso.with(this).load(url).into(loadtarget);
    }

    private void UserLogin() {

        final Dialog LogDialog = new Dialog(MainActivity.this, R.style.Dialog);
        LogDialog.setContentView(R.layout.dialog_login_register);
        LogDialog.show();

        registerName = (EditText) LogDialog.findViewById(R.id.login_name);
        registerEmail = (EditText) LogDialog.findViewById(R.id.login_email);
        registerPassword = (EditText) LogDialog.findViewById(R.id.login_password);
        showLoginPass = (ImageButton) LogDialog.findViewById(R.id.loginShowPassword);

        regisrerRule = (CheckBox) LogDialog.findViewById(R.id.login_rules_checkBox);
        registerRuleTV = (TextView) LogDialog.findViewById(R.id.login_rules_textView);
        loginButton = (Button) LogDialog.findViewById(R.id.login_button);
        forgetPassword = (TextView) LogDialog.findViewById(R.id.login_forgetPassword);
        registerTV = (TextView) LogDialog.findViewById(R.id.register);
        loginTV = (TextView) LogDialog.findViewById(R.id.login);
        rulesLayout = (LinearLayout) LogDialog.findViewById(R.id.login_rules_layout);
        dialogClose = (ImageButton) LogDialog.findViewById(R.id.login_closeIcon);

        changeColor();

        registerName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                registerName.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));
                setTextViewDrawableColor(registerName, ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));

                registerEmail.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(registerEmail, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                registerPassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(registerPassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                showLoginPass.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(registerPassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
            }
        });

        registerEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                registerName.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(registerName, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                registerEmail.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));
                setTextViewDrawableColor(registerEmail, ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));

                registerPassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(registerPassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                showLoginPass.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(registerPassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
            }
        });

        registerPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                registerName.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(registerName, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                registerEmail.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(registerEmail, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

                registerPassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));
                setTextViewDrawableColor(registerPassword, ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));

                showLoginPass.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));
                setTextViewDrawableColor(registerPassword, ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));
            }
        });

        registerRuleTV.setPaintFlags(registerRuleTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        registerRuleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this, R.style.Dialog);
                dialog.setContentView(R.layout.dialog_rules_register);
                dialog.show();

                btn_rules = (Button) dialog.findViewById(R.id.close_rules);

                btn_rules.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();

                    }
                });
            }
        });

        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogDialog.dismiss();
            }
        });

        showLoginPass.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        registerPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;

                    case (MotionEvent.ACTION_UP):
                        registerPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;


                }

                return true;
            }
        });


        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StateLogin = true;

                loginTV.setTextColor(getResources().getColor(R.color.colorPrimary));
                registerTV.setTextColor(getResources().getColor(R.color.dark_gray));

                registerName.setVisibility(View.GONE);
                rulesLayout.setVisibility(View.GONE);
                forgetPassword.setVisibility(View.VISIBLE);

                loginButton.setText(R.string.enter);

            }
        });

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StateLogin = false;

                registerTV.setTextColor(getResources().getColor(R.color.colorPrimary));
                loginTV.setTextColor(getResources().getColor(R.color.dark_gray));
                registerName.setVisibility(View.VISIBLE);
                rulesLayout.setVisibility(View.VISIBLE);
                forgetPassword.setVisibility(View.GONE);

                loginButton.setText(R.string.register);

            }
        });

        if (HttpManager.checkNetwork(MainActivity.this)) {

            forgetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String email = registerEmail.getText().toString();

                    if (!(email.equals(""))) {

                        PasswordRecovery password = new PasswordRecovery(MainActivity.this);
                        password.execute(email);

                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        // Creates textview for centre title
                        TextView myMsg = new TextView(MainActivity.this);
                        myMsg.setText("خطا!");
                        myMsg.setGravity(Gravity.RIGHT);
                        myMsg.setPadding(10, 20, 50, 0);

                        myMsg.setTextSize(20);
                        myMsg.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorAccent)));
                        //set custom title
                        builder.setCustomTitle(myMsg);
                        builder.setMessage("لطفا آدرس ایمیل خود را وارد کنید!");
                        builder.setPositiveButton("بستن", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.show();
                        //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);

                    }

                }
            });

        } else {
            Toast.makeText(this, R.string.plzLogIn, Toast.LENGTH_SHORT).show();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (StateLogin) {

                    String user = registerEmail.getText().toString();
                    String pass = registerPassword.getText().toString();

                    if (pass.length() < 8 && pass.length() > 0) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        // Creates textview for centre title
                        TextView myMsg = new TextView(MainActivity.this);
                        myMsg.setText("هشدار");
                        myMsg.setGravity(Gravity.RIGHT);
                        myMsg.setPadding(10, 20, 50, 0);

                        myMsg.setTextSize(20);
                        myMsg.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorAccent)));
                        //set custom title
                        builder.setCustomTitle(myMsg);
                        builder.setMessage("تعداد کاراکترهای مجاز برای رمز حساب کاربری شما، 8 عدد است!");
                        builder.setPositiveButton("بستن", null);
                        AlertDialog dialog = builder.show();
                        //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);

                    } else if (registerEmail.getText().toString().equals("") || registerTV.getText().toString().equals("")
                            ) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        // Creates textview for centre title
                        TextView myMsg = new TextView(MainActivity.this);
                        myMsg.setText("خطا!");
                        myMsg.setGravity(Gravity.RIGHT);
                        myMsg.setPadding(10, 20, 50, 0);

                        myMsg.setTextSize(20);
                        myMsg.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorAccent)));
                        //set custom title
                        builder.setCustomTitle(myMsg);
                        builder.setMessage("لطفا همه ی فیلدها را پر کنید");
                        builder.setPositiveButton("بستن", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.show();
                        //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);
//
                    } else {

                        if (HttpManager.checkNetwork(MainActivity.this)) {

                            UserFetch userFetch = new UserFetch(MainActivity.this);
                            userFetch.execute(user, pass);

                            LogDialog.dismiss();

                        } else {

                            Toast.makeText(MainActivity.this, R.string.Disconnect, Toast.LENGTH_SHORT).show();
                        }

                    }

                } else {

                    if (registerName.getText().toString().equals("") || registerEmail.getText().toString().equals("")
                            || registerPassword.getText().toString().equals("")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        // Creates textview for centre title
                        TextView myMsg = new TextView(MainActivity.this);
                        myMsg.setText("خطا!");
                        myMsg.setGravity(Gravity.RIGHT);
                        myMsg.setPadding(10, 20, 50, 0);

                        myMsg.setTextSize(20);
                        myMsg.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorAccent)));
                        //set custom title
                        builder.setCustomTitle(myMsg);
                        builder.setMessage("لطفا همه ی فیلدها را پر کنید");
                        builder.setPositiveButton("بستن", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.show();
                        //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);

                    } else if (!(regisrerRule.isChecked())) {
                        Toast.makeText(MainActivity.this, "لطفا قوانین را مطالعه فرمائید", Toast.LENGTH_LONG).show();
                    } else {
                        if (HttpManager.checkNetwork(MainActivity.this)) {


                            if (isEmailValid(registerEmail.getText().toString())) {

                                registerUser registerUser = new registerUser(MainActivity.this);
                                registerUser.execute(registerEmail.getText().toString(), registerPassword.getText().toString(),
                                        registerName.getText().toString());


                            } else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                // Creates textview for centre title
                                TextView myMsg = new TextView(MainActivity.this);
                                myMsg.setText("خطا!");
                                myMsg.setGravity(Gravity.RIGHT);
                                myMsg.setPadding(10, 20, 50, 0);

                                myMsg.setTextSize(20);
                                myMsg.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorAccent)));
                                //set custom title
                                builder.setCustomTitle(myMsg);
                                builder.setMessage("لطفا یک ایمیل صحیح وارد نمایید ");
                                builder.setPositiveButton("بستن", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        registerEmail.setText("");

                                    }
                                });
                                AlertDialog dialog = builder.show();
                                //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);
//
                            }


                        } else {

                            Toast.makeText(MainActivity.this, R.string.Disconnect, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case gallery:
                    uri = data.getData();
                    CropImage.activity(uri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .setActivityTitle("ویرایش")
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .setRequestedSize(1000, 1000)
                            .start(this);
                    break;
                case camera:
                    CropImage.activity(fileUri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .setActivityTitle("ویرایش")
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .setRequestedSize(1000, 1000)
                            .start(this);
                    break;
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dialogProfileImage.setImageBitmap(bitmap);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        String IMAGE_DIRECTORY_NAME = "Chili Foods";

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public String getStringImage(Bitmap bmp) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp = Bitmap.createScaledBitmap(bmp, 500, 500, true);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private class registerUser extends AsyncTask<String, String, Integer> {

        Context context;
        AlertDialog.Builder builder;

        private ProgressDialog progress;

        private int response;

        public registerUser(Context context) {

            this.context = context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = ProgressDialog.show(context, "درحال بررسی اطلاعات",
                    "لطفا صبر کنید ...", true);

        }

        @Override
        protected void onPostExecute(Integer result) {

            super.onPostExecute(result);

            response = result;

            progress.dismiss();

            if (response == 0) {


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                // Creates textview for centre title
                TextView myMsg = new TextView(MainActivity.this);
                myMsg.setText("ثبت نام");
                myMsg.setGravity(Gravity.RIGHT);
                myMsg.setPadding(10, 20, 50, 0);

                myMsg.setTextSize(20);
                myMsg.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorAccent)));
                //set custom title
                builder.setCustomTitle(myMsg);
                builder.setMessage("این ایمیل تکراری است");
                builder.setPositiveButton("بستن", null);
                AlertDialog dialog = builder.show();
                //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);

            } else if (response == 1) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Creates textview for centre title
                TextView myMsg = new TextView(context);
                myMsg.setText("ثبت نام");
                myMsg.setGravity(Gravity.RIGHT);
                myMsg.setPadding(10, 20, 50, 0);

                myMsg.setTextSize(20);
                myMsg.setTextColor(ContextCompat.getColor(context, (R.color.colorAccent)));
                //set custom title
                builder.setCustomTitle(myMsg);
                builder.setMessage("ثبت نام با موفقیت انجام شد");
                builder.setPositiveButton("باشه", null);
                AlertDialog dialog = builder.show();

                registerName.setText("");
                registerEmail.setText("");
                registerPassword.setText("");

                //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);

            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                // Creates textview for centre title
                TextView myMsg = new TextView(MainActivity.this);
                myMsg.setText("خطا!");
                myMsg.setGravity(Gravity.RIGHT);
                myMsg.setPadding(10, 20, 50, 0);

                myMsg.setTextSize(20);
                myMsg.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorAccent)));
                //set custom title
                builder.setCustomTitle(myMsg);
                builder.setMessage("خطا در ثبت نام");
                builder.setPositiveButton("بستن", null);
                AlertDialog dialog = builder.show();
                //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);

            }


        }

        @Override
        protected Integer doInBackground(String... strings) {

            try {
                return HttpManager.registerUser(strings[0], strings[1], strings[2]);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class UserFetch extends AsyncTask<String, String, UserModel> {

        Context context;

        private UserModel userModel;

        private int ID = 1;

        public UserFetch(Context context) {

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            ProgressDialog dialog1 = ProgressDialog.show(MainActivity.this, "", "Loading...", true);
//            dialog1.dismiss();

        }

        @Override
        protected void onPostExecute(UserModel result) {

            super.onPostExecute(result);


            userModel = result;

            userModel.getUsername();
            userModel.getPass();
            userModel.getPic_Url();
            userModel.getName();


            if (userModel.isSuccess()) {

                showItem();

                String name, pass, email, image, fat, sugar, Prompted;
                int tel, length, weight, Status, Level;

                name = userModel.getName();
                pass = userModel.getPass();
                email = userModel.getUsername();
                image = userModel.getPic_Url();
                tel = userModel.getTelephone();
                length = userModel.getLenght();
                weight = userModel.getWeight();
                fat = userModel.getFat();
                sugar = userModel.getSugar();
                Prompted = userModel.getPrompted();
                Status = userModel.getStatus();
                Level = userModel.getLevel();
                final int IdUser = userModel.getId_user();

                session.createUserLoginSession(name, pass, email, image, IdUser, tel, length, weight, fat, sugar, Prompted, Status, Level);

//
//                GetNumbers getNumbers = new GetNumbers(MainActivity.this);
//                getNumbers.execute(String.valueOf(userModel.getId_user()));
                int score = userModel.getScore();
                int nFollowing = userModel.getnFollowing();
                int nFollower = userModel.getnFollower();

                follower.setText(StringManager.convertEnglishNumbersToPersian(Integer.toString(nFollower)));
                following.setText(StringManager.convertEnglishNumbersToPersian(Integer.toString(nFollowing)));
                Score_tx.setText(StringManager.convertEnglishNumbersToPersian(Integer.toString(score)));


                Picasso.with(getApplicationContext()).load(userModel.getPic_Url()).placeholder(R.drawable.login_chef).into(profilePicture);
                enter.setText(userModel.getName());
                nameRelative.setVisibility(View.VISIBLE);
                editProfilePen.setVisibility(View.VISIBLE);


                int myLevel = userModel.getLevel();

                if (myLevel == 0) {
                    ZeroStar.setVisibility(View.VISIBLE);
                } else if (myLevel == 1) {
                    OneStar.setVisibility(View.VISIBLE);
                } else if (myLevel == 2) {
                    TwoStar.setVisibility(View.VISIBLE);
                } else if (myLevel == 3) {
                    ThreeStar.setVisibility(View.VISIBLE);
                } else if (myLevel == 4) {
                    FourStar.setVisibility(View.VISIBLE);
                } else if (myLevel > 4) {
                    FiveStar.setVisibility(View.VISIBLE);
                }

                defaultStart();

                enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (session.isUserLoggedIn()) {
                            Intent intent = new Intent(MainActivity.this, SharedActivity.class);
                            intent.putExtra("Offer", "0");
                            startActivity(intent);
                        } else {

                            UserLogin();

                        }
                    }
                });

                if (session.isUserLoggedIn()) {

                    profilePicture.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, SharedActivity.class);
                            intent.putExtra("Offer", "0");
                            startActivity(intent);
                        }
                    });


                }


                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyyMMdd");

                String strDate = mdformat.format(calendar.getTime());

                String year = Prompted.substring(0, 4);
                String month = Prompted.substring(5, 7);
                String day = Prompted.substring(8, 10);

                String temp = year + month + day;

                int editedPrompted = Integer.parseInt(temp);

                int CurrentTime = Integer.valueOf(strDate);

                VisitTime = ((CurrentTime - editedPrompted));


                if (((tel == 0) || (length == 0) || (weight == 0) || (fat.equals(0)) || (sugar.equals(0))) && (VisitTime > 7)) {


                    setCurrentTime time = new setCurrentTime(MainActivity.this);
                    time.execute(String.valueOf(IdUser));


                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    // Creates textview for centre title
                    TextView myMsg = new TextView(MainActivity.this);
                    myMsg.setText("پیشنهاد");
                    myMsg.setGravity(Gravity.RIGHT);
                    myMsg.setPadding(10, 20, 50, 0);

                    myMsg.setTextSize(20);
                    myMsg.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorAccent)));
                    //set custom title
                    builder.setCustomTitle(myMsg);
                    builder.setMessage("با وارد کردن همه اطلاعات امتیاز بیشتری بگیرید");
                    builder.setPositiveButton("بعدا", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("باشه", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editProfile();

                        }

                    });
                    AlertDialog dialog = builder.show();
                    //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);

                } else {

                }
            } else {


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                // Creates textview for centre title
                TextView myMsg = new TextView(MainActivity.this);
                myMsg.setText("هشدار");
                myMsg.setGravity(Gravity.RIGHT);
                myMsg.setPadding(10, 20, 50, 0);

                myMsg.setTextSize(20);
                myMsg.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorAccent)));
                //set custom title
                builder.setCustomTitle(myMsg);
                builder.setMessage("لطفا با اطلاعات صحیح وارد شوید");
                builder.setPositiveButton("بستن", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        profilePicture.setImageResource(R.drawable.login_chef);
                        editProfilePen.setVisibility(View.GONE);
                        nameRelative.setVisibility(View.GONE);
                        FiveStar.setVisibility(View.GONE);
                        FourStar.setVisibility(View.GONE);
                        ThreeStar.setVisibility(View.GONE);
                        TwoStar.setVisibility(View.GONE);
                        OneStar.setVisibility(View.GONE);
                        ZeroStar.setVisibility(View.GONE);
                        enter.setText(R.string.LoginOrRegister);
                        enter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                UserLogin();
//
                            }
                        });


                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.show();
                //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);


            }
        }


        @Override
        protected UserModel doInBackground(String... strings) {


            String result = getDataUser(strings[0], strings[1]);

            UserModel um = new JsonParserUsers().parseJson(result);

            return um;

        }

    }

    private class StatusFetch extends AsyncTask<Integer, String, UsersPromptedModel> {

        Context context;
        private UsersPromptedModel usersPromptedModel;
//        private UserModel userModel;

        public StatusFetch(Context context) {

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            if (HttpManager.checkNetwork(MainActivity.this)) {
//
//                Recipes_Frame.setVisibility(View.VISIBLE);
//                testInternet.setVisibility(View.GONE);
//
//            } else {
//
//                TestInternet();
//            }

        }

        @Override
        protected UsersPromptedModel doInBackground(Integer... integers) {

            try {
                String result = HttpManager.getUsersStatus(integers[0]);

                UsersPromptedModel usm = new JsonParserUsersPrompted().parseJson(result);

                return usm;


            } catch (Exception e) {

                return null;
            }

        }

        @Override
        protected void onPostExecute(UsersPromptedModel result) {
//            super.onPostExecute(result);

//            if (result != "" ) {

            usersPromptedModel = result;

            String updateName, updatePass, updateEmail, updateImage, updateFat, updateSugar, Prompted;
            int updateTel, updateLength, updateWeight, updateStatus, updateLevel;

            updateName = usersPromptedModel.getName();
            updatePass = usersPromptedModel.getPass();
            updateEmail = usersPromptedModel.getUsername();
            updateImage = usersPromptedModel.getPic_Url();
            updateTel = usersPromptedModel.getTelephone();
            updateLength = usersPromptedModel.getLenght();
            updateWeight = usersPromptedModel.getWeight();
            updateFat = usersPromptedModel.getFat();
            updateSugar = usersPromptedModel.getSugar();
            updateStatus = usersPromptedModel.getStatus();
            final int updateIdUser = usersPromptedModel.getId_user();
            Prompted = usersPromptedModel.getPrompted();
            updateLevel = usersPromptedModel.getLevel();


            session.createUserLoginSession(updateName, updatePass, updateEmail, updateImage, updateIdUser, updateTel, updateLength, updateWeight, updateFat, updateSugar, Prompted, updateStatus, updateLevel);


            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyyMMdd");

            String strDate = mdformat.format(calendar.getTime());

            String year = Prompted.substring(0, 4);
            String month = Prompted.substring(5, 7);
            String day = Prompted.substring(8, 10);

            String temp = year + month + day;

            int editedPrompted = Integer.parseInt(temp);

            int CurrentTime = Integer.valueOf(strDate);

            VisitTime = ((CurrentTime - editedPrompted));

            HashMap<String, String> User = session.getUserDetails();


            final String UserID = User.get(UserSessionManager.KEY_ID_USER);

            final String fat = User.get(UserSessionManager.KEY_FAT_USER);
            final String sugar = User.get(UserSessionManager.KEY_SUGAR_USER);
            final int weight = Integer.parseInt(User.get(UserSessionManager.KEY_WEIGHT_USER));
            final int length = Integer.parseInt(User.get(UserSessionManager.KEY_LENGHT_USER));
            final int tel = Integer.parseInt(User.get(UserSessionManager.KEY_TEL_USER));


            if (((tel == 0) || (length == 0) || (weight == 0) || (fat.equals(0)) || (sugar.equals(0))) && (VisitTime > 7)) {


                setCurrentTime time = new setCurrentTime(MainActivity.this);
                time.execute(String.valueOf(UserID));

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                // Creates textview for centre title
                TextView myMsg = new TextView(MainActivity.this);
                myMsg.setText("پیشنهاد");
                myMsg.setGravity(Gravity.RIGHT);
                myMsg.setPadding(10, 20, 50, 0);

                myMsg.setTextSize(20);
                myMsg.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorAccent)));
                //set custom title
                builder.setCustomTitle(myMsg);
                builder.setMessage("با وارد کردن همه اطلاعات امتیاز بیشتری بگیرید");
                builder.setPositiveButton("بعدا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("باشه", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editProfile();
                    }

                });
                AlertDialog dialog = builder.show();
                //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);


            }
//            } else if (result == null) {
//
//            } else if (result.equals("[]\n")) {
//            }
        }

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
                Score = numbersModel.getScore();

                follower.setText(StringManager.convertEnglishNumbersToPersian(Integer.toString(nFolowers)));
                following.setText(StringManager.convertEnglishNumbersToPersian(Integer.toString(nFolowings)));
                Score_tx.setText(StringManager.convertEnglishNumbersToPersian(Integer.toString(Score)));

            } else if (result.equals("error") || result.isEmpty()) {

            } else if (result.equals("[]\n")) {
            }
        }
    }

    private class setCurrentTime extends AsyncTask<String, String, String> {

        Context context;


        JSONObject object = new JSONObject();

        public setCurrentTime(Context context) {
            object = new JSONObject();

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... strings) {

            String jsonText = HttpManager.setCurrentTime(Integer.parseInt(strings[0]));

            return jsonText;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


        }


    }

    private class PasswordRecovery extends AsyncTask<String, String, Integer> {

        Context context;


        JSONObject object = new JSONObject();

        public PasswordRecovery(Context context) {
            object = new JSONObject();

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Integer doInBackground(String... strings) {

            int jsonText = HttpManager.RecoveryPassword(strings[0]);

            return jsonText;

        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);


            if (result == 3) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Creates textview for centre title
                TextView myMsg = new TextView(context);
                myMsg.setText("خطا!");
                myMsg.setGravity(Gravity.RIGHT);
                myMsg.setPadding(10, 20, 50, 0);

                myMsg.setTextSize(20);
                myMsg.setTextColor(ContextCompat.getColor(context, (R.color.colorAccent)));
                //set custom title
                builder.setCustomTitle(myMsg);
                builder.setMessage("متاسفیم! ایمیل شما تایید نشده است. با پشتیبانی تماس بگیرید.");
                builder.setPositiveButton("باشه", null);
                AlertDialog dialog = builder.show();
                //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);

            } else if (result == 4) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Creates textview for centre title
                TextView myMsg = new TextView(context);
                myMsg.setText("بازیابی رمز عبور");
                myMsg.setGravity(Gravity.RIGHT);
                myMsg.setPadding(10, 20, 50, 0);

                myMsg.setTextSize(20);
                myMsg.setTextColor(ContextCompat.getColor(context, (R.color.colorAccent)));
                //set custom title
                builder.setCustomTitle(myMsg);
                builder.setMessage("لینک تغییر رمز عبور به ایمیل شما ارسال شد");
                builder.setPositiveButton("باشه", null);
                AlertDialog dialog = builder.show();
                //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);


            } else if (result == 1) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Creates textview for centre title
                TextView myMsg = new TextView(context);
                myMsg.setText("بازیابی رمز عبور");
                myMsg.setGravity(Gravity.RIGHT);
                myMsg.setPadding(10, 20, 50, 0);

                myMsg.setTextSize(20);
                myMsg.setTextColor(ContextCompat.getColor(context, (R.color.colorAccent)));
                //set custom title
                builder.setCustomTitle(myMsg);
                builder.setMessage("کاربری با این آدرس ایمیل وجود ندارد!");
                builder.setPositiveButton("باشه", null);
                AlertDialog dialog = builder.show();
                //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);


            } else if (result == 0) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Creates textview for centre title
                TextView myMsg = new TextView(context);
                myMsg.setText("بازیابی رمز عبور");
                myMsg.setGravity(Gravity.RIGHT);
                myMsg.setPadding(10, 20, 50, 0);

                myMsg.setTextSize(20);
                myMsg.setTextColor(ContextCompat.getColor(context, (R.color.colorAccent)));
                //set custom title
                builder.setCustomTitle(myMsg);
                builder.setMessage("ایمیل وارد شده نامعتبر است.");
                builder.setPositiveButton("باشه", null);
                AlertDialog dialog = builder.show();
                //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);


            } else if (result == 2) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Creates textview for centre title
                TextView myMsg = new TextView(context);
                myMsg.setText("بازیابی رمز عبور");
                myMsg.setGravity(Gravity.RIGHT);
                myMsg.setPadding(10, 20, 50, 0);

                myMsg.setTextSize(20);
                myMsg.setTextColor(ContextCompat.getColor(context, (R.color.colorAccent)));
                //set custom title
                builder.setCustomTitle(myMsg);
                builder.setMessage("خطایی رخ داده است. مجدد تلاش کنید.");
                builder.setPositiveButton("باشه", null);
                AlertDialog dialog = builder.show();
                //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);


            }

        }
    }

    public void handleLoadedBitmap(Bitmap b) {
        // do something here
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private class SendValidationEmail extends AsyncTask<String, String, String> {

        Context context;


        JSONObject object = new JSONObject();

        public SendValidationEmail(Context context) {
            object = new JSONObject();

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... strings) {

            String jsonText = HttpManager.SendValidationEmail(strings[0]);

            return jsonText;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            int res = Integer.parseInt(String.valueOf(result.charAt(0)));

            if (res == 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Creates textview for centre title
                TextView myMsg = new TextView(context);
                myMsg.setText("ارسال مجدد لینک فعالسازی ");
                myMsg.setGravity(Gravity.RIGHT);
                myMsg.setPadding(10, 20, 50, 0);

                myMsg.setTextSize(20);
                myMsg.setTextColor(ContextCompat.getColor(context, (R.color.colorAccent)));
                //set custom title
                builder.setCustomTitle(myMsg);
                builder.setMessage("لینک فعالسازی با موفقیت به آدرس ایمیل شما ارسال گردید.");
                builder.setPositiveButton("باشه", null);
                AlertDialog dialog = builder.show();
                //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);


            } else if (res == 0) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Creates textview for centre title
                TextView myMsg = new TextView(context);
                myMsg.setText("خطا!");
                myMsg.setGravity(Gravity.RIGHT);
                myMsg.setPadding(10, 20, 50, 0);

                myMsg.setTextSize(20);
                myMsg.setTextColor(ContextCompat.getColor(context, (R.color.colorAccent)));
                //set custom title
                builder.setCustomTitle(myMsg);
                builder.setMessage("ارسال لینک با شکست مواجه شده است، لطفا دوباره سعی کنید!");
                builder.setPositiveButton("باشه", null);
                AlertDialog dialog = builder.show();
                //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);


            }

        }


    }

    public void Clear_Cache() {


        try {
            File[] f = getBaseContext().getCacheDir().listFiles();
            for (File file : f) {
                file.delete();
            }

        } catch (Exception e) {
            Log.i("message", "error in clear cache" + e.toString());
        }


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        HashMap<String, String> User = session.getUserDetails();
        final String username = User.get(UserSessionManager.KEY_NAME);
        final String id = User.get(UserSessionManager.KEY_ID_USER);
        final String userImage = User.get(UserSessionManager.KEY_IMAGE);

        if (HttpManager.checkNetwork(MainActivity.this)) {

            if (session.isUserLoggedIn()) {
//
//                Picasso.with(getApplicationContext()).load(userImage).into(profilePicture);
//                loadBitmap(userImage);
//
//                StatusFetch statusFetch = new StatusFetch(MainActivity.this);
//                statusFetch.execute(Integer.valueOf(id));
//
                GetNumbers getNumbers = new GetNumbers(this);
                getNumbers.execute(id);
//
//                enter.setText(username);
//
            }
        } else {

            Toast.makeText(MainActivity.this, R.string.Disconnect, Toast.LENGTH_SHORT).show();

        }


        if (!(session.isUserLoggedIn())) {

            profilePicture.setImageResource(R.drawable.login_chef);
            editProfilePen.setVisibility(View.GONE);
            nameRelative.setVisibility(View.GONE);
            FiveStar.setVisibility(View.GONE);
            FourStar.setVisibility(View.GONE);
            ThreeStar.setVisibility(View.GONE);
            TwoStar.setVisibility(View.GONE);
            OneStar.setVisibility(View.GONE);
            ZeroStar.setVisibility(View.GONE);
        }


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!session.isUserLoggedIn()) {

                    UserLogin();

                } else {

                    enter.setText(username);
                    Intent intent = new Intent(MainActivity.this, SharedActivity.class);
                    intent.putExtra("Offer", "0");
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
            }
        }
    }

    private void changeColor() {

        registerName.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));
        setTextViewDrawableColor(registerName, ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));

        registerEmail.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
        setTextViewDrawableColor(registerEmail, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

        registerPassword.setTextColor(ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));
        setTextViewDrawableColor(registerPassword, ContextCompat.getColor(MainActivity.this, (R.color.light_gray)));

        showLoginPass.setColorFilter(ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));
        setTextViewDrawableColor(registerPassword, ContextCompat.getColor(MainActivity.this, (R.color.colorPrimary)));


    }
    private void defaultStart() {

        bottomBar = (BottomNavigationView) findViewById(R.id.navigation);
        bottomBar.setSelectedItemId(R.id.tab_home);
    }
}
