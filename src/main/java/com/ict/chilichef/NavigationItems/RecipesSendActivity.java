package com.ict.chilichef.NavigationItems;

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
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ict.chilichef.Adapter.Recycler_SendRecipeIng_Adapter;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Manage.ImageUploadHandler;
import com.ict.chilichef.Manage.InputFilterMinMax;
import com.ict.chilichef.Model.SendRecipe_IngredientModel;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class RecipesSendActivity extends AppCompatActivity {

    ImageButton btncam, btngal, back;
    ImageView imageView;
    Toolbar toolbar;

    Bitmap bitmap = null;
    static final int gallery = 20;
    static final int camera = 50;
    Uri fileUri;
    Uri uri;

    private GoogleApiClient client;
    AlertDialog.Builder builder;

    Button addIngredientBtn, sendRecipeBTN;
    EditText Ingredient_Name_ETX, Ingredient_Number_ETX, Ingredient_Unit_ETX, foodTitle, foodRecipe, Time, Number;
    String Ingredient_1, get_food, get_description, get_time, get_number, ing, show_hard;

    private Button btn_rules;
    TextView rules;

    RadioButton radio_easy, radio_med, radio_hard;
    RadioButton radioButton;
    RadioGroup radioGroup;
    CheckBox checkBox;

    private List<SendRecipe_IngredientModel> itemList = new ArrayList<>();
    private RecyclerView Ing_RecyclerView;
    private Recycler_SendRecipeIng_Adapter IngAdapter;
    Context context;

    public static final String UPLOAD_URL = "http://ashpazchili.ir/Chili/Pages/postNewRecipe.php";
    public static final String UPLOAD_KEY = "image";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_send);

        Bundle bundle = getIntent().getExtras();
        final String TITLE = bundle.getString("TITLE");

        toolbar = (Toolbar) findViewById(R.id.Toolbar_Public);
        ((TextView) findViewById(R.id.PublicTitle_toolbar)).setText(TITLE);
        setSupportActionBar(toolbar);

        back = (ImageButton) findViewById(R.id.PublicLeft_icon_id);
        back.setImageResource(R.drawable.ic_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipesSendActivity.this.finish();
            }
        });

        btncam = (ImageButton) findViewById(R.id.image_camera);
        btngal = (ImageButton) findViewById(R.id.image_gallery);
        imageView = (ImageView) findViewById(R.id.show_photo);
        foodTitle = (EditText) findViewById(R.id.recipeSend_foodTitle);
        foodRecipe = (EditText) findViewById(R.id.recipeSend_foodDescription);
        addIngredientBtn = (Button) findViewById(R.id.addIngredientBtn);
        sendRecipeBTN = (Button) findViewById(R.id.sendRecipeBTN);
        Time = (EditText) findViewById(R.id.timeFill);
        Number = (EditText) findViewById(R.id.recipeSend_number);


        radio_easy = (RadioButton) findViewById(R.id.easy);
        radio_med = (RadioButton) findViewById(R.id.med);
        radio_hard = (RadioButton) findViewById(R.id.hard);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group_hard);
        checkBox = (CheckBox) findViewById(R.id.ImageSend_rules_checkBox);

        rules = (TextView) findViewById(R.id.rulesRecipe);
        rules.setPaintFlags(rules.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        foodTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                foodTitle.setTextColor(ContextCompat.getColor(RecipesSendActivity.this, (R.color.colorAccent)));
                setTextViewDrawableColor(foodTitle, ContextCompat.getColor(RecipesSendActivity.this, (R.color.colorAccent)));

                foodRecipe.setTextColor(ContextCompat.getColor(RecipesSendActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(foodRecipe, ContextCompat.getColor(RecipesSendActivity.this, (R.color.light_gray)));

            }
        });

        foodRecipe.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                foodTitle.setTextColor(ContextCompat.getColor(RecipesSendActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(foodTitle, ContextCompat.getColor(RecipesSendActivity.this, (R.color.light_gray)));

                foodRecipe.setTextColor(ContextCompat.getColor(RecipesSendActivity.this, (R.color.colorAccent)));
                setTextViewDrawableColor(foodRecipe, ContextCompat.getColor(RecipesSendActivity.this, (R.color.colorAccent)));

            }
        });

        btncam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, camera);
            }
        });

        btngal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryPickerIntent = new Intent();
                galleryPickerIntent.setType("image/*");
                galleryPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryPickerIntent, "انتخاب تصویر"), gallery);
            }
        });


        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(RecipesSendActivity.this, R.style.Dialog);
                dialog.setContentView(R.layout.dialog_rules_send_recipes);
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


        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        //adding Ingredient

        addIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog AddIng_dialog = new Dialog(RecipesSendActivity.this, R.style.Dialog);
                AddIng_dialog.setContentView(R.layout.dialog_add_ingredient);
                AddIng_dialog.show();

                Ingredient_Name_ETX = (EditText) AddIng_dialog.findViewById(R.id.add_ingredient_Title);
                Ingredient_Number_ETX = (EditText) AddIng_dialog.findViewById(R.id.add_ingredient_number);
                Ingredient_Unit_ETX = (EditText) AddIng_dialog.findViewById(R.id.add_ingredient_unit);

                Button Ing_confirm_button = (Button) AddIng_dialog.findViewById(R.id.add_ingredient_BTN);
                ImageButton Ing_close_button = (ImageButton) AddIng_dialog.findViewById(R.id.add_ingredient_closeIcon);

                Ing_close_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddIng_dialog.dismiss();
                    }
                });
                Ing_confirm_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!(Ingredient_Name_ETX.getText().toString().equals(""))) {

                            Ingredient_1 = (Ingredient_Number_ETX.getText().toString() + " " + Ingredient_Unit_ETX.getText().toString() + " " + Ingredient_Name_ETX.getText().toString());
                            String Number = Ingredient_Number_ETX.getText().toString();
                            String Unit = Ingredient_Unit_ETX.getText().toString();
                            String Name = Ingredient_Name_ETX.getText().toString();


                            Ing_RecyclerView = (RecyclerView) findViewById(R.id.Ingredient_Recycler);

                            SendRecipe_IngredientModel item = new SendRecipe_IngredientModel(Number, Unit, Name);
                            itemList.add(item);

                            IngAdapter = new Recycler_SendRecipeIng_Adapter(itemList, context);
                            IngAdapter.setClickListener(this);
                            RecyclerView.LayoutManager mLayoutManger = new GridLayoutManager(getApplicationContext(), 1);
                            Ing_RecyclerView.setLayoutManager(mLayoutManger);
                            Ing_RecyclerView.setItemAnimator(new DefaultItemAnimator());
                            Ing_RecyclerView.setAdapter(IngAdapter);

                            AddIng_dialog.dismiss();
                        } else {

                        }
                    }
                });
            }
        });

        sendRecipeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ing = "";
                for (int i = 0; i < itemList.size(); i++) {
                    ing = ing + itemList.get(i).toString();

                }

                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);

                switch (radioButton.getText().toString()) {
                    case "آسان":
                        show_hard = "1";
                        break;
                    case "متوسط":
                        show_hard = "2";
                        break;
                    case "سخت":
                        show_hard = "3";
                        break;
                }


                get_food = foodTitle.getText().toString();
                get_description = foodRecipe.getText().toString();
                get_time = Time.getText().toString();
                get_number = Number.getText().toString();

                if (get_food.equals("") || (get_description.equals("")) || (get_time.equals("")) || (get_number.equals("")) || (ing.equals("")) || (bitmap == null)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipesSendActivity.this);
                    // Creates textview for centre title
                    TextView myMsg = new TextView(RecipesSendActivity.this);
                    myMsg.setText("خطا!");
                    myMsg.setGravity(Gravity.RIGHT);
                    myMsg.setPadding(10, 20, 50, 0);

                    myMsg.setTextSize(20);
                    myMsg.setTextColor(ContextCompat.getColor(RecipesSendActivity.this, (R.color.colorAccent)));
                    //set custom title
                    builder.setCustomTitle(myMsg);
                    builder.setMessage("لطفا همه ی فیلدها را پر کنید");
                    builder.setPositiveButton("باشه", null);
                    builder.show();

                } else if (!(checkBox.isChecked())) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipesSendActivity.this);
                    // Creates textview for centre title
                    TextView myMsg = new TextView(RecipesSendActivity.this);
                    myMsg.setText("خطا!");
                    myMsg.setGravity(Gravity.RIGHT);
                    myMsg.setPadding(10, 20, 50, 0);

                    myMsg.setTextSize(20);
                    myMsg.setTextColor(ContextCompat.getColor(RecipesSendActivity.this, (R.color.colorAccent)));
                    //set custom title
                    builder.setCustomTitle(myMsg);
                    builder.setMessage("لطفا موافقت با قوانین را تایید کنید");
                    builder.setPositiveButton("باشه", null);
                    builder.show();

                } else {

                    if (HttpManager.checkNetwork(RecipesSendActivity.this)) {

                        UploadRecipe ui = new UploadRecipe(RecipesSendActivity.this);
                        ui.execute(bitmap);
                    } else {

                        Toast.makeText(RecipesSendActivity.this, R.string.Disconnect, Toast.LENGTH_SHORT).show();
                    }
                }
            }


        });
    }


    class UploadRecipe extends AsyncTask<Bitmap, Void, String> {

        private ProgressDialog loading;
        private Context context;

        public UploadRecipe(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(context, "لطفا صبر کنید", "در حال ارسال اطلاعات...", true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            loading.dismiss();

            Toast.makeText(RecipesSendActivity.this, "دستورپخت شما با موفقیت ارسال شد. بعد از تائید ادمین قابل نمایش خواهد بود", Toast.LENGTH_LONG).show();
            RecipesSendActivity.this.finish();

        }

        @Override
        protected String doInBackground(Bitmap... params) {
            Bitmap bitmapP = params[0];

            String uploadImage = null;
            try {
                uploadImage = getStringImage(bitmapP);
            } catch (IOException e) {
                e.printStackTrace();
            }

            UserSessionManager session = new UserSessionManager(context);

            HashMap<String, String> user = session.getUserDetails();
            String IdUser = user.get(UserSessionManager.KEY_ID_USER);


            ImageUploadHandler rh = new ImageUploadHandler();

            HashMap<String, String> data = new HashMap<>();


            data.put(UPLOAD_KEY, uploadImage);
            data.put("Title", get_food);
            data.put("Recipe", get_description);
            data.put("Time", get_time);
            data.put("Meal", get_number);
            data.put("Ingredients", ing);
            data.put("Difficulty", show_hard);
            data.put("Id_User", IdUser);


            String result = rh.sendPostRequest(UPLOAD_URL, data);

            return result;

        }
    }


    @Override
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
                            .start(RecipesSendActivity.this);
                    break;
                case camera:
                    CropImage.activity(fileUri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .setActivityTitle("ویرایش")
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .setRequestedSize(1000, 1000)
                            .start(RecipesSendActivity.this);
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
                imageView.setImageBitmap(bitmap);
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

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SendImageFood Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }


    @Override
    protected void onStart() {
        super.onStart();

        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onStop() {
        super.onStop();

        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}

