package com.ict.chilichef.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.Manage.StringManager;
import com.ict.chilichef.Model.FoodsCategoricalModel;
import com.ict.chilichef.R;
import com.ict.chilichef.RecipesActivity;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;


/**
 * Created by NP on 5/30/2017.
 */

public class Recycler_Food_Adapter extends RecyclerView.Adapter<Recycler_Food_Adapter.ViewHolder> {

    List<FoodsCategoricalModel> itemListFood = Collections.emptyList();

    private ClickListener clicklistener = null;

    Context context;
    UserSessionManager session;

    public Recycler_Food_Adapter(List<FoodsCategoricalModel> itemListFood, Context context) {
        this.itemListFood = itemListFood;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_food_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clicklistener = clickListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        holder.nameFood.setText(itemListFood.get(position).getTitle());
        holder.nameSenderFood.setText(itemListFood.get(position).getWriterName());
        holder.countLike.setText(StringManager.convertEnglishNumbersToPersian(String.valueOf(itemListFood.get(position).getLikes())));


        Picasso.with(context).load(itemListFood.get(position).getWriter_Pic_url())
                .into((holder.imageSenderFood));

        Picasso.with(context).load(itemListFood.get(position).getFood_Pic_Url())
                .into((holder.imageFood));

    }

    public interface ClickListener {
        void itemClicked(View view, int position);

    }

    @Override
    public int getItemCount() {
        return itemListFood.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardViewFood;
        TextView nameFood, nameSenderFood, countLike;
        ImageView imageFood, imageSenderFood;
        ImageButton like, full_like;
        private LinearLayout mainLin;

        ViewHolder(final View parent) {
            super(parent);
            cardViewFood = (CardView) itemView.findViewById(R.id.CardViewFood);
            nameFood = (TextView) itemView.findViewById(R.id.titleFood);
            nameSenderFood = (TextView) itemView.findViewById(R.id.name_sender_recipes);
            countLike = (TextView) itemView.findViewById(R.id.count_Like);
            imageFood = (ImageView) itemView.findViewById(R.id.image_food);
            imageSenderFood = (ImageView) itemView.findViewById(R.id.image_sender_recipes);
            like = (ImageButton) itemView.findViewById(R.id.Like_food);
//            full_like = (ImageButton) itemView.findViewById(R.id.full_Like_food);
            mainLin = (LinearLayout) parent.findViewById(R.id.Layout_food);
            mainLin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    session = new UserSessionManager(context);

                    if (!session.isUserLoggedIn()) {

                        Toast.makeText(context, "لطفا برای دسترسی به این بخش به حساب کاربری خود وارد شوید ", Toast.LENGTH_LONG).show();

                    } else {

                        Intent intent = new Intent(parent.getContext(), RecipesActivity.class);
                        intent.putExtra("ItemName", itemListFood.get(getPosition()).getTitle());
                        intent.putExtra("ItemImage", itemListFood.get(getPosition()).getFood_Pic_Url());
                        intent.putExtra("ItemIDFood", itemListFood.get(getPosition()).getId_Food());

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);
                    }
                }
            });

        }

    }





}
