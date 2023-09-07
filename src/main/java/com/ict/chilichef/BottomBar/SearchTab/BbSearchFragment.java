package com.ict.chilichef.BottomBar.SearchTab;


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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.Adapter.ViewPagerAdapter;
import com.ict.chilichef.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BbSearchFragment extends Fragment {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TabLayout.Tab tab;
    ImageButton SearchButton;
    EditText SearchEtx;
    public static String KEYWORD = "";
    boolean tabSelect = true;

    public BbSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bb_search, container, false);


        toolbar = (Toolbar) getActivity().findViewById(R.id.Toolbar_Public);
        toolbar.setBackgroundResource(R.drawable.below_shadow);

        ((TextView) getActivity().findViewById(R.id.PublicTitle_toolbar)).setText(R.string.search);

        toolbar = (Toolbar) v.findViewById(R.id.search_toolbar);

        viewPager = (ViewPager) v.findViewById(R.id.Search_viewPager);

        final ViewPagerAdapter Adapter = new ViewPagerAdapter(getChildFragmentManager());

        Adapter.addFragments(new SearchUsersFragment(), null);
        Adapter.addFragments(new SearchGalleryFragment(), null);
        Adapter.addFragments(new SearchRecipesFragment(), null);

        viewPager.setAdapter(Adapter);

        tabLayout = (TabLayout) v.findViewById(R.id.Search_tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tab = tabLayout.getTabAt(2);

        createTabIcons();
        setTextViewDrawableColor((TextView) tab.getCustomView(), ContextCompat.getColor(getContext(), (R.color.colorPrimary)));
        ((TextView) tab.getCustomView()).setTextColor(ContextCompat.getColor(getContext(), (R.color.colorPrimary)));
        tab.select();

        SearchEtx = (EditText) v.findViewById(R.id.keyword_toolbar);
        SearchButton = (ImageButton) v.findViewById(R.id.PublicLeft_icon_id);


        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabSelect = false;
                KEYWORD = SearchEtx.getText().toString();
                int thisPosition = tabLayout.getSelectedTabPosition();

                viewPager.setAdapter(Adapter);
                tabLayout.setupWithViewPager(viewPager);
                tab = tabLayout.getTabAt(thisPosition);
                createTabIcons();
                tab.select();

                switch (thisPosition) {
                    case 0:

                        setTextViewDrawableColor((TextView) tab.getCustomView(), ContextCompat.getColor(getContext(), (R.color.colorAccent)));
                        ((TextView) tab.getCustomView()).setTextColor(ContextCompat.getColor(getContext(), (R.color.colorAccent)));

                        setTextViewDrawableColor((TextView) tab.getCustomView(), ContextCompat.getColor(getContext(), (R.color.colorPrimary)));
                        ((TextView) tab.getCustomView()).setTextColor(ContextCompat.getColor(getContext(), (R.color.colorPrimary)));

                        break;
                    case 1:

                        setTextViewDrawableColor((TextView) tab.getCustomView(), ContextCompat.getColor(getContext(), (R.color.colorAccent)));
                        ((TextView) tab.getCustomView()).setTextColor(ContextCompat.getColor(getContext(), (R.color.colorAccent)));

                        setTextViewDrawableColor((TextView) tab.getCustomView(), ContextCompat.getColor(getContext(), (R.color.colorPrimary)));
                        ((TextView) tab.getCustomView()).setTextColor(ContextCompat.getColor(getContext(), (R.color.colorPrimary)));
                        break;
                    case 2:

                        setTextViewDrawableColor((TextView) tab.getCustomView(), ContextCompat.getColor(getContext(), (R.color.colorAccent)));
                        ((TextView) tab.getCustomView()).setTextColor(ContextCompat.getColor(getContext(), (R.color.colorAccent)));

                        setTextViewDrawableColor((TextView) tab.getCustomView(), ContextCompat.getColor(getContext(), (R.color.colorPrimary)));
                        ((TextView) tab.getCustomView()).setTextColor(ContextCompat.getColor(getContext(), (R.color.colorPrimary)));
                        break;

                }


            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tabSelect) {

                    tabLayout.getTabAt(tab.getPosition()).getCustomView().setAlpha(1);
                    //tabLayout.setCol

                    setTextViewDrawableColor((TextView) tab.getCustomView(), ContextCompat.getColor(getContext(), (R.color.colorPrimary)));
                    ((TextView) tab.getCustomView()).setTextColor(ContextCompat.getColor(getContext(), (R.color.colorPrimary)));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

//
                    setTextViewDrawableColor((TextView) tab.getCustomView(), ContextCompat.getColor(getContext(), (R.color.colorAccent)));
                    ((TextView) tab.getCustomView()).setTextColor(ContextCompat.getColor(getContext(), (R.color.colorAccent)));
//
                tabSelect = true;
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
        tabOne.setText(R.string.recipes);
        tabOne.setTextColor(ContextCompat.getColor(getContext(), (R.color.colorAccent)));
        setTextViewDrawableColor(tabOne, ContextCompat.getColor(getContext(), (R.color.colorAccent)));

        Configuration config = getActivity().getResources().getConfiguration();

        if (config.smallestScreenWidthDp >= 720) {
            // sw720dp code goes here

            tabOne.setCompoundDrawablePadding(-100);
            tabOne.setPadding(0,0,110,0);
            tabOne.setGravity(Gravity.CENTER);

        }
        else if (config.smallestScreenWidthDp >= 600) {
            // sw600dp code goes here]

            tabOne.setCompoundDrawablePadding(-100);
            tabOne.setPadding(0,0,110,0);
            tabOne.setGravity(Gravity.CENTER);

        }
        else {
            // fall-back code goes here
        }



        TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_camera, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
        tabTwo.setText(R.string.gallery);
        tabTwo.setTextColor(ContextCompat.getColor(getContext(), (R.color.colorAccent)));
        setTextViewDrawableColor(tabTwo, ContextCompat.getColor(getContext(), (R.color.colorAccent)));

        if (config.smallestScreenWidthDp >= 720) {
            // sw720dp code goes here

            tabTwo.setCompoundDrawablePadding(-100);
            tabTwo.setPadding(0,0,110,0);
            tabTwo.setGravity(Gravity.CENTER);
        }
        else if (config.smallestScreenWidthDp >= 600) {
            // sw600dp code goes here]

            tabTwo.setCompoundDrawablePadding(-100);
            tabTwo.setPadding(0,0,110,0);
            tabTwo.setGravity(Gravity.CENTER);

        }
        else {
            // fall-back code goes here
        }



        TextView tabThree = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chef, 0);
        tabLayout.getTabAt(0).setCustomView(tabThree);
        tabThree.setText(R.string.users);
        tabThree.setTextColor(ContextCompat.getColor(getContext(), (R.color.colorAccent)));
        setTextViewDrawableColor(tabThree, ContextCompat.getColor(getContext(), (R.color.colorAccent)));

        if (config.smallestScreenWidthDp >= 720) {
            // sw720dp code goes here

            tabThree.setCompoundDrawablePadding(-100);
            tabThree.setPadding(0,0,110,0);
            tabThree.setGravity(Gravity.CENTER);
        }
        else if (config.smallestScreenWidthDp >= 600) {
            // sw600dp code goes here]

            tabThree.setCompoundDrawablePadding(-100);
            tabThree.setPadding(0,0,110,0);
            tabThree.setGravity(Gravity.CENTER);

        }
        else {
            // fall-back code goes here
        }



    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
            }
        }
    }


}
