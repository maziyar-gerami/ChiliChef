package com.ict.chilichef.BottomBar.RecipesTab;


import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ict.chilichef.Adapter.ViewPagerAdapter;
import com.ict.chilichef.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BbRecipesFragment extends Fragment {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    boolean state = false;

    public BbRecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_bb_recipes, container, false);

        toolbar = (Toolbar) getActivity().findViewById(R.id.Toolbar_Public);
        toolbar.setBackgroundResource(R.drawable.below_shadow);

        ((TextView) getActivity().findViewById(R.id.PublicTitle_toolbar)).setText(R.string.recipes);

        viewPager = (ViewPager) v.findViewById(R.id.Recipes_viewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) v.findViewById(R.id.Recipes_tabLayout);
        tabLayout.setupWithViewPager(viewPager);


        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();
        createTabIcons();
//        reduceMarginsInTabs(tabLayout, 20);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tabLayout.getTabAt(tab.getPosition()).getCustomView().setAlpha(1);

                setTextViewDrawableColor((TextView) tab.getCustomView(),ContextCompat.getColor(getContext(), (R.color.colorPrimary)));
                ((TextView) tab.getCustomView()).setTextColor(ContextCompat.getColor(getContext(), (R.color.colorPrimary)));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                setTextViewDrawableColor((TextView) tab.getCustomView(),ContextCompat.getColor(getContext(), (R.color.colorAccent)));
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
        tabOne.setCompoundDrawablesWithIntrinsicBounds( 0, 0,R.drawable.ic_new,0);
        tabLayout.getTabAt(1).setCustomView(tabOne);
        tabOne.setText(R.string.newRecipes);

        tabOne.setTextColor(ContextCompat.getColor(getContext(), (R.color.colorPrimary)));
        setTextViewDrawableColor(tabOne,ContextCompat.getColor(getContext(), (R.color.colorPrimary)));


        TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds( 0, 0,R.drawable.ic_best,0);
        tabLayout.getTabAt(0).setCustomView(tabTwo);
        tabTwo.setText(R.string.bestRecipe);

        tabTwo.setTextColor(ContextCompat.getColor(getContext(), (R.color.colorAccent)));
        setTextViewDrawableColor(tabTwo,ContextCompat.getColor(getContext(), (R.color.colorAccent)));
        Configuration config = getActivity().getResources().getConfiguration();


        if (config.smallestScreenWidthDp >= 720) {
            // sw720dp code goes here

            tabOne.setCompoundDrawablePadding(-100);
            tabOne.setPadding(0,0,110,0);
            tabOne.setGravity(Gravity.CENTER);

            tabTwo.setCompoundDrawablePadding(-100);
            tabTwo.setPadding(0,0,110,0);
            tabTwo.setGravity(Gravity.CENTER);

        }
        else if (config.smallestScreenWidthDp >= 600) {
            // sw600dp code goes here]

            tabOne.setCompoundDrawablePadding(-100);
            tabOne.setPadding(0,0,110,0);
            tabOne.setGravity(Gravity.CENTER);

            tabTwo.setCompoundDrawablePadding(-100);
            tabTwo.setPadding(0,0,110,0);
            tabTwo.setGravity(Gravity.CENTER);


        }
        else {
            // fall-back code goes here
        }


    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter Adapter = new ViewPagerAdapter(getChildFragmentManager());

        Adapter.addFragments(new RecipesBestFragment(), null);
        Adapter.addFragments(new RecipesRecentFragment(), null);

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

//    public static void reduceMarginsInTabs(TabLayout tabLayout, int marginOffset) {
//
//        View tabStrip = tabLayout.getChildAt(0);
//        if (tabStrip instanceof ViewGroup) {
//            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
//            for (int i = 0; i < ((ViewGroup) tabStrip).getChildCount(); i++) {
//                View tabView = tabStripGroup.getChildAt(i);
//                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
//                    ((ViewGroup.MarginLayoutParams) tabView.getLayoutParams()).leftMargin = marginOffset;
//                    ((ViewGroup.MarginLayoutParams) tabView.getLayoutParams()).rightMargin = marginOffset;
//                }
//            }
//
//            tabLayout.requestLayout();
//        }
//    }


}
