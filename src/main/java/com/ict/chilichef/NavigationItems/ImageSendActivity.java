package com.ict.chilichef.NavigationItems;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ict.chilichef.CommentGalleryActivity;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Manage.ImageUploadGalleryHandler;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;


public class ImageSendActivity extends AppCompatActivity {

    ImageButton btncam, btngal,back;
    Button btnSend;
    ImageView imageView;
    EditText txtname, txtdescription;
    Toolbar toolbar;

    TextView rules;
    private Button btn_rules;
    CheckBox checkBox_rules;

    AlertDialog.Builder builder;

    String name, description;
    UserSessionManager session;
    String IdUser;

    String selectedimagePath;
    Bitmap rotatedBitmap = null;
    Bitmap bitmap = null;
    static final int gallery = 20;
    static final int camera = 50;
    Uri fileUri;
    Uri uri;

    public static final String UPLOAD_URL = "http://ashpazchili.ir/Chili/Pages/postImageToGallery.php";
    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_send);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle bundle = getIntent().getExtras();
        String TITLE = bundle.getString("TITLE");

        toolbar = (Toolbar) findViewById(R.id.Toolbar_Public);
        ((TextView) findViewById(R.id.PublicTitle_toolbar)).setText(TITLE);
        setSupportActionBar(toolbar);

        back = (ImageButton) findViewById(R.id.PublicLeft_icon_id);
        back.setImageResource(R.drawable.ic_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageSendActivity.this.finish();
            }
        });


        btncam = (ImageButton) findViewById(R.id.image_send_cam);
        btngal = (ImageButton) findViewById(R.id.image_send_gal);
        btnSend = (Button) findViewById(R.id.ImageSend_addImage_btn);
        imageView = (ImageView) findViewById(R.id.image_send_photo);

        txtname = (EditText) findViewById(R.id.image_send_name);
        txtdescription = (EditText) findViewById(R.id.image_send_description);

//        txtname.requestFocus();

        rules = (TextView) findViewById(R.id.ImageSend_rules_textView);
        rules.setPaintFlags(rules.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        checkBox_rules = (CheckBox) findViewById(R.id.ImageSend_rules_checkBox);

        txtname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                txtname.setTextColor(ContextCompat.getColor(ImageSendActivity.this, (R.color.colorAccent)));
                setTextViewDrawableColor(txtname, ContextCompat.getColor(ImageSendActivity.this, (R.color.colorAccent)));

                txtdescription.setTextColor(ContextCompat.getColor(ImageSendActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(txtdescription, ContextCompat.getColor(ImageSendActivity.this, (R.color.light_gray)));

            }
        });

        txtdescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                txtname.setTextColor(ContextCompat.getColor(ImageSendActivity.this, (R.color.light_gray)));
                setTextViewDrawableColor(txtname, ContextCompat.getColor(ImageSendActivity.this, (R.color.light_gray)));

                txtdescription.setTextColor(ContextCompat.getColor(ImageSendActivity.this, (R.color.colorAccent)));
                setTextViewDrawableColor(txtdescription, ContextCompat.getColor(ImageSendActivity.this, (R.color.colorAccent)));

            }
        });

        session = new UserSessionManager(getApplicationContext());

        HashMap<String, String> User = session.getUserDetails();

        IdUser = User.get(UserSessionManager.KEY_ID_USER);


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

                final Dialog dialog = new Dialog(ImageSendActivity.this, R.style.Dialog);
                dialog.setContentView(R.layout.dialog_rules_send_image);
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

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = txtname.getText().toString();
                description = txtdescription.getText().toString();

                if (name.equals("") || description.equals("") || bitmap == null  ) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ImageSendActivity.this);
                    // Creates textview for centre title
                    TextView myMsg = new TextView(ImageSendActivity.this);
                    myMsg.setText("خطا!");
                    myMsg.setGravity(Gravity.RIGHT);
                    myMsg.setPadding(10,20,50,0);

                    myMsg.setTextSize(20);
                    myMsg.setTextColor(ContextCompat.getColor(ImageSendActivity.this, (R.color.colorAccent)));
                    //set custom title
                    builder.setCustomTitle(myMsg);
                    builder.setMessage("لطفا همه ی فیلدها را پر کنید");
                    builder.setPositiveButton("باشه", null);
                    builder.show();

                } else {

                    if (!(checkBox_rules.isChecked())){
                        Toast.makeText(ImageSendActivity.this, "لطفا قوانین را مطالعه فرمائید", Toast.LENGTH_LONG).show();
                    }else {

                        if(HttpManager.checkNetwork(ImageSendActivity.this)){

                            UploadImage ui = new UploadImage(ImageSendActivity.this);
                            ui.execute(bitmap);
                        }else {

                            Toast.makeText(ImageSendActivity.this, R.string.Disconnect, Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
                            .start(ImageSendActivity.this);
                    break;
                case camera:
                    CropImage.activity(fileUri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .setActivityTitle("ویرایش")
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .setRequestedSize(1000, 1000)
                            .start(ImageSendActivity.this);
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

    class UploadImage extends AsyncTask<Bitmap, Void, String> {

        private ProgressDialog loading;
        private Context context;

        public UploadImage(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(context, "بارگذاری عکس", "لطفا صبر کنید. در حال ارسال...", true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            loading.dismiss();
            Toast.makeText(ImageSendActivity.this, "عکس و توضیحات شما با موفقیت ارسال شد", Toast.LENGTH_LONG).show();
            ImageSendActivity.this.finish();
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

            ImageUploadGalleryHandler rh = new ImageUploadGalleryHandler();

            HashMap<String, String> data = new HashMap<>();
            data.put(UPLOAD_KEY, uploadImage);
            data.put("Id_User", IdUser);
            data.put("Name", name);
            data.put("Description", description);

            String result = rh.sendPostRequest(UPLOAD_URL, data);

            return result;
        }
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
}
