package com.ict.chilichef.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.ict.chilichef.BottomBar.DesignTab.DesignDescriptionActivity;
import com.ict.chilichef.Model.Data;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by roshan on 2/27/2017.
 */

/**
 * Created by roshan on 12/3/2017.
 */

public class Recycle_Fill_Adapter extends RecyclerView.Adapter<Recycle_Fill_Adapter.ViewHolder> {

    List<Data> itemList = Collections.emptyList();
    private ClickListener clicklistener = null;

    Context context;
    UserSessionManager session;


    public Recycle_Fill_Adapter(List<Data> itemList, Context context) {

        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;

    }

    public void setClickListener(ClickListener clickListener) {
        this.clicklistener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.title.setText(itemList.get(position).getTitle());
//        holder.imageView.setImageResource(itemList.get(position).getImageId());

        Picasso.with(context).load(itemList.get(position).getPicUrl()).into(holder.imageView);

        //animate(holder);
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display

        return itemList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Data data) {
        itemList.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Data data) {
        int position = itemList.indexOf(data);
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public interface ClickListener {
        public void itemClicked(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView title;
        ImageView imageView;
        private LinearLayout main;


        ViewHolder(final View parent) {
            super(parent);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            main = (LinearLayout) parent.findViewById(R.id.main);

            main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    session = new UserSessionManager(context);
                        if(session.isUserLoggedIn()) {

                            Intent i = new Intent(parent.getContext(), DesignDescriptionActivity.class);
                            i.putExtra("ItemID", itemList.get(getPosition()).getId_cat());
                            i.putExtra("ItemTitle", itemList.get(getPosition()).getTitle());
                            context.startActivity(i);

                        }else {

                            Toast.makeText(context,R.string.plzLogIn, Toast.LENGTH_SHORT).show();
                        }

//                    if (clicklistener != null) {
//                        clicklistener.itemClicked(v, getAdapterPosition());
//                    }
                }
            });
        }
    }
}









