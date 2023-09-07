package com.ict.chilichef.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.advertiseModel;
import com.ict.chilichef.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by roshan on 2/27/2017.
 */

// this is for advertise in first page and the list that we us here might be empty

public class Recycler_advertise_Adapter extends RecyclerView.Adapter<Recycler_advertise_Adapter.ViewHolder> {

    List<advertiseModel> itemListADV = Collections.emptyList();
    private ClickListener clicklistener = null;

    Context context;


    public Recycler_advertise_Adapter(List<advertiseModel> itemListADV, Context context) {
        this.itemListADV = itemListADV;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_advertise_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clicklistener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        Picasso.with(context).load(itemListADV.get(position).getPic_Url()).into(holder.imageView);


        //animate(holder);
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display

        return itemListADV.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, advertiseModel data) {
        itemListADV.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(advertiseModel data) {
        int position = itemListADV.indexOf(data);
        itemListADV.remove(position);
        notifyItemRemoved(position);
    }

    public interface ClickListener {
        public void itemClicked(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        ImageView imageView;
        private LinearLayout main;


        ViewHolder(final View parent) {
            super(parent);


            imageView = (ImageView) itemView.findViewById(R.id.advertise_image);
            main = (LinearLayout) parent.findViewById(R.id.Layout_adv);
            main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    Toast.makeText(parent.getContext(), "Position:" + Integer.toString(getPosition()), Toast.LENGTH_SHORT).show();
                    if (clicklistener != null) {
                        clicklistener.itemClicked(v, getAdapterPosition());
                    }
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(HttpManager.checkNetwork(context)) {
                       Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(itemListADV.get(getPosition()).getTarget_Url()));
                       context.startActivity(i);
                   }

                }
            });
        }
    }
}








