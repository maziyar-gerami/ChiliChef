package com.ict.chilichef;

import android.graphics.Path;
import android.graphics.drawable.shapes.PathShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ict.chilichef.Manage.DBFetch;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Manage.IngFetch;

import org.json.JSONException;

import java.io.File;
import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.ict.chilichef.MainActivity.URL_PAGES;

public class SplashActivity extends AppCompatActivity {


    TextView random_text;
    String filePath;
    ImageButton retryBTN;

    private RelativeLayout relProgress;
    boolean retry = false;
    LinearLayout checkNetLayout;

    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        random_text = (TextView) findViewById(R.id.nameApp);

        checkNetLayout = (LinearLayout) findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) findViewById(R.id.retry);

        filePath = "/data/data/com.ict.chilichef/databases/foodapp.db";
        file = new File(filePath);

        int min = 1;
        int max = 7;
        Random r = new Random();

        int i1 = r.nextInt(max - min + 1) + min;

        switch (i1) {

            case 1:
                random_text.setText("با ما حرفه ای آشپزی کنید");
                break;

            case 2:
                random_text.setText("لذت آشپزی را تجربه کنید");
                break;

            case 3:
                random_text.setText("آشپزی را آسان میکند");
                break;

            case 4:
                random_text.setText("سیر تا پیاز آشپزی");
                break;

            case 5:
                random_text.setText("آشپزی حرفه ای");
                break;

            case 6:
                random_text.setText("لذت یک میهمانی مجلل");
                break;

            case 7:
                random_text.setText("به دنیای آشپزی خوش آمدید");
                break;

        }

        if (file.exists()) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);
                    finish();

                }
            }, 4000);

        } else {

            if (HttpManager.checkNetwork(SplashActivity.this)) {
                getDataBaseFile();

                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
                finish();

            } else {

                TestInternet();
            }
        }

    }

    private void TestInternet() {

        checkNetLayout.setVisibility(View.VISIBLE);

        retryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (HttpManager.checkNetwork(SplashActivity.this)) {

                    getDataBaseFile();
                    checkNetLayout.setVisibility(View.GONE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            if (file.exists()) {

                                Intent i = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(i);
                                finish();
                            }

                        }
                    }, 4000);


                    retry = true;


                } else {

                    retry = false;

                    TestInternet();
                }


            }
        });
    }

    private void getDataBaseFile() {

        DBFetch task = new DBFetch(SplashActivity.this);
        IngFetch ingTask = new IngFetch(SplashActivity.this);

        try {
            HttpManager httpManager = new HttpManager(SplashActivity.this, filePath);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (HttpManager.checkNetwork(SplashActivity.this)) {

            task.execute(URL_PAGES + "getCategoriesAll.php");
        }
        if (HttpManager.checkNetwork(SplashActivity.this)) {

            ingTask.execute(URL_PAGES + "getAllIngredients.php");

        } else if (!HttpManager.checkNetwork(SplashActivity.this)) {

//            Toast.makeText(this, R.string.Disconnect, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

