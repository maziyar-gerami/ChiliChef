package com.ict.chilichef.NavigationItems;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SupportActivity extends AppCompatActivity {

    ImageButton Telegram,Instagram,Email, Facebook;
    Toolbar toolbar;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        toolbar = (Toolbar) findViewById(R.id.Toolbar_Public);
        ((TextView) findViewById(R.id.PublicTitle_toolbar)).setText(R.string.app_name);

        back = (ImageButton) findViewById(R.id.PublicLeft_icon_id);
        back.setImageResource(R.drawable.ic_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupportActivity.this.finish();
            }
        });

        Telegram = (ImageButton) findViewById(R.id.telegram);
        Instagram = (ImageButton) findViewById(R.id.instagram);
        Email = (ImageButton) findViewById(R.id.email);
        Facebook = (ImageButton) findViewById(R.id.facebook);


        Telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/ChiliChef"));
                startActivity(i);
            }
        });



        Instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/chili.chef/"));
                startActivity(i);
            }
        });



        Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Send Email","");
                String[] To = {"chili.chefIcon.app@gmail.com"};
                Intent emailintent = new Intent(Intent.ACTION_SEND);

                emailintent.setData(Uri.parse("mail to:"));
                emailintent.setType("Text/Plain");
                emailintent.putExtra(Intent.EXTRA_EMAIL , To);
                emailintent.putExtra(Intent.EXTRA_SUBJECT , "Your Subject");
                emailintent.putExtra(Intent.EXTRA_TEXT , "Email Message Goes Here");

                try {
                    startActivity(Intent.createChooser(emailintent , "Send Email..."));
                    finish();
                    Log.i("Finished Send Email..." , "");
                }catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(SupportActivity.this , "There is no email" , Toast.LENGTH_LONG).show();
                }
            }

        });

        Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/chili.chef/"));
                startActivity(i);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
