package com.ict.chilichef.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ict.chilichef.Manage.StringManager;
import com.ict.chilichef.Model.IngredientsModel;
import com.ict.chilichef.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by roshan on 2/27/2017.
 */

public class Recycler_Ingredient_Adapter extends RecyclerView.Adapter<Recycler_Ingredient_Adapter.ViewHolder> {

    List<IngredientsModel> itemList = Collections.emptyList();
//    private ClickListener clicklistener = null;
    Context context;
    ImageView dot;


    public Recycler_Ingredient_Adapter(List<IngredientsModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ingredient_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.number.setText(StringManager.convertEnglishNumbersToPersian(itemList.get(position).getAmount()));
        holder.unit.setText(itemList.get(position).getUnit_Name());
        holder.name.setText(itemList.get(position).getIng_Name());

        if(itemList.get(position).getUnit_Name().equals(" ")){

            dot.setVisibility(View.GONE);
            holder.name.setTextSize(18);
            holder.name.setTextColor(Color.BLACK);
        }



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



    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView number,unit,name;




        ViewHolder(final View parent) {
            super(parent);

            number = (TextView) itemView.findViewById(R.id.number);
            unit = (TextView) itemView.findViewById(R.id.unit);
            name = (TextView) itemView.findViewById(R.id.name);
            dot=(ImageView) itemView.findViewById(R.id.Ingredient_dot);


        }
    }
}








