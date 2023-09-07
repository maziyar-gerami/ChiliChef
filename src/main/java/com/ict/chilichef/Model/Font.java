package com.ict.chilichef.Model;



import android.app.Application;

import com.ict.chilichef.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by roshan on 3/26/2017.
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
