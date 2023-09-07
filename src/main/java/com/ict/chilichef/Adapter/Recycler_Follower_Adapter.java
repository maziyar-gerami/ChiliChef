package com.ict.chilichef.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.FriendsModel;
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

public class Recycler_Follower_Adapter extends RecyclerView.Adapter<Recycler_Follower_Adapter.ViewHolder> {

    private List<FriendsModel> itemListFollow = Collections.emptyList();
    private ClickListener clicklistener = null;
    private Context context;
    private String text;
    private int color;
    UserSessionManager session;


    public Recycler_Follower_Adapter(List<FriendsModel> itemListFollow, Context context) {

        this.itemListFollow = itemListFollow;
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
    public void onBindViewHolder(ViewHolder holder, int position) {

        session = new UserSessionManager(context);
        HashMap<String, String> user = session.getUserDetails();
        String IdUser = user.get(UserSessionManager.KEY_ID_USER);

//        holder.score.setText(String.valueOf(itemListFollow.get(position).getScore()));
        holder.name.setText(itemListFollow.get(position).getName_Friend());
        holder.follow_btn.setText(itemListFollow.get(position).getText());
        holder.follow_btn.setBackgroundColor(itemListFollow.get(position).getColor());

        if (itemListFollow.get(position).getID_Friend() == Integer.valueOf(IdUser)) {
            holder.follow_btn.setVisibility(View.INVISIBLE);
        }

        Picasso.with(context).load(itemListFollow.get(position).getPicUrl_Friend())
                .into((holder.user_image));

        if (itemListFollow.get(position).getIsFollowing()==0) {
            holder.follow_btn.setText("دنبال کردن");
            holder.follow_btn.setBackgroundColor(ContextCompat.getColor(context, (R.color.light_gray)));
        }else if(itemListFollow.get(position).getIsFollowing()==1) {


            holder.follow_btn.setText("دنبال میکنم");
            holder.follow_btn.setBackgroundColor(ContextCompat.getColor(context, (R.color.colorPrimaryDark)));
        }
    }

    @Override
    public int getItemCount() {
        return itemListFollow.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface ClickListener {
        public void itemSelectClicked(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView name;
        private ImageView user_image;
        private TextView follow_btn;
        LinearLayout mainFollowLin;


        ViewHolder(final View parent) {
            super(parent);

            name = (TextView) itemView.findViewById(R.id.follow_name);
            user_image = (ImageView) itemView.findViewById(R.id.follow_userImage);
            mainFollowLin= (LinearLayout) itemView.findViewById(R.id.main_FollowLin);

            follow_btn = (TextView) itemView.findViewById(R.id.follow_followBtn);
            follow_btn.setText(text);
            follow_btn.setBackgroundColor(color);


            follow_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (HttpManager.checkNetwork(context)) {

                        UserSessionManager session = new UserSessionManager(context);
                        HashMap<String, String> user = session.getUserDetails();
                        String IdUser = user.get(UserSessionManager.KEY_ID_USER);

                        if (itemListFollow.get(getPosition()).getIsFollowing() == 0) {

                            FollowTask followTask = new FollowTask(context);
                            itemListFollow.get(getPosition()).setIsFollowing(1);
                            int test = itemListFollow.get(getPosition()).getID_Friend();
                            followTask.execute(Integer.valueOf(IdUser), test);
                            follow_btn.setBackgroundColor(ContextCompat.getColor(context, (R.color.colorPrimaryDark)));
                            follow_btn.setText("دنبال میکنم");

                        } else if (itemListFollow.get(getPosition()).getIsFollowing() == 1) {

                            UnFollowTask unFollowTask = new UnFollowTask(context);

                            int test = itemListFollow.get(getPosition()).getID_Friend();
                            unFollowTask.execute(Integer.valueOf(IdUser), test);
                            itemListFollow.get(getPosition()).setIsFollowing(0);
                            follow_btn.setBackgroundColor(ContextCompat.getColor(context, (R.color.light_gray)));
                            follow_btn.setText("دنبال کردن");

                        }

                    }else {

                        Toast.makeText(context, R.string.Disconnect, Toast.LENGTH_LONG).show();

                    }
                }
            });


            mainFollowLin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    UserSessionManager session = new UserSessionManager(context);
                    HashMap<String, String> user = session.getUserDetails();
                    String IdUser = user.get(UserSessionManager.KEY_ID_USER);

                    int test = itemListFollow.get(getPosition()).getID_Friend();

                    if (itemListFollow.get(getPosition()).getID_Friend() == Integer.valueOf(IdUser)) {
                        Intent intent = new Intent(context, SharedActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {

                        Intent intent = new Intent(context, UsersActivity.class);
                        intent.putExtra("IdUSER", test);
                        int isFollowing = itemListFollow.get(getPosition()).getIsFollowing();
                        intent.putExtra("isFollowing", isFollowing);
                        intent.putExtra("NumberOfFollower", itemListFollow.get(getPosition()).getNumberOfFollowers());
                        intent.putExtra("NumberOfFollowings", itemListFollow.get(getPosition()).getNumberOfFollowings());
                        intent.putExtra("NumberOfPic", itemListFollow.get(getPosition()).getNumberOfPictures());
                        intent.putExtra("NumberOfRec", itemListFollow.get(getPosition()).getNumberOfRecipes());
                        intent.putExtra("Score", itemListFollow.get(getPosition()).getScore());
                        intent.putExtra("isFollowing", isFollowing);
                        String Name = itemListFollow.get(getPosition()).getName_Friend();
                        intent.putExtra("PicUSER", Name);
                        String picture =itemListFollow.get(getPosition()).getPicUrl_Friend();
                        intent.putExtra("PIC_URL",picture);

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }
                }
            });


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

//            progress = ProgressDialog.show(context, "Fetching Foods data",
//                    "Please Wait...", true_email);

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

                        Toast.makeText(context, "Following Task have done successfuly", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//            progress.dismiss();


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

                //progress = ProgressDialog.show(context, "Fetching Foods data",
                //"Please Wait...", true_email);

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

                        Toast.makeText(context, "UnFollowing Task have done successfuly", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //progress.dismiss();


            }


        }
    }

}


