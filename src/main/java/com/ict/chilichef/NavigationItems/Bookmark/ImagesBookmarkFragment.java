package com.ict.chilichef.NavigationItems.Bookmark;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ict.chilichef.Adapter.Recycler_Only_Image_Adapter;
import com.ict.chilichef.Adapter.Recycler_UserRecipes_Adapter;
import com.ict.chilichef.JsonParser.JsonParserGallery;
import com.ict.chilichef.JsonParser.JsonParserUserRecipe;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.FoodsUserModel;
import com.ict.chilichef.Model.GalleryModel;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImagesBookmarkFragment extends Fragment {


    private RecyclerView recyclerBookmark;
    private Recycler_Only_Image_Adapter ImagesAdapter;
    private ArrayList<GalleryModel> itemListBookmark = new ArrayList<>();


    UserSessionManager session;

    ImageButton retryBTN;
    private RelativeLayout relProgress;
    boolean retry = false;
    LinearLayout checkNetLayout;

    public ImagesBookmarkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_images_bookmark, container, false);

        session = new UserSessionManager(getActivity());

        itemListBookmark = new ArrayList<>();
        recyclerBookmark = (RecyclerView) v.findViewById(R.id.recycler_images_mark);

        relProgress = (RelativeLayout) v.findViewById(R.id.relProgress);
        checkNetLayout = (LinearLayout) v.findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) v.findViewById(R.id.retry);

        return v;
    }


    private class GalleryBookmark extends AsyncTask<Integer, String, String> {

        Context context;

        public GalleryBookmark(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Integer... integers) {


            String jsonText = null;

            try {

                jsonText = HttpManager.getBookmarkedGallery(integers[0], integers[1]);


                return jsonText;

            } catch (Exception e) {

                return "error";
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            int size = itemListBookmark.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    itemListBookmark.remove(0);
                }

                ImagesAdapter.notifyItemRangeRemoved(0, size);
            }

            if (HttpManager.checkNetwork(getActivity())) {

                relProgress.setVisibility(View.VISIBLE);
                checkNetLayout.setVisibility(View.GONE);

            } else {


                TestInternet();
            }

        }


        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);

            if (result.length() > 6) {


                JsonParserGallery jsonParserGallery = new JsonParserGallery();

                ArrayList<GalleryModel> galleryModel = null;
                try {
                    galleryModel = jsonParserGallery.parseJson(result);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                for (GalleryModel GM : galleryModel) {
                    itemListBookmark.add(GM);
                }


                ImagesAdapter = new Recycler_Only_Image_Adapter(itemListBookmark, getActivity());
                RecyclerView.LayoutManager ELayoutManger = new GridLayoutManager(getActivity(), 2);
                recyclerBookmark.setLayoutManager(ELayoutManger);
                recyclerBookmark.setItemAnimator(new DefaultItemAnimator());
                recyclerBookmark.setAdapter(ImagesAdapter);

                relProgress.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);


            } else if (result.equals("error") || result.isEmpty()) {

                TestInternet();
            }else if (result.equals("[]\n")) {

                relProgress.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            }

        }
    }


    @Override
    public void onResume() {
        super.onResume();

        HashMap<String, String> user = session.getUserDetails();
        String id = user.get(UserSessionManager.KEY_ID_USER);
        if (HttpManager.checkNetwork(getActivity())) {

            GalleryBookmark gb = new GalleryBookmark(getActivity());
            gb.execute(1, Integer.valueOf(id));

        } else {

            TestInternet();
        }
    }

    private void TestInternet() {

        checkNetLayout.setVisibility(View.VISIBLE);
        relProgress.setVisibility(View.GONE);

        retryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (HttpManager.checkNetwork(getActivity())) {

                    HashMap<String, String> user = session.getUserDetails();

                    String id = user.get(UserSessionManager.KEY_ID_USER);

                    GalleryBookmark gb = new GalleryBookmark(getActivity());
                    gb.execute(1, Integer.valueOf(id));

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

