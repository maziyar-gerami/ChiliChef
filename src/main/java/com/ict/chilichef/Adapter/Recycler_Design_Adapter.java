package com.ict.chilichef.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ict.chilichef.Model.Data;
import com.ict.chilichef.Model.DateModel;
import com.ict.chilichef.Model.DesignModel;
import com.ict.chilichef.Model.GalleryModel;
import com.ict.chilichef.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by mahshid on 11/30/2017.
 */

public class Recycler_Design_Adapter extends RecyclerView.Adapter<Recycler_Design_Adapter.ViewHolder>  {


    List<Data> itemListDesign = Collections.emptyList();

    private ClickListener clicklistener = null;

    Context context;

    public Recycler_Design_Adapter(List<Data> itemListDesign, Context context) {
        this.itemListDesign = itemListDesign;
        this.context = context;
    }


    @Override
    public Recycler_Design_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image_description_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;

    }


    public void setClickListener(ClickListener clickListener) {
        this.clicklistener = clickListener;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.title.setText(itemListDesign.get(position).getTitle());
        holder.description.setText(itemListDesign.get(position).getDescription());
        holder.imageView.setImageResource(itemListDesign.get(position).getImageId());

    }


    @Override
    public int getItemCount() {
        return itemListDesign.size();
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Data data) {
        int position = itemListDesign.indexOf(data);
        itemListDesign.remove(position);
        notifyItemRemoved(position);
    }


    public interface ClickListener {
        public void itemClicked(View view, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        CardView cv;
        TextView title, description;
        ImageView imageView;
        ImageButton Saved,UnSaved;
        private LinearLayout main;


        public ViewHolder(final View parent) {
            super(parent);

            cv = (CardView) parent.findViewById(R.id.cardView);
            title = (TextView) parent.findViewById(R.id.design_title);
            description = (TextView) parent.findViewById(R.id.design_description);
            imageView = (ImageView) parent.findViewById(R.id.show_image);
            Saved = (ImageButton) parent.findViewById(R.id.Bookmark);
//            UnSaved = (ImageButton) parent.findViewById(R.id.No_Bookmark);

            main = (LinearLayout) parent.findViewById(R.id.main);
            main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (clicklistener != null) {
                        clicklistener.itemClicked(v, getAdapterPosition());
                    }
                }
            });

        }
    }

}
