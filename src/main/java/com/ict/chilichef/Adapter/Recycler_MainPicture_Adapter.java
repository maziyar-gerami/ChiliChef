package com.ict.chilichef.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ict.chilichef.CommentGalleryActivity;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.SearchPicturesModel;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * Created by NP on 5/30/2017.
 */

public class Recycler_MainPicture_Adapter extends RecyclerView.Adapter<Recycler_MainPicture_Adapter.ViewHolder> {

    List<SearchPicturesModel> itemListPicture = Collections.emptyList();

    private ClickListener clicklistener = null;
    UserSessionManager session;


    Context context;

    public Recycler_MainPicture_Adapter(List<SearchPicturesModel> itemListPicture, Context context) {
        this.itemListPicture = itemListPicture;
        this.context = context;
    }

    @Override
    public Recycler_MainPicture_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main_picture_layout, parent, false);
        Recycler_MainPicture_Adapter.ViewHolder holder = new Recycler_MainPicture_Adapter.ViewHolder(v);
        return holder;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clicklistener = clickListener;
    }

    @Override
    public void onBindViewHolder(final Recycler_MainPicture_Adapter.ViewHolder holder, int position) {

        Picasso.with(context).load(itemListPicture.get(position).getPicUrlWriter())
                .into((holder.imageSenderFood));

        Picasso.with(context).load(itemListPicture.get(position).getPicUrl())
                .into((holder.imageFood));

    }

    public interface ClickListener {
        void itemClicked(View view, int position);

    }

    @Override
    public int getItemCount() {
        return itemListPicture.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageFood, imageSenderFood;

        private RelativeLayout mainLin;

        ViewHolder(final View parent) {
            super(parent);

            imageFood = (ImageView) parent.findViewById(R.id.image_food);
            mainLin = (RelativeLayout) parent.findViewById(R.id.main_relative);
            imageSenderFood = (ImageView) parent.findViewById(R.id.imageSenderFood);

            mainLin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    session = new UserSessionManager(context);

                    if (!session.isUserLoggedIn()) {

                        Toast.makeText(context, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();
                    } else {

                        if (HttpManager.checkNetwork(context)) {

                            Intent intent = new Intent(parent.getContext(), CommentGalleryActivity.class);
                            intent.putExtra("PicFoodUrl", itemListPicture.get(getPosition()).getPicUrl());
                            intent.putExtra("PicFoodId", itemListPicture.get(getPosition()).getID_Pic());
                            intent.putExtra("PicFoodName", itemListPicture.get(getPosition()).getFoodName());
                            intent.putExtra("UserName", itemListPicture.get(getPosition()).getWriterName());
                            intent.putExtra("UserImage", itemListPicture.get(getPosition()).getPicUrlWriter());
                            intent.putExtra("nLikes",String.valueOf( itemListPicture.get(getPosition()).getLikes()));
                            intent.putExtra("nComments", itemListPicture.get(getPosition()).getnComments());
                            intent.putExtra("userID", itemListPicture.get(getPosition()).getWriterID());
                            intent.putExtra("isLiked",String.valueOf(itemListPicture.get(getPosition()).getLiked()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            context.startActivity(intent);
                        }
                    }
                }
            });

        }
    }

}
