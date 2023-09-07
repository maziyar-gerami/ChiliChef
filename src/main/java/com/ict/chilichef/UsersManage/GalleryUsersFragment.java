package com.ict.chilichef.UsersManage;


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
import android.widget.Toast;

import com.ict.chilichef.Adapter.Recycler_UserGallery_Adapter;
import com.ict.chilichef.JsonParser.JsonParserGallery;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.GalleryModel;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryUsersFragment extends Fragment {

    private RecyclerView recyclerUserImage;
    private Recycler_UserGallery_Adapter GalleryAdapter;
    private List<GalleryModel> itemListGalleryUser;

    UserSessionManager session;
    private int ID;
    private Bundle bundle;

    public GalleryUsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_gallery_users, container, false);
        bundle = getActivity().getIntent().getExtras();
        session = new UserSessionManager(getActivity());
        ID = bundle.getInt("IdUSER");

        itemListGalleryUser = new ArrayList<>();

        recyclerUserImage = (RecyclerView) v.findViewById(R.id.user_recycler_Gallery);

        if (HttpManager.checkNetwork(getActivity())) {

            HashMap<String, String> user = session.getUserDetails();

            String id = user.get(UserSessionManager.KEY_ID_USER);

            GalleryUserFetch galleryUserFetch = new GalleryUserFetch(getActivity());
            galleryUserFetch.execute(ID, Integer.valueOf(id));

        } else {

            Toast.makeText(getActivity(), R.string.Disconnect, Toast.LENGTH_SHORT).show();

        }


        return v;
    }

    private class GalleryUserFetch extends AsyncTask<Integer, String, String> implements Recycler_UserGallery_Adapter.ClickListener {

        Context context;


        public GalleryUserFetch(Context context) {

            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
                    itemListGalleryUser.add(GM);
                }

                GalleryAdapter = new Recycler_UserGallery_Adapter(itemListGalleryUser, getActivity());
                GalleryAdapter.setClickListener(this);
                RecyclerView.LayoutManager ELayoutManger = new GridLayoutManager(getActivity(), 3);
                recyclerUserImage.setLayoutManager(ELayoutManger);
                recyclerUserImage.setItemAnimator(new DefaultItemAnimator());
                recyclerUserImage.setAdapter(GalleryAdapter);


            } else if (result.equals("error") || result.isEmpty()) {

                Toast.makeText(getActivity(), R.string.Disconnect, Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void itemOnClicked(View view, int position) {

        }
    }

}
