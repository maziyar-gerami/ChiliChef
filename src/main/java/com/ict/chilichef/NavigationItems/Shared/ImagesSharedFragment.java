package com.ict.chilichef.NavigationItems.Shared;


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

import com.ict.chilichef.Adapter.Recycler_UserGallery_Adapter;
import com.ict.chilichef.JsonParser.JsonParserGallery;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.GalleryModel;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.ict.chilichef.UsersManage.UsersActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImagesSharedFragment extends Fragment {


    private RecyclerView recyclerUserImage;
    private Recycler_UserGallery_Adapter GalleryAdapter;
    private List<GalleryModel> itemListGalleryUser = new ArrayList<GalleryModel>();
    ImageButton retryBTN;

    UserSessionManager session;

    private ProgressBar progressBar;
    private RelativeLayout relProgress;
    boolean retry = false;
    LinearLayout checkNetLayout;

    public ImagesSharedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_images_shared, container, false);

        itemListGalleryUser = new ArrayList<>();
        recyclerUserImage = (RecyclerView) v.findViewById(R.id.recycler_images_shared);

        relProgress = (RelativeLayout) v.findViewById(R.id.relProgress);
        checkNetLayout = (LinearLayout) v.findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) v.findViewById(R.id.retry);

        session = new UserSessionManager(getActivity());

        HashMap<String, String> user = session.getUserDetails();
        String id = user.get(UserSessionManager.KEY_ID_USER);

        if (HttpManager.checkNetwork(getActivity())) {

            GalleryUserFetch galleryUserFetch = new GalleryUserFetch(getActivity());
            galleryUserFetch.execute(Integer.valueOf(id), Integer.valueOf(id));

        } else {

            TestInternet();
        }

        return v;
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

                    GalleryUserFetch galleryUserFetch = new GalleryUserFetch(getActivity());
                    galleryUserFetch.execute(Integer.valueOf(id), Integer.valueOf(id));

                    checkNetLayout.setVisibility(View.GONE);

                    retry = true;

                } else {

                    retry = false;

                    TestInternet();
                }
            }
        });
    }


    private class GalleryUserFetch extends AsyncTask<Integer, String, String> implements Recycler_UserGallery_Adapter.ClickListener {

        Context context;


        public GalleryUserFetch(Context context) {

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (HttpManager.checkNetwork(getActivity())) {

                relProgress.setVisibility(View.VISIBLE);
                checkNetLayout.setVisibility(View.GONE);

            } else {

                TestInternet();
            }

        }

        @Override
        protected String doInBackground(Integer... integers) {

            try {
                String jsonText = HttpManager.getUserGallery(integers[0], integers[1]);

                return jsonText;

            } catch (Exception e) {
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            if (result.length() > 6) {

                JsonParserGallery jsonParserGallery = new JsonParserGallery();
                ArrayList<GalleryModel> galleryModels = null;
                try {
                    galleryModels = jsonParserGallery.parseJson(result);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                for (GalleryModel GM : galleryModels) {
                    itemListGalleryUser.add(GM);
                }

                GalleryAdapter = new Recycler_UserGallery_Adapter(itemListGalleryUser, getActivity());
                GalleryAdapter.setClickListener(this);
                RecyclerView.LayoutManager ELayoutManger = new GridLayoutManager(getActivity(), 3);
                recyclerUserImage.setLayoutManager(ELayoutManger);
                recyclerUserImage.setItemAnimator(new DefaultItemAnimator());
                recyclerUserImage.setAdapter(GalleryAdapter);

                relProgress.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            }else if (result.equals("[]\n")) {

                Toast.makeText(getActivity(), R.string.emptyList, Toast.LENGTH_SHORT).show();

                relProgress.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            } else if (result.equals("error") || result.isEmpty()) {

                TestInternet();
            }
        }

        @Override
        public void itemOnClicked(View view, int position) {

        }
    }

}