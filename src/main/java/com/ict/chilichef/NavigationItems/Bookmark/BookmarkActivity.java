package com.ict.chilichef.NavigationItems.Bookmark;

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
import android.widget.TextView;

import com.ict.chilichef.Adapter.ViewPagerAdapter;
import com.ict.chilichef.FoodActivity;
import com.ict.chilichef.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BookmarkActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton back;

    boolean state = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        toolbar = (Toolbar) findViewById(R.id.Toolbar_Public);
        ((TextView) findViewById(R.id.PublicTitle_toolbar)).setText(R.string.myFavorite);

        back = (ImageButton) findViewById(R.id.PublicLeft_icon_id);
        back.setImageResource(R.drawable.ic_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BookmarkActivity.this.finish();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.Bookmark_viewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.Bookmark_tabLayout);
        tabLayout.setupWithViewPager(viewPager);


        TabLayout.Tab tab = tabLayout.getTabAt(2);
        tab.select();
        setupTabIcons();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tabLayout.getTabAt(tab.getPosition()).getCustomView().setAlpha(1);
                //tabLayout.setCol

                setTextViewDrawableColor((TextView) tab.getCustomView(), ContextCompat.getColor(BookmarkActivity.this, (R.color.colorPrimary)));
                ((TextView) tab.getCustomView()).setTextColor(ContextCompat.getColor(BookmarkActivity.this, (R.color.colorPrimary)));



            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTextViewDrawableColor((TextView) tab.getCustomView(),ContextCompat.getColor(BookmarkActivity.this, (R.color.colorAccent)));
                ((TextView) tab.getCustomView()).setTextColor(ContextCompat.getColor(BookmarkActivity.this, (R.color.colorAccent)));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(BookmarkActivity.this).inflate(R.layout.custom_tab, null);
        tabOne.setCompoundDrawablesWithIntrinsicBounds( 0, 0,R.drawable.ic_food,0);
        tabLayout.getTabAt(2).setCustomView(tabOne);
        tabOne.setText(R.string.recipes);
        tabOne.setTextColor(ContextCompat.getColor(BookmarkActivity.this, (R.color.colorPrimary)));
        setTextViewDrawableColor(tabOne,ContextCompat.getColor(BookmarkActivity.this, (R.color.colorPrimary)));



        TextView tabTwo = (TextView) LayoutInflater.from(BookmarkActivity.this).inflate(R.layout.custom_tab, null);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds( 0, 0,R.drawable.ic_chef,0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
        tabTwo.setText(R.string.design_chef);
        tabTwo.setTextColor(ContextCompat.getColor(BookmarkActivity.this, (R.color.colorAccent)));
        setTextViewDrawableColor(tabTwo,ContextCompat.getColor(BookmarkActivity.this, (R.color.colorAccent)));

        TextView tabThree = (TextView) LayoutInflater.from(BookmarkActivity.this).inflate(R.layout.custom_tab, null);
        tabThree.setCompoundDrawablesWithIntrinsicBounds( 0, 0,R.drawable.ic_camera,0);
        tabLayout.getTabAt(0).setCustomView(tabThree);
        tabThree.setText(R.string.gallery);
        tabThree.setTextColor(ContextCompat.getColor(BookmarkActivity.this, (R.color.colorAccent)));
        setTextViewDrawableColor(tabThree,ContextCompat.getColor(BookmarkActivity.this, (R.color.colorAccent)));


    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter Adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Adapter.addFragments(new ImagesBookmarkFragment(), null);
        Adapter.addFragments(new DesignBookmarkFragment(),null);
        Adapter.addFragments(new RecipesBookmarkFragment(), null);

        viewPager.setAdapter(Adapter);

    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
            }
        }
    }


    public void onResume() {
        super.onResume();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }
}
