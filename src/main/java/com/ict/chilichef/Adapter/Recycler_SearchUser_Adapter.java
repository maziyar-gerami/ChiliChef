package com.ict.chilichef.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.SearchUsersModel;
import com.ict.chilichef.NavigationItems.Shared.SharedActivity;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.ict.chilichef.UsersManage.UsersActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Elham on 4/16/2017.
 */

public class Recycler_SearchUser_Adapter extends RecyclerView.Adapter<Recycler_SearchUser_Adapter.ViewHolder> {

    List<SearchUsersModel> itemListUser = Collections.emptyList();
    private ClickListener clicklistener = null;
    Context context;
    UserSessionManager session;
    String IdUser;
    boolean b = true;


    public Recycler_SearchUser_Adapter(List<SearchUsersModel> itemListUser, Context context) {

        this.itemListUser = itemListUser;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_follow_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clicklistener = clickListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        session = new UserSessionManager(context);

        HashMap<String, String> user = session.getUserDetails();
        IdUser = user.get(UserSessionManager.KEY_ID_USER);
        if (IdUser == null) {

            IdUser = "0";
            holder.follow_btn.setVisibility(View.GONE);
        }

        holder.name.setText(itemListUser.get(position).getName());

        Picasso.with(context).load(itemListUser.get(position).getPic_Url()).into((holder.user_image));


        if (itemListUser.get(position).getIsFollowing() == 0) {
            holder.follow_btn.setText("دنبال کردن");
            holder.follow_btn.setBackgroundColor(ContextCompat.getColor(context, (R.color.light_gray)));

        } else if (itemListUser.get(position).getIsFollowing() == 1) {


            holder.follow_btn.setText("دنبال میکنم");
            holder.follow_btn.setBackgroundColor(ContextCompat.getColor(context, (R.color.colorPrimaryDark)));

        } else if (itemListUser.get(position).getIsFollowing() == 2) {

            holder.follow_btn.setText("");
            holder.follow_btn.setBackgroundColor(ContextCompat.getColor(context, (R.color.white)));

        }

    }

    @Override
    public int getItemCount() {
        return itemListUser.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface ClickListener {
        public void itemSelectClicked(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        CardView cv;
        TextView score, name;
        ImageView user_image;
        TextView follow_btn;

        ViewHolder(final View parent) {
            super(parent);
            cv = (CardView) parent.findViewById(R.id.follow_cardView);
            name = (TextView) parent.findViewById(R.id.follow_name);
            user_image = (ImageView) parent.findViewById(R.id.follow_userImage);


            follow_btn = (TextView) parent.findViewById(R.id.follow_followBtn);


            follow_btn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {

                    session = new UserSessionManager(context);

                    if (!session.isUserLoggedIn()) {

                        Toast.makeText(context, "لطفا برای دسترسی به این بخش به حساب کاربری خود وارد شوید ", Toast.LENGTH_LONG).show();

                    } else {
                        HashMap<String, String> user = session.getUserDetails();
                        String IdUser = user.get(UserSessionManager.KEY_ID_USER);


                        int test = itemListUser.get(getPosition()).getId_User();
                        if (!(IdUser.equals(String.valueOf(test)))) {


                            if (itemListUser.get(getPosition()).getIsFollowing() == 0) {

                                FollowTask followTask = new FollowTask(context);
                                itemListUser.get(getPosition()).setIsFollowing(1);
                                test = itemListUser.get(getPosition()).getId_User();
                                followTask.execute(Integer.valueOf(IdUser), test);
                                follow_btn.setBackgroundColor(ContextCompat.getColor(context, (R.color.colorPrimaryDark)));
                                follow_btn.setText("دنبال میکنم");

                            } else if (itemListUser.get(getPosition()).getIsFollowing() == 1) {

                                UnFollowTask unFollowTask = new UnFollowTask(context);

                                test = itemListUser.get(getPosition()).getId_User();
                                unFollowTask.execute(Integer.valueOf(IdUser), test);
                                itemListUser.get(getPosition()).setIsFollowing(0);
                                follow_btn.setBackgroundColor(ContextCompat.getColor(context, (R.color.light_gray)));
                                follow_btn.setText("دنبال کردن");

                            }

                        }
                        if (itemListUser.get(getPosition()).getIsFollowing() == 2) {

                            Intent intent = new Intent(context, SharedActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                        }
                    }

                }
            });


            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    session = new UserSessionManager(context);

                    if (!session.isUserLoggedIn()) {

                        Toast.makeText(context, "لطفا برای دسترسی به این بخش به حساب کاربری خود وارد شوید ", Toast.LENGTH_LONG).show();

                    } else {
                        HashMap<String, String> user = session.getUserDetails();
                        String IdUser = user.get(UserSessionManager.KEY_ID_USER);

                        int test = itemListUser.get(getPosition()).getId_User();

                        if (!(IdUser.equals(String.valueOf(test)))) {

                            Intent intent = new Intent(context, UsersActivity.class);
                            intent.putExtra("IdUSER", test);
                            String picture = itemListUser.get(getPosition()).getName();
                            intent.putExtra("PicUSER", picture);
                            int isFollowing = itemListUser.get(getPosition()).getIsFollowing();
                            int follow = Integer.valueOf(isFollowing);
                            intent.putExtra("UserName", itemListUser.get(getPosition()).getName());
                            intent.putExtra("isFollowing", follow);
                            intent.putExtra("NumberOfFollower", itemListUser.get(getPosition()).getNumberOfFollowers());
                            intent.putExtra("NumberOfFollowings", itemListUser.get(getPosition()).getNumberOfFollowings());
                            intent.putExtra("isFollowing", follow);
                            intent.putExtra("NumberOfPic", itemListUser.get(getPosition()).getNumberOfPictures());
                            intent.putExtra("NumberOfRec", itemListUser.get(getPosition()).getNumberOfRecipes());
                            intent.putExtra("Score", itemListUser.get(getPosition()).getScore());
                            intent.putExtra("PIC_URL", itemListUser.get(getPosition()).getPic_Url());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {

                            Intent intent = new Intent(context, SharedActivity.class);
                            context.startActivity(intent);

                        }
                    }
                }

            });

        }
    }


    public class FollowTask extends AsyncTask<Integer, String, String> {

        Context context;

        private ProgressDialog progress;

        JSONObject object = new JSONObject();

        public FollowTask(Context context) {
            object = new JSONObject();

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... integers) {


            String jsonText = HttpManager.Follow(integers[0], integers[1]);

            return jsonText;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            try {
                JSONArray jsonarray = new JSONArray(result);
                object = jsonarray.getJSONObject(0);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (object.getInt("message") == 1) {

                    Toast.makeText(context, "Following Task have done successfully", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private class UnFollowTask extends AsyncTask<Integer, String, String> {

        Context context;

        private ProgressDialog progress;

        JSONObject object = new JSONObject();

        public UnFollowTask(Context context) {
            object = new JSONObject();

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... integers) {

            String jsonText = HttpManager.UnFollow(integers[0], integers[1]);

            return jsonText;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            try {
                JSONArray jsonarray = new JSONArray(result);
                object = jsonarray.getJSONObject(0);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (object.getInt("message") == 1) {

                    Toast.makeText(context, "UnFollowing Task have done successfully", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }


}



