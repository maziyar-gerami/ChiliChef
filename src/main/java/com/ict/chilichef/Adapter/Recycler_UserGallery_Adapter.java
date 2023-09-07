package com.ict.chilichef.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.CommentGalleryActivity;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Manage.StringManager;
import com.ict.chilichef.Model.GalleryModel;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Recycler_UserGallery_Adapter extends RecyclerView.Adapter<Recycler_UserGallery_Adapter.ViewHolder> {


    List<GalleryModel> itemListUser = Collections.emptyList();
    private ClickListener clicklistener = null;
    Context context;
    UserSessionManager session;
    boolean b = true;
    String num;


    public Recycler_UserGallery_Adapter(List<GalleryModel> itemListUser, Context context) {
        this.itemListUser = itemListUser;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_usergallery_layout, parent, false);

        ViewHolder holderUser = new ViewHolder(v);
        return holderUser;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clicklistener = clickListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.NumLikes.setText(StringManager.convertEnglishNumbersToPersian(String.valueOf(itemListUser.get(position).getLikes())));
        num = StringManager.convertEnglishNumbersToPersian(String.valueOf(itemListUser.get(position).getLikes()));

        holder.ImageTitle.setText(itemListUser.get(position).getFoodName());

        Picasso.with(context).load(itemListUser.get(position).getPic_Url()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return itemListUser.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface ClickListener {
        void itemOnClicked(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;

        ImageView imageView, Like, DisLike;
        TextView ImageTitle, NumLikes;
        private LinearLayout main;


        ViewHolder(final View parent) {
            super(parent);
            cv = (CardView) parent.findViewById(R.id.user_cardView);
            imageView = (ImageView) parent.findViewById(R.id.user_foodImage);
            DisLike = (ImageView) parent.findViewById(R.id.user_DislikeImage);
            ImageTitle = (TextView) parent.findViewById(R.id.user_imageTitle);
            NumLikes = (TextView) parent.findViewById(R.id.user_numberOfLikes);
            main = (LinearLayout) parent.findViewById(R.id.user_main);
            main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (HttpManager.checkNetwork(context)) {
//                    first we need an activity for each picture to send comment ...
                        Intent intent = new Intent(parent.getContext(), CommentGalleryActivity.class);
                        intent.putExtra("PicFoodUrl", itemListUser.get(getPosition()).getPic_Url());
                        intent.putExtra("PicFoodId", itemListUser.get(getPosition()).getID_Pic());
                        intent.putExtra("PicFoodName", itemListUser.get(getPosition()).getFoodName());
                        intent.putExtra("UserName", itemListUser.get(getPosition()).getName());
                        intent.putExtra("UserImage", itemListUser.get(getPosition()).getPic_url_user());
                        intent.putExtra("nLikes", NumLikes.getText().toString());
                        intent.putExtra("nComments", itemListUser.get(getPosition()).getnComments());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);


                    } else {
                        Toast.makeText(context, R.string.Disconnect, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}
