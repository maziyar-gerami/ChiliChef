package com.ict.chilichef.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ict.chilichef.Model.SendRecipe_IngredientModel;
import com.ict.chilichef.R;


import java.util.Collections;
import java.util.List;

/**
 * Created by Elham on 8/6/2017.
 */

public class Recycler_SendRecipeIng_Adapter extends RecyclerView.Adapter<Recycler_SendRecipeIng_Adapter.ViewHolder> {

    List<SendRecipe_IngredientModel> itemList = Collections.emptyList();
    private ClickListener clicklistener = null;
    Context context;

    public Recycler_SendRecipeIng_Adapter(List<SendRecipe_IngredientModel> itemList, Context context) {
        this.itemList = itemList;
        this.context=context;

    }



    public void setClickListener(ClickListener clickListener) {
        this.clicklistener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sendrecipe_ingredient_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.number.setText(itemList.get(position).getAmount());
        holder.unit.setText(itemList.get(position).getUnit_Name());
        holder.name.setText(itemList.get(position).getIng_Name());

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
    public void remove(SendRecipe_IngredientModel data) {
        int position = itemList.indexOf(data);
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView number,unit,name;
        ImageButton delete;



        ViewHolder(final View parent) {
            super(parent);
            number = (TextView) itemView.findViewById(R.id.ingredient_num);
            unit = (TextView) itemView.findViewById(R.id.ingredient_unit);
            name = (TextView) itemView.findViewById(R.id.ingredient_name);
            delete= (ImageButton) itemView.findViewById(R.id.Delete_btn);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(itemList.get(getPosition()));
                }
            });


        }
    }
}
