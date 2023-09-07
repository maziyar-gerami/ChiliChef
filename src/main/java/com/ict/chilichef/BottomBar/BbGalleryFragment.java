package com.ict.chilichef.BottomBar;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.Adapter.Recycler_Image_Adapter;
import com.ict.chilichef.JsonParser.JsonParserGallery;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.GalleryModel;
import com.ict.chilichef.NavigationItems.Categories.CategoriesFragment;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.AdvSessionManager;
import com.ict.chilichef.Sessions.UserSessionManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BbGalleryFragment extends Fragment {

    Toolbar toolbar;
    private RecyclerView recyclerImage;
    private Recycler_Image_Adapter EAdapter;
    private List<GalleryModel> itemListGallery;
    private UserSessionManager session;

    ImageButton retryBTN;
    private ProgressBar progressBar;
    boolean retry = false;
    LinearLayout checkNetLayout;

    SwipeRefreshLayout swipeView;


    public BbGalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bb_gallary, container, false);
        itemListGallery = new ArrayList<>();

        toolbar = (Toolbar) getActivity().findViewById(R.id.Toolbar_Public);
        toolbar.setBackgroundResource(R.drawable.below_shadow);

        ((TextView) getActivity().findViewById(R.id.PublicTitle_toolbar)).setText(R.string.gallery);


        recyclerImage = (RecyclerView) v.findViewById(R.id.recycler_image);

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        checkNetLayout = (LinearLayout) v.findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) v.findViewById(R.id.retry);

        swipeView = (SwipeRefreshLayout) v.findViewById(R.id.swipe);


        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeView.setEnabled(true);

                Log.d("Swipe", "Refreshing Number");

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeView.setRefreshing(false);

                        HashMap<String, String> user = session.getUserDetails();
                        String IdUser = user.get(UserSessionManager.KEY_ID_USER);
                        if (IdUser == null) IdUser = "0";

                        GalleryFetch galleryFetch = new GalleryFetch(getContext());
                        galleryFetch.execute(1, Integer.valueOf(IdUser));

                    }
                }, 3000);
            }
        });


        session = new UserSessionManager(getContext());

        if (HttpManager.checkNetwork(getActivity())) {

            HashMap<String, String> user = session.getUserDetails();
            String IdUser = user.get(UserSessionManager.KEY_ID_USER);
            if (IdUser == null) IdUser = "0";

            GalleryFetch galleryFetch = new GalleryFetch(getContext());
            galleryFetch.execute(1, Integer.valueOf(IdUser));

        } else {

            TestInternet();
        }


        return v;
    }

    private class GalleryFetch extends AsyncTask<Integer, String, String> implements Recycler_Image_Adapter.ClickListener {

        Context context;


        public GalleryFetch(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            int size = itemListGallery.size();

            if (size > 0) {
                for (int i = 0; i < size; i++) {

                    itemListGallery.remove(0);
                }

                EAdapter.notifyItemRangeRemoved(0, size);
            }

            if (HttpManager.checkNetwork(getActivity())) {

                progressBar.setVisibility(View.VISIBLE);
                checkNetLayout.setVisibility(View.GONE);

            } else {

                TestInternet();
            }

        }

        @Override
        protected String doInBackground(Integer... integers) {

            try {

                String jsonText = HttpManager.getGallery(integers[0], integers[1]);

                return jsonText;

            } catch (Exception e) {

                return "error";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.length() > 6) {

                JsonParserGallery jsonParserGallery = new JsonParserGallery();
                ArrayList<GalleryModel> galleryModels = null;
                try {
                    galleryModels = jsonParserGallery.parseJson(result);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                for (GalleryModel GM : galleryModels) {
                    itemListGallery.add(GM);
                }

                EAdapter = new Recycler_Image_Adapter(itemListGallery, getActivity());
                EAdapter.setClickListener(this);
                RecyclerView.LayoutManager ELayoutManger = new GridLayoutManager(getActivity(), 1);
                recyclerImage.setLayoutManager(ELayoutManger);
                recyclerImage.setItemAnimator(new DefaultItemAnimator());
                recyclerImage.setAdapter(EAdapter);

                progressBar.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            } else {
                Toast.makeText(context, R.string.Disconnect, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void itemSelectClicked(View view, int position) {

        }

    }

    private void TestInternet() {

        checkNetLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        retryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (HttpManager.checkNetwork(getActivity())) {

                    HashMap<String, String> user = session.getUserDetails();
                    final String userID = user.get(UserSessionManager.KEY_ID_USER);

                    GalleryFetch galleryFetch = new GalleryFetch(getContext());
                    galleryFetch.execute(1, Integer.valueOf(userID));

                    checkNetLayout.setVisibility(View.GONE);

                    retry = true;

                } else {

                    retry = false;

                    TestInternet();
                }
            }
        });
    }

}
