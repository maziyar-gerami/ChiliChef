package com.ict.chilichef.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Manage.StringManager;
import com.ict.chilichef.Model.SearchRecipesModel;
import com.ict.chilichef.R;
import com.ict.chilichef.RecipesActivity;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by Elham on 4/16/2017.
 */

public class Recycler_MainRecipes_Adapter extends RecyclerView.Adapter<Recycler_MainRecipes_Adapter.ViewHolder> {

    List<SearchRecipesModel> itemListSearchRecepies = Collections.emptyList();

    private ClickListener clicklistener = null;
    Context context;


    public Recycler_MainRecipes_Adapter(List<SearchRecipesModel> itemListSearchRecepies, Context context) {

        this.itemListSearchRecepies = itemListSearchRecepies;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main_recipes, parent, false);

        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clicklistener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.FoodName.setText(itemListSearchRecepies.get(position).getTitle());
        holder.NumberLike.setText(StringManager.convertEnglishNumbersToPersian(String.valueOf(itemListSearchRecepies.get(position).getLikes())));


        Picasso.with(context).load(itemListSearchRecepies.get(position).getPic_Url()).into(holder.FoodImage);
    }

    @Override
    public int getItemCount() {
        return itemListSearchRecepies.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface ClickListener {
        public void itemSelectClicked(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView FoodName, NumberLike;
        ImageView FoodImage, Like;
        private LinearLayout mainLinear;


        ViewHolder(final View parent) {
            super(parent);

            FoodName = (TextView) parent.findViewById(R.id.user_imageTitle);
            FoodImage = (ImageView) parent.findViewById(R.id.user_foodImage);
            Like = (ImageView) parent.findViewById(R.id.user_DislikeImage);
            NumberLike = (TextView) parent.findViewById(R.id.user_numberOfLikes);
            mainLinear = (LinearLayout) parent.findViewById(R.id.recipes_main);
            mainLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    UserSessionManager session = new UserSessionManager(context);

                    if (!session.isUserLoggedIn()) {

                        Toast.makeText(context, "برای دسترسی به این بخش باید به حساب کاربری خود وارد شوید", Toast.LENGTH_LONG).show();

                    } else {

                        if (HttpManager.checkNetwork(context)) {

                            Intent intent = new Intent(parent.getContext(), RecipesActivity.class);
                            intent.putExtra("ItemName", itemListSearchRecepies.get(getPosition()).getTitle());
                            intent.putExtra("ItemImage", itemListSearchRecepies.get(getPosition()).getPic_Url());
                            intent.putExtra("ItemIDFood", itemListSearchRecepies.get(getPosition()).getId_Food());

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            context.startActivity(intent);

                        }

                    }

                }
            });
        }
    }
}


