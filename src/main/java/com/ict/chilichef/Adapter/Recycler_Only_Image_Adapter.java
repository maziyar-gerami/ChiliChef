package com.ict.chilichef.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.BottomBar.BbGalleryFragment;
import com.ict.chilichef.CommentGalleryActivity;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.GalleryModel;
import com.ict.chilichef.Model.SearchRecipesModel;
import com.ict.chilichef.R;
import com.ict.chilichef.RecipesActivity;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mahshid on 11/22/2017.
 */

public class Recycler_Only_Image_Adapter extends RecyclerView.Adapter<Recycler_Only_Image_Adapter.ViewHolder> {

    List<GalleryModel> itemListGallery = Collections.emptyList();

    private Recycler_Only_Image_Adapter.ClickListener clicklistener = null;
    Context context;


    public Recycler_Only_Image_Adapter(List<GalleryModel> itemListGallery, Context context) {
        this.itemListGallery = itemListGallery;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }



    public void setClickListener(Recycler_Only_Image_Adapter.ClickListener clickListener) {
        this.clicklistener = clickListener;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.FoodName.setText(itemListGallery.get(position).getFoodName());

        if (itemListGallery.get(position).getSaved() == 1)
            holder.saved.setVisibility(View.VISIBLE);

        Picasso.with(context).load(itemListGallery.get(position).getPic_Url()).into(holder.FoodImage);

    }



    @Override
    public int getItemCount() {
        return itemListGallery.size();
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    public interface ClickListener {
        public void itemSelectClicked(View view, int position);
    }




    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView FoodImage;
        ImageButton saved, unsaved;
        LinearLayout main_linear;



        ViewHolder(final View parent) {
            super(parent);

//            FoodName = (TextView) parent.findViewById(R.id.titleRecFood);
            FoodImage = (ImageView) parent.findViewById(R.id.show_image);
            saved = (ImageButton) parent.findViewById(R.id.bookmark_image_save);
            unsaved = (ImageButton) parent.findViewById(R.id.bookmark_image_unsave);

            main_linear = (LinearLayout) parent.findViewById(R.id.main);

            UserSessionManager session = new UserSessionManager(context);





            main_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    UserSessionManager session = new UserSessionManager(context);

                    if (!session.isUserLoggedIn()) {

                        Toast.makeText(context, "برای دسترسی به این بخش باید به حساب کاربری خود وارد شوید", Toast.LENGTH_LONG).show();

                    } else {

                        if (HttpManager.checkNetwork(context)) {
                            Intent intent = new Intent(context, CommentGalleryActivity.class);

                            intent.putExtra("PicFoodUrl", itemListGallery.get(getPosition()).getPic_Url());
                            intent.putExtra("PicFoodId", itemListGallery.get(getPosition()).getID_Pic());
                            intent.putExtra("PicFoodName", itemListGallery.get(getPosition()).getFoodName());
                            intent.putExtra("nComments", itemListGallery.get(getPosition()).getnComments());
                            intent.putExtra("nLikes", itemListGallery.get(getPosition()).getLikes());
                            intent.putExtra("UserImage", itemListGallery.get(getPosition()).getPic_url_user());
                            intent.putExtra("UserName", itemListGallery.get(getPosition()).getName());

//            intent.putExtra("ItemSaved", itemListGallery.get(getPosition()).getSaved());

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            context.startActivity(intent);


                        } else {

                            Toast.makeText(context, R.string.Disconnect, Toast.LENGTH_LONG).show();

                        }

                    }

                }
            });


            unsaved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    UserSessionManager session = new UserSessionManager(context);

                    if (!session.isUserLoggedIn()) {

                        Toast.makeText(context, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();


                    } else {
                        saved.setVisibility(View.VISIBLE);
                        unsaved.setVisibility(View.INVISIBLE);

                        HashMap<String, String> user = session.getUserDetails();
                        String IdUser = user.get(UserSessionManager.KEY_ID_USER);
                        Save save = new Save(context);
                        save.execute(Integer.valueOf(IdUser), itemListGallery.get(getPosition()).getID_Pic());


                    }
                }
            });



            saved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    UserSessionManager session = new UserSessionManager(context);

                    if (!session.isUserLoggedIn()) {

                        Toast.makeText(context, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();


                    } else {

                        unsaved.setVisibility(View.VISIBLE);
                        saved.setVisibility(View.INVISIBLE);

                        HashMap<String, String> user = session.getUserDetails();
                        String IdUser = user.get(UserSessionManager.KEY_ID_USER);


                        UnSave unSave = new UnSave(context);
                        unSave.execute(Integer.valueOf(IdUser), itemListGallery.get(getPosition()).getID_Pic());


                    }
                }
            });

        }
    }


    private class Save extends AsyncTask<Integer, String, String> {

        Context context;


        JSONObject object = new JSONObject();

        public Save(Context context) {
            object = new JSONObject();

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(Integer... integers) {

            String jsonText = HttpManager.galleryBookmark(integers[0], integers[1]);

            return jsonText;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //progress.dismiss();

        }

    }

    private class UnSave extends AsyncTask<Integer, String, String> {

        Context context;


        JSONObject object = new JSONObject();

        public UnSave(Context context) {
            object = new JSONObject();

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //progress = ProgressDialog.show(context, "Fetching Foods data",
            //"Please Wait...", true_email);

        }

        @Override
        protected String doInBackground(Integer... integers) {

            String jsonText = HttpManager.galleryUnBookmark(integers[0], integers[1]);

            return jsonText;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }


    }
}
