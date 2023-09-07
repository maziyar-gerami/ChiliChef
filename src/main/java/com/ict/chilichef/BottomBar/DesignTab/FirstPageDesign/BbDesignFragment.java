package com.ict.chilichef.BottomBar.DesignTab.FirstPageDesign;

import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.Adapter.Recycler_Design_Adapter;
import com.ict.chilichef.Adapter.ViewPagerAdapter;
import com.ict.chilichef.Model.Data;
import com.ict.chilichef.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

public class BbDesignFragment extends Fragment {


    private List<Data> itemList;
    private RecyclerView recyclerview;
    private Recycler_Design_Adapter mAdapter;
    Toolbar toolbar;

    private TabLayout tabLayout;
    private ViewPager viewPager;


    public BbDesignFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_bb_design, container, false);

        toolbar = (Toolbar) getActivity().findViewById(R.id.Toolbar_Public);
        toolbar.setBackgroundResource(R.drawable.below_shadow);
        ((TextView) getActivity().findViewById(R.id.PublicTitle_toolbar)).setText(R.string.design_food);

        viewPager = (ViewPager) v.findViewById(R.id.bb_Design_viewPager);

        final ViewPagerAdapter Adapter = new ViewPagerAdapter(getChildFragmentManager());

        Adapter.addFragments(new DesignTableFragment(), null);
        Adapter.addFragments(new DesignFruitFragment(), null);
        Adapter.addFragments(new DesignFoodFragment(), null);

        viewPager.setAdapter(Adapter);

        tabLayout = (TabLayout) v.findViewById(R.id.bb_Design_tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        TabLayout.Tab tab = tabLayout.getTabAt(2);
        tab.select();
        createTabIcons();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tabLayout.getTabAt(tab.getPosition()).getCustomView().setAlpha(1);
                //tabLayout.setCol

                setTextViewDrawableColor((TextView) tab.getCustomView(), ContextCompat.getColor(getContext(), (R.color.colorPrimary)));
                ((TextView) tab.getCustomView()).setTextColor(ContextCompat.getColor(getContext(), (R.color.colorPrimary)));


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTextViewDrawableColor((TextView) tab.getCustomView(), ContextCompat.getColor(getContext(), (R.color.colorAccent)));
                ((TextView) tab.getCustomView()).setTextColor(ContextCompat.getColor(getContext(), (R.color.colorAccent)));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return v;
    }

    private void createTabIcons() {


        TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_food_des, 0);
        tabLayout.getTabAt(2).setCustomView(tabOne);
        tabOne.setText("غذا آرایی");
        Configuration config = getActivity().getResources().getConfiguration();

        if (config.smallestScreenWidthDp >= 720) {
            // sw720dp code goes here

            tabOne.setCompoundDrawablePadding(-100);
            tabOne.setPadding(0, 0, 110, 0);
            tabOne.setGravity(Gravity.CENTER);
        } else if (config.smallestScreenWidthDp >= 600) {
            // sw600dp code goes here]

            tabOne.setCompoundDrawablePadding(-100);
            tabOne.setPadding(0, 0, 110, 0);
            tabOne.setGravity(Gravity.CENTER);

        } else {
            // fall-back code goes here

        }


        tabOne.setTextColor(ContextCompat.getColor(getContext(), (R.color.colorPrimary)));
        setTextViewDrawableColor(tabOne, ContextCompat.getColor(getContext(), (R.color.colorPrimary)));


        TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_fruit_des, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
        tabTwo.setText(R.string.fruit);
        tabTwo.setTextColor(ContextCompat.getColor(getContext(), (R.color.colorAccent)));
        setTextViewDrawableColor(tabTwo, ContextCompat.getColor(getContext(), (R.color.colorAccent)));

        if (config.smallestScreenWidthDp >= 720) {
            // sw720dp code goes here

            tabTwo.setCompoundDrawablePadding(-100);
            tabTwo.setPadding(0, 0, 110, 0);
            tabTwo.setGravity(Gravity.CENTER);
        } else if (config.smallestScreenWidthDp >= 600) {
            // sw600dp code goes here]

            tabTwo.setCompoundDrawablePadding(-100);
            tabTwo.setPadding(0, 0, 110, 0);
            tabTwo.setGravity(Gravity.CENTER);

        } else {
            // fall-back code goes here
        }


        TextView tabThree = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_table_des, 0);
        tabLayout.getTabAt(0).setCustomView(tabThree);
        tabThree.setText(R.string.table);
        tabThree.setTextColor(ContextCompat.getColor(getContext(), (R.color.colorAccent)));
        setTextViewDrawableColor(tabThree, ContextCompat.getColor(getContext(), (R.color.colorAccent)));

        if (config.smallestScreenWidthDp >= 720) {
            // sw720dp code goes here

            tabThree.setCompoundDrawablePadding(-100);
            tabThree.setPadding(0, 0, 110, 0);
            tabThree.setGravity(Gravity.CENTER);
        } else if (config.smallestScreenWidthDp >= 600) {
            // sw600dp code goes here]

            tabThree.setCompoundDrawablePadding(-100);
            tabThree.setPadding(0, 0, 110, 0);
            tabThree.setGravity(Gravity.CENTER);

        } else {
            // fall-back code goes here
        }


    }


    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
            }
        }
    }

}
