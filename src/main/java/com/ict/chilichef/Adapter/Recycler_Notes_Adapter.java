package com.ict.chilichef.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ict.chilichef.Model.Data;
import com.ict.chilichef.Model.NotesModel;
import com.ict.chilichef.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Elham on 8/25/2017.
 */

public class Recycler_Notes_Adapter extends RecyclerView.Adapter<Recycler_Notes_Adapter.ViewHolder> {

    List<NotesModel> itemList = Collections.emptyList();
    private ClickListener clicklistener = null;

    Context context;


    public Recycler_Notes_Adapter(List<NotesModel> itemList, Context context) {

        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notes_layout, parent, false);
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
        holder.description.setText(itemList.get(position).getDescription());


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
    public void insert(int position, NotesModel data) {
        itemList.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(NotesModel data) {
        int position = itemList.indexOf(data);
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public interface ClickListener {
        public void itemClicked(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView title,description;



        ViewHolder(final View parent) {
            super(parent);

            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);

        }
    }
}








