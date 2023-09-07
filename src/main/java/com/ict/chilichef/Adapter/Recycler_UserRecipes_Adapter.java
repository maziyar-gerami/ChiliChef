package com.ict.chilichef.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.FoodsUserModel;
import com.ict.chilichef.R;
import com.ict.chilichef.RecipesActivity;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by Elham on 4/16/2017.
 */

public class Recycler_UserRecipes_Adapter extends RecyclerView.Adapter<Recycler_UserRecipes_Adapter.ViewHolder> {

    List<FoodsUserModel> itemListRecepies = Collections.emptyList();

    private ClickListener clicklistener = null;
    Context context;


    public Recycler_UserRecipes_Adapter(List<FoodsUserModel> itemListRecepies, Context context) {

        this.itemListRecepies = itemListRecepies;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_recipes, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clicklistener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.FoodName.setText(itemListRecepies.get(position).getTitle());

        Picasso.with(context).load(itemListRecepies.get(position).getFood_Pic_Url()).into(holder.FoodImage);
    }

    @Override
    public int getItemCount() {
        return itemListRecepies.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface ClickListener {
        public void itemSelectClicked(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView FoodName;
        ImageView FoodImage;
        private RelativeLayout mainRelative;


        ViewHolder(final View parent) {
            super(parent);

            FoodName = (TextView) parent.findViewById(R.id.user_titleRecFood);
            FoodImage = (ImageView) parent.findViewById(R.id.user_image_recFood);
            mainRelative = (RelativeLayout) parent.findViewById(R.id.user_Layout_SearchFood);
            mainRelative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    UserSessionManager session = new UserSessionManager(context);

                    if (!session.isUserLoggedIn()) {

                        Toast.makeText(context, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();

                    } else {

                        if (HttpManager.checkNetwork(context)) {

                            Intent intent = new Intent(parent.getContext(), RecipesActivity.class);
                            intent.putExtra("ItemName", itemListRecepies.get(getPosition()).getTitle());
                            intent.putExtra("ItemImage", itemListRecepies.get(getPosition()).getFood_Pic_Url());
                            intent.putExtra("ItemIDFood", itemListRecepies.get(getPosition()).getId_Food());

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            context.startActivity(intent);
                        }
                        else {
                            Toast.makeText(context, R.string.Disconnect, Toast.LENGTH_LONG).show();

                        }
                    }
                }
            });
        }
    }

}
