package com.ict.chilichef.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.CommentGalleryActivity;
import com.ict.chilichef.Model.SearchPicturesModel;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by Elham on 4/16/2017.
 */

public class Recycler_SearchImage_Adapter extends RecyclerView.Adapter<Recycler_SearchImage_Adapter.ViewHolder> {

    List<SearchPicturesModel> itemListSearchImage = Collections.emptyList();

    private ClickListener clicklistener = null;
    Context context;

    private   EditText report_txt ;
    private   Button report_btn ;


    public Recycler_SearchImage_Adapter(List<SearchPicturesModel> itemListSearchImage, Context context) {

        this.itemListSearchImage = itemListSearchImage;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_gallery, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clicklistener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.Image_title.setText(itemListSearchImage.get(position).getFoodName());
        Picasso.with(context).load(itemListSearchImage.get(position).getPicUrl()).into(holder.showImage);

    }

    @Override
    public int getItemCount() {
        return itemListSearchImage.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface ClickListener {
        public void itemSelectClicked(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView Image_title;
        ImageView showImage;
        private LinearLayout mainLin;
        UserSessionManager session = new UserSessionManager(context);


        ViewHolder(final View parent) {
            super(parent);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            showImage = (ImageView) itemView.findViewById(R.id.imageView);
            Image_title = (TextView) itemView.findViewById(R.id.title);
            mainLin = (LinearLayout) parent.findViewById(R.id.main_search);
            mainLin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!session.isUserLoggedIn()) {

                        Toast.makeText(context, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();


                    } else {
                        Intent i = new Intent(context, CommentGalleryActivity.class);
                        i.putExtra("nLikes", String.valueOf(itemListSearchImage.get(getPosition()).getLikes()));
                        i.putExtra("nComments", itemListSearchImage.get(getPosition()).getnComments());
                        i.putExtra("UserName", itemListSearchImage.get(getPosition()).getWriterName());
                        i.putExtra("UserImage", itemListSearchImage.get(getPosition()).getPicUrlWriter());
                        i.putExtra("PicFoodUrl", itemListSearchImage.get(getPosition()).getPicUrl());
                        i.putExtra("PicFoodId", itemListSearchImage.get(getPosition()).getID_Pic());
                        i.putExtra("PicFoodName", itemListSearchImage.get(getPosition()).getFoodName());
                        i.putExtra("isLiked",String.valueOf(itemListSearchImage.get(getPosition()).getLiked()));
                        context.startActivity(i);
                    }
                }
            });


        }


    }


}
