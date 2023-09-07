package com.ict.chilichef.NavigationItems.Shared;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ict.chilichef.Adapter.ViewPagerAdapter;
import com.ict.chilichef.NavigationItems.Bookmark.BookmarkActivity;
import com.ict.chilichef.R;
import com.ict.chilichef.UsersManage.UsersActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SharedActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);


        toolbar = (Toolbar) findViewById(R.id.Toolbar_Public);
        setSupportActionBar(toolbar);
        ((TextView) findViewById(R.id.PublicTitle_toolbar)).setText(R.string.shared);


        back = (ImageButton) findViewById(R.id.PublicLeft_icon_id);
        back.setImageResource(R.drawable.ic_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedActivity.this.finish();
            }
        });


        viewPager = (ViewPager) findViewById(R.id.Users_viewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.Users_tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();
        createTabIcons();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tabLayout.getTabAt(tab.getPosition()).getCustomView().setAlpha(1);

                setTextViewDrawableColor((TextView) tab.getCustomView(), ContextCompat.getColor(SharedActivity.this, (R.color.colorPrimary)));
                ((TextView) tab.getCustomView()).setTextColor(ContextCompat.getColor(SharedActivity.this, (R.color.colorPrimary)));

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTextViewDrawableColor((TextView) tab.getCustomView(),ContextCompat.getColor(SharedActivity.this, (R.color.light_gray)));
                ((TextView) tab.getCustomView()).setTextColor(ContextCompat.getColor(SharedActivity.this, (R.color.light_gray)));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(SharedActivity.this).inflate(R.layout.custom_tab, null);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chef, 0);
        tabLayout.getTabAt(1).setCustomView(tabOne);
        tabOne.setText(R.string.recipes);
        tabOne.setTextColor(ContextCompat.getColor(SharedActivity.this, (R.color.colorPrimary)));
        setTextViewDrawableColor(tabOne,ContextCompat.getColor(SharedActivity.this, (R.color.colorPrimary)));

        TextView tabTwo = (TextView) LayoutInflater.from(SharedActivity.this).inflate(R.layout.custom_tab, null);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_camera, 0);
        tabLayout.getTabAt(0).setCustomView(tabTwo);
        tabTwo.setText(R.string.gallery);
        tabTwo.setTextColor(ContextCompat.getColor(SharedActivity.this, (R.color.light_gray)));
        setTextViewDrawableColor(tabTwo,ContextCompat.getColor(SharedActivity.this, (R.color.light_gray)));

    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter Adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Adapter.addFragments(new ImagesSharedFragment(), null);
        Adapter.addFragments(new RecipesSharedFragment(), null);

        viewPager.setAdapter(Adapter);

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
