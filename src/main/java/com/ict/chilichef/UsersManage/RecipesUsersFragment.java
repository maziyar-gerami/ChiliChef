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

import com.ict.chilichef.Adapter.Recycler_UserRecipes_Adapter;
import com.ict.chilichef.JsonParser.JsonParserUserRecipe;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.FoodsUserModel;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipesUsersFragment extends Fragment {

    private RecyclerView recyclerUserRec;
    private Recycler_UserRecipes_Adapter RecAdapter;
    private List<FoodsUserModel> itemListRecUser;
    UserSessionManager session;
    private int ID;
    private Bundle bundle;

    public RecipesUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recipes_users, container, false);

        bundle = getActivity().getIntent().getExtras();
        session = new UserSessionManager(getActivity());
        ID = bundle.getInt("IdUSER");

        itemListRecUser = new ArrayList<>();

        recyclerUserRec = (RecyclerView) v.findViewById(R.id.user_recycler_Recipes);

        if (HttpManager.checkNetwork(getActivity())) {

            RecUserFetch recUserFetch = new RecUserFetch(getActivity());
            recUserFetch.execute(ID);


        } else {
            Toast.makeText(getActivity(), R.string.Disconnect, Toast.LENGTH_SHORT).show();
        }

        return v;

    }

    private class RecUserFetch extends AsyncTask<Integer, String, String> implements Recycler_UserRecipes_Adapter.ClickListener {

        Context context;

        public RecUserFetch(Context context) {

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (HttpManager.checkNetwork(getActivity())) {

            }


        }

        @Override
        protected String doInBackground(Integer... integers) {

            try {
                String jsonText = HttpManager.getUserRecipes(integers[0]);

                return jsonText;

            } catch (Exception e) {
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);

            if (result.length() > 6) {
                JsonParserUserRecipe jsonParserUserRecipe = new JsonParserUserRecipe();
                ArrayList<FoodsUserModel> foodsUserModel = jsonParserUserRecipe.parseJson(result);

                for (FoodsUserModel GM : foodsUserModel) {
                    itemListRecUser.add(GM);
                }

                RecAdapter = new Recycler_UserRecipes_Adapter(itemListRecUser, getActivity());
                RecAdapter.setClickListener(this);
                RecyclerView.LayoutManager ELayoutManger = new GridLayoutManager(getActivity(), 3);
                recyclerUserRec.setLayoutManager(ELayoutManger);
                recyclerUserRec.setItemAnimator(new DefaultItemAnimator());
                recyclerUserRec.setAdapter(RecAdapter);


            } else if (result.equals("error") || result.isEmpty()) {

                Toast.makeText(getActivity(), R.string.Disconnect, Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void itemSelectClicked(View view, int position) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }


}
