package com.ict.chilichef.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ict.chilichef.Model.SearchIngredientsModel;
import com.ict.chilichef.Model.SendRecipe_IngredientModel;
import com.ict.chilichef.R;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Elham on 8/6/2017.
 */

public class Recycler_ProposeSearchIng_Adapter extends RecyclerView.Adapter<Recycler_ProposeSearchIng_Adapter.ViewHolder> {

    List<SearchIngredientsModel> itemList = Collections.emptyList();

    public ArrayList<SearchIngredientsModel> Checked_itemList = new ArrayList<>();
    private ClickListener clicklistener = null;
    Context context;


    public Recycler_ProposeSearchIng_Adapter(List<SearchIngredientsModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;

    }


    public void setClickListener(ClickListener clickListener) {
        this.clicklistener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_whatcook_search_ingredient, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.name.setText(itemList.get(position).getName());

        holder.checkBox.setOnCheckedChangeListener(null);

        holder.checkBox.setChecked(itemList.get(position).isSelected());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                itemList.get(holder.getAdapterPosition()).setSelected(isChecked);

                if(isChecked){

                    Checked_itemList.add(itemList.get(position));

                }else {
                    Checked_itemList.remove(itemList.get(position));
                }
            }
        });

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView name;
        CheckBox checkBox;
        RelativeLayout main_layout;
        ItemClickListener itemClickListener;

        ViewHolder(final View parent) {
            super(parent);

            name = (TextView) itemView.findViewById(R.id.ingSearch_item);
            checkBox = (CheckBox) itemView.findViewById(R.id.propose_checkBox);
            main_layout = (RelativeLayout) itemView.findViewById(R.id.main_layout);

          checkBox.setOnClickListener(this);



        }


        public void setItemClickListener(ItemClickListener ic) {

            this.itemClickListener=ic;

        }

        @Override
        public void onClick(View v) {

        }
    }
}