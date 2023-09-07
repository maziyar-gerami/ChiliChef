package com.ict.chilichef.BottomBar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.Adapter.Recycler_Fix_Adapter;
import com.ict.chilichef.Adapter.Recycler_MainPicture_Adapter;
import com.ict.chilichef.Adapter.Recycler_MainRecipes_Adapter;

import com.ict.chilichef.Adapter.Recycler_View_Adapter;
import com.ict.chilichef.Adapter.Recycler_advertise_Adapter;
import com.ict.chilichef.JsonParser.JsonParserAdvertise;
import com.ict.chilichef.JsonParser.JsonParserSearchPictures;
import com.ict.chilichef.JsonParser.JsonParserSearchRecepies;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.Data;
import com.ict.chilichef.Model.SearchPicturesModel;
import com.ict.chilichef.Model.SearchRecipesModel;
import com.ict.chilichef.Model.advertiseModel;
import com.ict.chilichef.NavigationItems.Categories.FoodCategoriesActivity;
import com.ict.chilichef.NavigationItems.Notes.NotesCatActivity;
import com.ict.chilichef.NavigationItems.What2Cook.WhatCookActivity;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.AdvSessionManager;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.ict.chilichef.NavigationItems.Categories.TabFoodActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BbHomeFragment extends Fragment {

    Toolbar toolbar;
    boolean flag = false;
    public Handler handler = new Handler();
    UserSessionManager session;
    AdvSessionManager advSessionManager;

    SwipeRefreshLayout swipeView;

    private LinearLayout appetizer, tradition, modern, drinks, sweets, dessert,
     linNotes,linWhatCook, linCat;

    private RecyclerView GalleryRecyclerLikes, UpAdvRecycler, DownAdvRecycler, recipesRecyclerLikes;

    private ArrayList<advertiseModel> UpItemListAdvertise;
    private ArrayList<advertiseModel> UpItemListAdvertiseTemp = new ArrayList<>();
    private ArrayList<advertiseModel> DownItemListAdvertise;
    private ArrayList<advertiseModel> DownItemListAdvertiseTemp = new ArrayList<>();

    private Recycler_MainRecipes_Adapter RecipesAdapterLikes;
    private Recycler_advertise_Adapter advertise_adapter;
    private Recycler_MainPicture_Adapter GalleryAdapterLikes;


    private List<SearchRecipesModel> itemListRecipesTemp = new ArrayList<>();
    private List<SearchRecipesModel> itemListRecipes;
    private List<SearchPicturesModel> itemListGallery;
    private List<SearchPicturesModel> itemListGalleryTemp = new ArrayList<>();

    static final String Best_picMethod_Home = "POPULARPICS";
    static final String Best_picCount_Home = "10";
    static final String Best_recMethod_Home = "POPULARRECIPES";
    static final String Best_recCount_Home = "10";

    public BbHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bb_home, container, false);

        session = new UserSessionManager(getContext());

        UpItemListAdvertise = new ArrayList<>();
        DownItemListAdvertise = new ArrayList<>();
        itemListRecipes = new ArrayList<>();
        itemListGallery = new ArrayList<>();

        toolbar = (Toolbar) getActivity().findViewById(R.id.Toolbar_Public);
        ((TextView) getActivity().findViewById(R.id.PublicTitle_toolbar)).setText("");
        toolbar.setBackgroundColor(ContextCompat.getColor(getContext(), (R.color.white)));

        swipeView = (SwipeRefreshLayout) v.findViewById(R.id.swipe);

        UpAdvRecycler = (RecyclerView) v.findViewById(R.id.UpAdvRecycler);
        DownAdvRecycler = (RecyclerView) v.findViewById(R.id.DownAdvRecycler);
        GalleryRecyclerLikes = (RecyclerView) v.findViewById(R.id.GalleryRecyclerLikes);
        recipesRecyclerLikes = (RecyclerView) v.findViewById(R.id.recipesRecyclerLikes);

        appetizer = (LinearLayout) v.findViewById(R.id.LinearFoodType);
        tradition = (LinearLayout) v.findViewById(R.id.LinearTradition);
        modern = (LinearLayout) v.findViewById(R.id.LinearModern);
        drinks = (LinearLayout) v.findViewById(R.id.LinearDrink);
        sweets = (LinearLayout) v.findViewById(R.id.LinearCake);
        dessert = (LinearLayout) v.findViewById(R.id.LinearDesert);

        linNotes = (LinearLayout) v.findViewById(R.id.mainNotes);
        linWhatCook = (LinearLayout) v.findViewById(R.id.mainWhatCook);
        linCat = (LinearLayout) v.findViewById(R.id.mainCat);


        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeView.setEnabled(true);
                swipeView.setColorSchemeColors(ContextCompat.getColor(getActivity(), (R.color.colorAccent)));

                Log.d("Swipe", "Refreshing Number");

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeView.setRefreshing(false);

                        HashMap<String, String> user = session.getUserDetails();
                        String IdUser = user.get(UserSessionManager.KEY_ID_USER);
                        if (IdUser == null) IdUser = "0";

                        if (HttpManager.checkNetwork(getContext())) {

                            int sizeGal = itemListGallery.size();
                            int sizeRec = itemListRecipes.size();

                            if (sizeGal > 0) {
                                for (int i = 0; i < sizeGal; i++) {

                                    itemListGallery.remove(0);
                                }

                            }
                            if (sizeRec > 0) {
                                for (int i = 0; i < sizeRec; i++) {

                                    itemListRecipes.remove(0);
                                }

                            }

                            BestGalleryFetch GS = new BestGalleryFetch(getContext());
                            GS.execute(Best_picMethod_Home, Best_picCount_Home, IdUser);

                            BestRecFetch RS = new BestRecFetch(getContext());
                            RS.execute(Best_recMethod_Home, Best_recCount_Home);

                            UpAdvertise_fetch advertise_fetch = new UpAdvertise_fetch(getContext());
                            advertise_fetch.execute(1);

                            DownAdvertise_fetch advertise_fetch2 = new DownAdvertise_fetch(getContext());
                            advertise_fetch2.execute(2);

                            advSessionManager = new AdvSessionManager(getActivity());
                            advSessionManager.createSaveSession("-1");
                            handler.post(timedTask);
                        }else {

                            Toast.makeText(getActivity(), R.string.Disconnect, Toast.LENGTH_SHORT).show();
                        }

                    }
                }, 3000);
            }
        });


        linNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), NotesCatActivity.class);
                startActivity(i);

            }
        });


        linWhatCook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (session.isUserLoggedIn()) {

                    Intent i = new Intent(getActivity(), WhatCookActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getActivity(), R.string.plzLogIn, Toast.LENGTH_SHORT).show();
                }

            }
        });

        linCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), FoodCategoriesActivity.class);
                startActivity(i);
            }
        });


        catToolbar();

        HashMap<String, String> user = session.getUserDetails();
        String IdUser = user.get(UserSessionManager.KEY_ID_USER);

        if (IdUser == null) IdUser = "0";

        if (HttpManager.checkNetwork(getActivity())) {

            BestGalleryFetch GS = new BestGalleryFetch(getContext());
            GS.execute(Best_picMethod_Home, Best_picCount_Home, IdUser);

            BestRecFetch RS = new BestRecFetch(getContext());
            RS.execute(Best_recMethod_Home, Best_recCount_Home);


            UpAdvertise_fetch advertise_fetch = new UpAdvertise_fetch(getContext());
            advertise_fetch.execute(1);

            DownAdvertise_fetch advertise_fetch2 = new DownAdvertise_fetch(getContext());
            advertise_fetch2.execute(2);

            advSessionManager = new AdvSessionManager(getActivity());
            advSessionManager.createSaveSession("-1");
            handler.post(timedTask);

        } else {

            Toast.makeText(getActivity(), R.string.Disconnect, Toast.LENGTH_SHORT).show();
        }

        return v;

    }

    private void catToolbar() {

        appetizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext(), TabFoodActivity.class);
                i.putExtra("ItemID", 13);
                i.putExtra("ItemName", getString(R.string.appetizer));
                getContext().startActivity(i);
            }
        });


        tradition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext(), TabFoodActivity.class);
                i.putExtra("ItemID", 3);
                i.putExtra("ItemName", getString(R.string.tradition));
                getContext().startActivity(i);
            }
        });


        modern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext(), TabFoodActivity.class);
                i.putExtra("ItemID", 4);
                i.putExtra("ItemName", getString(R.string.modern));
                getContext().startActivity(i);
            }
        });

        drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext(), TabFoodActivity.class);
                i.putExtra("ItemID", 19);
                i.putExtra("ItemName", getString(R.string.drink));
                getContext().startActivity(i);
            }
        });

        sweets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext(), TabFoodActivity.class);
                i.putExtra("ItemID", 21);
                i.putExtra("ItemName", getString(R.string.cake));
                getContext().startActivity(i);
            }
        });


        dessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext(), TabFoodActivity.class);
                i.putExtra("ItemID", 14);
                i.putExtra("ItemName", getString(R.string.desert));
                getContext().startActivity(i);
            }
        });

    }

    private class BestRecFetch extends AsyncTask<String, String, String> {

        Context context;


        public BestRecFetch(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... string) {

            String jsonText = null;


            try {
                jsonText = HttpManager.FirstPageFoodsQueries(string[0], Integer.parseInt(string[1]));

                return jsonText;

            } catch (Exception e) {

                return "error";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);

            if (result.length() > 6) {

                JsonParserSearchRecepies jsonParserSearchRecepies = new JsonParserSearchRecepies();
                ArrayList<SearchRecipesModel> searchRecipesModels = jsonParserSearchRecepies.parseJson(result);

                for (SearchRecipesModel FD : searchRecipesModels) {
                    itemListRecipes.add(FD);

                    itemListRecipesTemp = itemListRecipes;
                }

                RecipesAdapterLikes = new Recycler_MainRecipes_Adapter(itemListRecipes, getActivity());
                RecyclerView.LayoutManager LLayoutManger = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
                recipesRecyclerLikes.setLayoutManager(LLayoutManger);
                recipesRecyclerLikes.setItemAnimator(new DefaultItemAnimator());
                recipesRecyclerLikes.setAdapter(RecipesAdapterLikes);


            } else if (result.equals("error") || result.isEmpty()) {

            }

        }
    }

    private class BestGalleryFetch extends AsyncTask<String, String, String> {

        Context context;

        public BestGalleryFetch(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... string) {


            String jsonText = null;
            try {
                jsonText = HttpManager.FirstPicsQueries(string[0], Integer.parseInt(string[1]), string[2]);

                return jsonText;

            } catch (Exception e) {

                return "error";
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);

            if (result.length() > 6) {

                JsonParserSearchPictures jsonParserSearchPictures = new JsonParserSearchPictures();
                ArrayList<SearchPicturesModel> searchPicturesModels = jsonParserSearchPictures.parseJson(result);

                for (SearchPicturesModel GG : searchPicturesModels) {
                    itemListGallery.add(GG);

                }

                itemListGalleryTemp = itemListGallery;

                GalleryAdapterLikes = new Recycler_MainPicture_Adapter(itemListGallery, getActivity());

                RecyclerView.LayoutManager GLayoutManger = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
                GalleryRecyclerLikes.setLayoutManager(GLayoutManger);
                GalleryRecyclerLikes.setItemAnimator(new DefaultItemAnimator());
                GalleryRecyclerLikes.setAdapter(GalleryAdapterLikes);

            }

        }
    }

    private class UpAdvertise_fetch extends AsyncTask<Integer, String, String> {

        Context context;

        public UpAdvertise_fetch(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Integer... integers) {


            String jsonText = null;

            try {


                jsonText = HttpManager.getAdvertise(integers[0]);
                return jsonText;

            } catch (Exception e) {
                return "error";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            int size = UpItemListAdvertise.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    UpItemListAdvertise.remove(0);
                }

            }

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.length() > 6) {

                JsonParserAdvertise jsonParserAdvertise = new JsonParserAdvertise();
                ArrayList<advertiseModel> advertiseModels = jsonParserAdvertise.parseJson(result);

                for (advertiseModel ADV : advertiseModels) {
                    UpItemListAdvertise.add(ADV);
                }


                UpItemListAdvertiseTemp = UpItemListAdvertise;

                advertise_adapter = new Recycler_advertise_Adapter(UpItemListAdvertise, getActivity());
                RecyclerView.LayoutManager advLayoutManger = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);

                UpAdvRecycler.setLayoutManager(advLayoutManger);
                UpAdvRecycler.setItemAnimator(new DefaultItemAnimator());
                UpAdvRecycler.setAdapter(advertise_adapter);

                PagerSnapHelper snapHelper = new PagerSnapHelper();
                UpAdvRecycler.setOnFlingListener(null);
                snapHelper.attachToRecyclerView(UpAdvRecycler);

            }
        }
    }

    private class DownAdvertise_fetch extends AsyncTask<Integer, String, String> {

        Context context;

        public DownAdvertise_fetch(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Integer... integers) {


            String jsonText = null;
            try {

                jsonText = HttpManager.getAdvertise(integers[0]);
                return jsonText;

            } catch (Exception e) {
                return "error";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            int size = DownItemListAdvertise.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    DownItemListAdvertise.remove(0);
                }

            }
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.length() > 6) {


                JsonParserAdvertise jsonParserAdvertise = new JsonParserAdvertise();
                ArrayList<advertiseModel> advertiseModels = jsonParserAdvertise.parseJson(result);

                for (advertiseModel ADV : advertiseModels) {
                    DownItemListAdvertise.add(ADV);
                }


                DownItemListAdvertiseTemp = DownItemListAdvertise;

                advertise_adapter = new Recycler_advertise_Adapter(DownItemListAdvertise, getActivity());
                RecyclerView.LayoutManager advLayoutManger = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);

                DownAdvRecycler.setLayoutManager(advLayoutManger);
                DownAdvRecycler.setItemAnimator(new DefaultItemAnimator());
                DownAdvRecycler.setAdapter(advertise_adapter);

                PagerSnapHelper snapHelper = new PagerSnapHelper();
                DownAdvRecycler.setOnFlingListener(null);
                snapHelper.attachToRecyclerView(DownAdvRecycler);

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        flag = false;

        if (itemListRecipesTemp.size() > 0) {

            RecipesAdapterLikes = new Recycler_MainRecipes_Adapter(itemListRecipesTemp, getActivity());
            RecyclerView.LayoutManager LLayoutManger = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
            recipesRecyclerLikes.setLayoutManager(LLayoutManger);
            recipesRecyclerLikes.setItemAnimator(new DefaultItemAnimator());
            recipesRecyclerLikes.setAdapter(RecipesAdapterLikes);
        }

        if (itemListGalleryTemp.size() > 0) {

            GalleryAdapterLikes = new Recycler_MainPicture_Adapter(itemListGalleryTemp, getActivity());
            RecyclerView.LayoutManager GLayoutManger = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
            GalleryRecyclerLikes.setLayoutManager(GLayoutManger);
            GalleryRecyclerLikes.setItemAnimator(new DefaultItemAnimator());
            GalleryRecyclerLikes.setAdapter(GalleryAdapterLikes);
        }

        if (UpItemListAdvertiseTemp.size() > 0) {

            advertise_adapter = new Recycler_advertise_Adapter(UpItemListAdvertiseTemp, getActivity());
            RecyclerView.LayoutManager advLayoutManger = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);

            UpAdvRecycler.setLayoutManager(advLayoutManger);
            UpAdvRecycler.setItemAnimator(new DefaultItemAnimator());
            UpAdvRecycler.setAdapter(advertise_adapter);

            PagerSnapHelper snapHelper = new PagerSnapHelper();
            UpAdvRecycler.setOnFlingListener(null);
            snapHelper.attachToRecyclerView(UpAdvRecycler);

            advSessionManager = new AdvSessionManager(getActivity());
            advSessionManager.createSaveSession("-1");
            handler.post(timedTask);


        }

        if (DownItemListAdvertiseTemp.size() > 0) {

            advertise_adapter = new Recycler_advertise_Adapter(DownItemListAdvertiseTemp, getActivity());
            RecyclerView.LayoutManager advLayoutManger = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);

            DownAdvRecycler.setLayoutManager(advLayoutManger);
            DownAdvRecycler.setItemAnimator(new DefaultItemAnimator());
            DownAdvRecycler.setAdapter(advertise_adapter);

            PagerSnapHelper snapHelper = new PagerSnapHelper();
            DownAdvRecycler.setOnFlingListener(null);
            snapHelper.attachToRecyclerView(DownAdvRecycler);


        }

    }

    public Runnable timedTask = new Runnable() {

        @Override
        public void run() {
            // type code here which will loop with the given delay

            if (flag == false) {

                advSessionManager = new AdvSessionManager(getActivity());
                HashMap<String, String> adv = advSessionManager.getSaveDetails();
                String advState = adv.get(advSessionManager.KEY_STATUS);

                int i = Integer.parseInt(advState) + 1;
                if (i < UpItemListAdvertiseTemp.size()) {

                    UpAdvRecycler.scrollToPosition(i);
                    advSessionManager.createSaveSession(String.valueOf(i));

                } else {
                    advSessionManager.createSaveSession("0");
                    UpAdvRecycler.scrollToPosition(0);

                }
            }

            //if you want to end the runnable type this in the condition
            if (flag == true) {

                handler.removeCallbacks(this);

                return;
            }

            //delay for the runnable
            handler.postDelayed(timedTask, 4000);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        flag = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        flag = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        flag = true;
    }
}


