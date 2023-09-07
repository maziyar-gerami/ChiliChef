package com.ict.chilichef.BottomBar.DesignTab;


import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.ict.chilichef.Adapter.Recycle_Fill_Adapter;
import com.ict.chilichef.Adapter.Recycler_Design_Adapter;
import com.ict.chilichef.Adapter.Recycler_Notes_Adapter;
import com.ict.chilichef.Adapter.Recycler_View_Adapter;
import com.ict.chilichef.JsonParser.JsonParserDataDesign;
import com.ict.chilichef.JsonParser.JsonParserDesign;
import com.ict.chilichef.JsonParser.JsonParserNotes;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.Data;
import com.ict.chilichef.Model.DesignModel;
import com.ict.chilichef.Model.NotesModel;
import com.ict.chilichef.NavigationItems.Notes.NotesFragment;
import com.ict.chilichef.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DesignFragment extends Fragment {


    int ID;
    private RecyclerView designRecycler;
    private ArrayList<Data> itemListDesign = new ArrayList<>();
    private Recycle_Fill_Adapter designAdapter;

    ImageButton retryBTN;
    private ProgressBar progressBar;
    boolean retry = false;
    LinearLayout checkNetLayout;
    private Bundle bundle;

    public DesignFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_design, container, false);

        Bundle bundle = getActivity().getIntent().getExtras();

        int ID_Cat = getArguments().getInt("Id_Cat", 0);

        ID = bundle.getInt("ItemID");

        designRecycler = (RecyclerView) v.findViewById(R.id.recycler_design);

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        checkNetLayout = (LinearLayout) v.findViewById(R.id.checkNetLayout);
        retryBTN = (ImageButton) v.findViewById(R.id.retry);


        if (HttpManager.checkNetwork(getActivity())) {

            FoodsFetch ff = new FoodsFetch(getActivity());
            ff.execute(ID_Cat);

        } else {

            TestInternet();
        }


        return v;
    }

    private class FoodsFetch extends AsyncTask<Integer, String, String> implements Recycle_Fill_Adapter.ClickListener {

        Context context;

        private FoodsFetch(Context context) {

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (HttpManager.checkNetwork(getActivity())) {

                progressBar.setVisibility(View.VISIBLE);
                checkNetLayout.setVisibility(View.GONE);

                int size = itemListDesign.size();
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        itemListDesign.remove(0);
                    }

                    designAdapter.notifyItemRangeRemoved(0, size);
                }

            } else {

                TestInternet();
            }

        }

        @Override
        protected String doInBackground(Integer... integers) {

            try {
                String jsonText = HttpManager.getCategoricalDesign(integers[0]);

                return jsonText;

            } catch (Exception e) {

                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.length() > 6) {

                JsonParserDataDesign jsonParserDataDesign = new JsonParserDataDesign();
                ArrayList<Data> dataModels = jsonParserDataDesign.parseJson(result);

                for (Data fdm : dataModels) {
                    itemListDesign.add(fdm);
                }


                designAdapter = new Recycle_Fill_Adapter(itemListDesign, getActivity());
                designAdapter.setClickListener(this);

                RecyclerView.LayoutManager fLayoutManger = new GridLayoutManager(getActivity(), 2);
                designRecycler.setLayoutManager(fLayoutManger);
                designRecycler.setItemAnimator(new DefaultItemAnimator());
                designRecycler.setAdapter(designAdapter);

                progressBar.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            } else if (result.equals("error") || result.isEmpty()) {

                //TestInternet();
            }
        }

        @Override
        public void itemClicked(View view, int position) {

        }

    }

    private void TestInternet() {

        checkNetLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        retryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (HttpManager.checkNetwork(getActivity())) {

                    bundle = getActivity().getIntent().getExtras();

                    int ID_Cat = getArguments().getInt("Id_Cat", 0);

                    FoodsFetch ff = new FoodsFetch(getActivity());
                    ff.execute(ID_Cat);

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
