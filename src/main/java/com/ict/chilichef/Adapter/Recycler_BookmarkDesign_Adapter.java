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

import com.ict.chilichef.BottomBar.DesignTab.DesignDescriptionActivity;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.DesignBookmarkModel;
import com.ict.chilichef.R;
import com.ict.chilichef.RecipesActivity;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import static com.ict.chilichef.MainActivity.URL_DesignPics;

/**
 * Created by Elham on 4/16/2017.
 */

public class Recycler_BookmarkDesign_Adapter extends RecyclerView.Adapter<Recycler_BookmarkDesign_Adapter.ViewHolder> {

    List<DesignBookmarkModel> itemListRecepies = Collections.emptyList();

    private ClickListener clicklistener = null;
    Context context;


    public Recycler_BookmarkDesign_Adapter(List<DesignBookmarkModel> itemListRecepies, Context context) {

        this.itemListRecepies = itemListRecepies;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bookmark_design_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clicklistener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.title.setText(itemListRecepies.get(position).getTitle());

        Picasso.with(context).load(URL_DesignPics+itemListRecepies.get(position).getPic_Url().get(0)).into(holder.FoodImage);

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


        TextView title;
        ImageView FoodImage;
        private RelativeLayout mainRelative;


        ViewHolder(final View parent) {
            super(parent);

            title = (TextView) parent.findViewById(R.id.title);
            FoodImage = (ImageView) parent.findViewById(R.id.imageView);
            mainRelative = (RelativeLayout) parent.findViewById(R.id.main);
            mainRelative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    UserSessionManager session = new UserSessionManager(context);

                    if (!session.isUserLoggedIn()) {

                        Toast.makeText(context, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();

                    } else {

                        if (HttpManager.checkNetwork(context)) {

                            Intent intent = new Intent(parent.getContext(), DesignDescriptionActivity.class);
                            intent.putExtra("ItemTitle", itemListRecepies.get(getPosition()).getTitle());
//                            intent.putExtra("ItemImage", itemListRecepies.get(getPosition()).getFood_Pic_Url());
                            intent.putExtra("ItemID", itemListRecepies.get(getPosition()).getID_Design());

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
