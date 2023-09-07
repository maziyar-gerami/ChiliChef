package com.ict.chilichef.NavigationItems.Notes;


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

import com.ict.chilichef.Adapter.Recycler_Food_Adapter;
import com.ict.chilichef.Adapter.Recycler_Notes_Adapter;
import com.ict.chilichef.JsonParser.JsonParserFoods;
import com.ict.chilichef.JsonParser.JsonParserNotes;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.FoodsCategoricalModel;
import com.ict.chilichef.Model.NotesModel;
import com.ict.chilichef.NavigationItems.Categories.CategoriesFragment;
import com.ict.chilichef.R;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {

    int ID;
    private RecyclerView notesRecycler;
    private ArrayList<NotesModel> itemListNotes = new ArrayList<>();
    private Recycler_Notes_Adapter notesAdapter;

    ImageButton retryBTN;
    private ProgressBar progressBar;
    boolean retry = false;
    LinearLayout checkNetLayout;
    private Bundle bundle;

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_notes, container, false);

        bundle = getActivity().getIntent().getExtras();

        int ID_Cat = getArguments().getInt("Id_Cat", 0);

        ID = bundle.getInt("ItemID");

        notesRecycler = (RecyclerView) v.findViewById(R.id.recycler_notes_tab);

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

    private class FoodsFetch extends AsyncTask<Integer, String, String> implements Recycler_Notes_Adapter.ClickListener {

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


                int size = itemListNotes.size();
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        itemListNotes.remove(0);
                    }
                    notesAdapter.notifyItemRangeRemoved(0, size);

                } else {

                    TestInternet();
                }
            }

        }

        @Override
        protected String doInBackground(Integer... integers) {

            try {
                String jsonText = HttpManager.getNotes(integers[0]);

                return jsonText;

            } catch (Exception e) {

                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (!(result.equals("error"))) {

                JsonParserNotes jsonParserNotes = new JsonParserNotes();
                ArrayList<NotesModel> notesModels = jsonParserNotes.parseJson(result);

                for (NotesModel fdm : notesModels) {
                    itemListNotes.add(fdm);
                }


                notesAdapter = new Recycler_Notes_Adapter(itemListNotes, getActivity());
                notesAdapter.setClickListener(this);

                RecyclerView.LayoutManager fLayoutManger = new GridLayoutManager(getActivity(), 1);
                notesRecycler.setLayoutManager(fLayoutManger);
                notesRecycler.setItemAnimator(new DefaultItemAnimator());
                notesRecycler.setAdapter(notesAdapter);

                progressBar.setVisibility(View.GONE);
                checkNetLayout.setVisibility(View.GONE);

            } else if (result.equals("error")) {

                TestInternet();
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
