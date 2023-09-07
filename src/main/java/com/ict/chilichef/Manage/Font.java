package com.ict.chilichef.Manage;

import android.app.Application;

import com.ict.chilichef.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by NP on 11/5/2017.
 */

public class Font extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/IRANSans.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
