package com.ict.chilichef.BottomBar.SearchTab;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ict.chilichef.Adapter.Recycler_SearchImage_Adapter;
import com.ict.chilichef.Adapter.ViewPagerAdapter;
import com.ict.chilichef.JsonParser.JsonParserSearchPictures;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.SearchPicturesModel;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ict.chilichef.BottomBar.SearchTab.BbSearchFragment.KEYWORD;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchGalleryFragment extends Fragment {

    //    public static String KEYWORD = "";
    private UserSessionManager session;

    private RecyclerView recyclerSearchImage;
    private Recycler_SearchImage_Adapter EAdapter;
    private ArrayList<SearchPicturesModel> itemListGallerySearch;
    private ArrayList<SearchPicturesModel> itemListGallerySearchTemp = new ArrayList<>();

    ImageButton retryBTN;
    private ProgressBar progressBar;
    boolean retry = false;
    private TextView noneGallery;
    LinearLayout checkNetLayout;

    public SearchGalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search_gallery, container, false);

        itemListGallerySearch = new ArrayList<>();
        session = new UserSessionManager(getContext());

        recyclerSearchImage = (RecyclerView) v.findViewById(R.id.recycler_image_search);
        noneGallery= (TextView) v.findViewById(R.id.noneGallery);

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        checkNetLayout = (LinearLayout) v.findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) v.findViewById(R.id.retry);

        HashMap<String, String> user = session.getUserDetails();
        String userID = user.get(UserSessionManager.KEY_ID_USER);
        if (userID == null) userID = "0";

        if (HttpManager.checkNetwork(getActivity())) {

            GallerySearchFetch galleryFetch = new GallerySearchFetch(getContext());
            galleryFetch.execute(KEYWORD, userID);

        } else {

            TestInternet();
        }

        return v;

    }

    private class GallerySearchFetch extends AsyncTask<String, String, String> {

        Context context;


        public GallerySearchFetch(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... string) {


            String jsonText = null;
            try {
                jsonText = HttpManager.searchGallery(string[0], Integer.parseInt(string[1]));

                return jsonText;

            } catch (Exception e) {

                return "error";
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            int size = itemListGallerySearch.size();

            if (size > 0) {

                for (int i = 0; i < size; i++) {
                    itemListGallerySearch.remove(0);
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
        protected void onPostExecute(String result) {

            if (result.length() > 6) {
                JsonParserSearchPictures jsonParserSearchPictures = new JsonParserSearchPictures();
                ArrayList<SearchPicturesModel> searchPicturesModels = jsonParserSearchPictures.parseJson(result);

                for (SearchPicturesModel FD : searchPicturesModels) {
                    itemListGallerySearch.add(FD);
                }

                itemListGallerySearchTemp = itemListGallerySearch;

                EAdapter = new Recycler_SearchImage_Adapter(itemListGallerySearch, getActivity());
                RecyclerView.LayoutManager ELayoutManger = new GridLayoutManager(getActivity(), 2);
                recyclerSearchImage.setLayoutManager(ELayoutManger);
                recyclerSearchImage.setItemAnimator(new DefaultItemAnimator());
                recyclerSearchImage.setAdapter(EAdapter);

                checkNetLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

            } else if (result.equals("error") || result.isEmpty()) {

                TestInternet();
            }

            else if (result.equals("[]\n")) {

                progressBar.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);
                noneGallery.setVisibility(View.VISIBLE);
            }

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
                    String userID = user.get(UserSessionManager.KEY_ID_USER);

                    if (userID == null) userID = "0";

                    GallerySearchFetch galleryFetch = new GallerySearchFetch(getContext());
                    galleryFetch.execute(KEYWORD, userID);

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
