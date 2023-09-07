package com.ict.chilichef.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ict.chilichef.Model.AddIngredientsModel;
import com.ict.chilichef.R;


import java.util.Collections;
import java.util.List;

/**
 * Created by Elham on 8/6/2017.
 */

public class Recycler_ProposeAddIng_Adapter extends RecyclerView.Adapter<Recycler_ProposeAddIng_Adapter.ViewHolder> {

    List<AddIngredientsModel> itemList = Collections.emptyList();


    private ClickListener clicklistener = null;
    Context context;


    public Recycler_ProposeAddIng_Adapter(List<AddIngredientsModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;

    }


    public void setClickListener(ClickListener clickListener) {
        this.clicklistener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_whatcook_final_ingredient, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        holder.name.setText(itemList.get(position).getName());
        holder.ID.setText(String.valueOf(itemList.get(position).getID()));


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public void setClickListener(View.OnClickListener onClickListener) {
    }

    public interface ClickListener {
        public void itemClicked(View view, int position);
    }

    public void remove(AddIngredientsModel data) {
        int position = itemList.indexOf(data);
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {


        TextView name;
        TextView ID;
        ImageView delete;


        ViewHolder(final View parent) {
            super(parent);

            name = (TextView) itemView.findViewById(R.id.ing_itemName);
            ID = (TextView) itemView.findViewById(R.id.ing_itemID);
            delete= (ImageView) itemView.findViewById(R.id.delete_icon);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(itemList.get(getPosition()));
                }
            });



        }

    }
}