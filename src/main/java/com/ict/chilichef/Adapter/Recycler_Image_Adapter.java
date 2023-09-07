package com.ict.chilichef.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.CommentGalleryActivity;
import com.ict.chilichef.CommentGalleryFixActivity;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Manage.StringManager;
import com.ict.chilichef.Model.DateModel;
import com.ict.chilichef.Model.DateParser;
import com.ict.chilichef.Model.GalleryModel;
import com.ict.chilichef.NavigationItems.Shared.SharedActivity;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.ict.chilichef.UsersManage.UsersActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by roshan on 11/12/2017.
 */

public class Recycler_Image_Adapter extends RecyclerView.Adapter<Recycler_Image_Adapter.ViewHolder> {

    private List<GalleryModel> itemListImage = Collections.emptyList();

    private ClickListener clicklistener = null;
    Context context;
    UserSessionManager session;
    //    TextView likes_number;
    private EditText report_txt;
    private Button report_btn;
    //    private TextView likes_number;
    boolean b = true;
    String likes;


    public Recycler_Image_Adapter(List<GalleryModel> itemListImage, Context context) {

        this.itemListImage = itemListImage;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clicklistener = clickListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (b) {
            holder.likes_number.setText(StringManager.convertEnglishNumbersToPersian(String.valueOf(itemListImage.get(position).getLikes())));
        } else {
            holder.likes_number.setText(StringManager.convertEnglishNumbersToPersian(likes));
        }
        holder.username.setText(itemListImage.get(position).getName());
        holder.Image_title.setText(itemListImage.get(position).getFoodName());
        holder.Description.setText(itemListImage.get(position).getDescription());

        holder.comment_number.setText(StringManager.convertEnglishNumbersToPersian(String.valueOf(itemListImage.get(position).getnComments())));


//        if (itemListImage.get(position).getLiked() == 1)
//            holder.full_like.setVisibility(View.VISIBLE);
        if (itemListImage.get(position).getLiked() == 1) {

            holder.like.setImageResource(R.drawable.ic_likea);
        } else {

            holder.like.setImageResource(R.drawable.ic_like);
        }


        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session = new UserSessionManager(context);

                if (!session.isUserLoggedIn()) {
//
                    Toast.makeText(context, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();


                } else {
                    HashMap<String, String> user = session.getUserDetails();
                    String IdUser = user.get(UserSessionManager.KEY_ID_USER);

                    if (itemListImage.get(position).getLiked() == 1) {
//                    Toast.makeText(activity,"Selected Row : "+position,Toast.LENGTH_SHORT).show();
                        itemListImage.get(position).setLiked(0);
                        holder.like.setImageResource(R.drawable.ic_like);
                        UnLike unLike = new UnLike(context);
                        unLike.execute(Integer.valueOf(IdUser), itemListImage.get(position).getID_Pic());

                        String a = holder.likes_number.getText().toString();
                        int A = Integer.parseInt(a);
                        int ab = A - 1;

                        likes = StringManager.convertEnglishNumbersToPersian(String.valueOf(ab));

                        holder.likes_number.setText(StringManager.convertEnglishNumbersToPersian(likes));
                    } else {
                        holder.like.setImageResource(R.drawable.ic_likea);
                        itemListImage.get(position).setLiked(1);
                        Like like = new Like(context);
                        like.execute(Integer.valueOf(IdUser), itemListImage.get(position).getID_Pic());

                        String a = holder.likes_number.getText().toString();
                        int A = Integer.parseInt(a);
                        int ab = A + 1;
                        likes = StringManager.convertEnglishNumbersToPersian(String.valueOf(ab));
                        holder.likes_number.setText(StringManager.convertEnglishNumbersToPersian(likes));

                    }

                }
                b = false;

            }
        });


//        if (itemListImage.get(position).getSaved() == 1){
//            holder.Save.setVisibility(View.VISIBLE);
////            holder.UnSave.setVisibility(View.GONE);
//        }else {
//            holder.UnSave.setVisibility(View.VISIBLE);
//            holder.Save.setVisibility(View.GONE);
//        }

///
        if (itemListImage.get(position).getSaved() == 1) {

            holder.Save.setImageResource(R.drawable.ic_bookmark_a);
        } else {

            holder.Save.setImageResource(R.drawable.ic_bookmark);
        }


        holder.Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session = new UserSessionManager(context);

                if (!session.isUserLoggedIn()) {
//
                    Toast.makeText(context, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();


                } else {
                    HashMap<String, String> user = session.getUserDetails();
                    String IdUser = user.get(UserSessionManager.KEY_ID_USER);

                    if (itemListImage.get(position).getSaved() == 1) {
//                    Toast.makeText(activity,"Selected Row : "+position,Toast.LENGTH_SHORT).show();
                        itemListImage.get(position).setSaved(0);
                        holder.Save.setImageResource(R.drawable.ic_bookmark);
                        UnSave unSave = new UnSave(context);
                        unSave.execute(Integer.valueOf(IdUser), itemListImage.get(position).getID_Pic());
                    } else {
                        holder.Save.setImageResource(R.drawable.ic_bookmark_a);
                        itemListImage.get(position).setSaved(1);
                        Save save = new Save(context);
                        save.execute(Integer.valueOf(IdUser), itemListImage.get(position).getID_Pic());
                    }

                }

            }
        });


///
        Picasso.with(context).load(itemListImage.get(position).getPic_url_user()).into(holder.user_prof);
        Picasso.with(context).load(itemListImage.get(position).getPic_Url()).into(holder.mainImage);

        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = formatter.format(itemListImage.get(position).getDate());

        DateParser dp = new DateParser(s);
        DateModel dm = dp.dateAndTimeParser();

        String temp = dm.toString();
        holder.date.setText(StringManager.convertEnglishNumbersToPersian(temp));
    }

    @Override
    public int getItemCount() {
        return itemListImage.size();
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
        TextView username, Image_title, likes_number, Description, comment_number, date;
        ImageView user_prof, Menu, mainImage;
        ImageButton like, comment, full_like, Save, UnSave;
        private RelativeLayout mainRel;
        UserSessionManager session = new UserSessionManager(context);

        int IDPic;

        private ViewHolder(final View parent) {
            super(parent);

            cv = (CardView) parent.findViewById(R.id.mycardView);
            username = (TextView) parent.findViewById(R.id.UserName_text);
            Description = (TextView) parent.findViewById(R.id.GalleryDescription);
            Image_title = (TextView) parent.findViewById(R.id.belowTitle);
            user_prof = (ImageView) parent.findViewById(R.id.UserImage);
            Menu = (ImageView) parent.findViewById(R.id.User_menu_icon);
            mainImage = (ImageView) parent.findViewById(R.id.mainImage);
            like = (ImageButton) parent.findViewById(R.id.disLike);
            likes_number = (TextView) parent.findViewById(R.id.likes_number);
//            full_like = (ImageButton) parent.findViewById(R.id.full_Like);
            comment = (ImageButton) parent.findViewById(R.id.comment);
            comment_number = (TextView) parent.findViewById(R.id.comment_number);
            mainRel = (RelativeLayout) parent.findViewById(R.id.GalleryRelative);
            Save = (ImageButton) parent.findViewById(R.id.Bookmark);
//            UnSave = (ImageButton) parent.findViewById(R.id.No_Bookmark);
            date = (TextView) parent.findViewById(R.id.dateGallery);

            mainRel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!session.isUserLoggedIn()) {

                        Toast.makeText(context, R.string.plzLogIn, Toast.LENGTH_SHORT).show();
                    }

                }
            });

            final Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale);

            mainImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mainImage.startAnimation(animation);
                    return false;
                }
            });

            user_prof.setOnClickListener(new View.OnClickListener() {
                @Override


                public void onClick(View view) {

                    if (!session.isUserLoggedIn()) {

                        Toast.makeText(context, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();


                    } else {


                        HashMap<String, String> user = session.getUserDetails();
                        String IdUser = user.get(UserSessionManager.KEY_ID_USER);

                        if (IdUser.equals(String.valueOf(itemListImage.get(getPosition()).getID_User()))) {

                            Intent i = new Intent(context, SharedActivity.class);
                            i.putExtra("Offer", "0");
                            context.startActivity(i);

                        } else {


                            Intent i = new Intent(context, UsersActivity.class);
                            i.putExtra("IdUSER", itemListImage.get(getPosition()).getID_User());
                            i.putExtra("UserName", itemListImage.get(getPosition()).getName());
                            i.putExtra("PIC_URL", itemListImage.get(getPosition()).getPic_url_user());
                            i.putExtra("isFollowing", itemListImage.get(getPosition()).getIs_Following());
                            context.startActivity(i);
                        }
                    }
                }
            });


            username.setOnClickListener(new View.OnClickListener() {
                @Override


                public void onClick(View view) {

                    if (!session.isUserLoggedIn()) {

                        Toast.makeText(context, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();


                    } else {


                        HashMap<String, String> user = session.getUserDetails();
                        String IdUser = user.get(UserSessionManager.KEY_ID_USER);

                        if (IdUser.equals(String.valueOf(itemListImage.get(getPosition()).getID_User()))) {

                            Intent i = new Intent(context, SharedActivity.class);
                            i.putExtra("Offer", "0");
                            context.startActivity(i);

                        } else {


                            Intent i = new Intent(context, UsersActivity.class);
                            i.putExtra("IdUSER", itemListImage.get(getPosition()).getID_User());
                            i.putExtra("UserName", itemListImage.get(getPosition()).getName());
                            i.putExtra("PIC_URL", itemListImage.get(getPosition()).getPic_url_user());
                            i.putExtra("isFollowing", itemListImage.get(getPosition()).getIs_Following());
                            context.startActivity(i);
                        }
                    }
                }
            });

//
//            like.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if (!session.isUserLoggedIn()) {
//
//                        Toast.makeText(context, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();
//
//
//                    } else {
////                      HashMap<String, String> user = session.getUserDetails();
//                        String IdUser = user.get(UserSessionManager.KEY_ID_USER);
//
//                        if (itemListImage.get(position).getLiked() == 1) {
//////                    Toast.makeText(activity,"Selected Row : "+position,Toast.LENGTH_SHORT).show();
////                            itemListImage.get(position).setLiked(0);
////                            holder.like.setImageResource(R.drawable.ic_like);
////                            UnLike unLike = new UnLike(context);
////                            unLike.execute(Integer.valueOf(IdUser), itemListImage.get(position).getID_Pic());
//
//                            String a =likes_number.getText().toString();
//                            int A = Integer.parseInt(a);
//                            int ab = A - 1;
//
//                            String abc = String.valueOf(ab);
//
//                            likes_number.setText(abc);
//                        } else {
////                            like.setImageResource(R.drawable.ic_likea);
////                            itemListImage.get(getPosition()).setLiked(1);
////                            Like like = new Like(context);
////                            like.execute(Integer.valueOf(IdUser), itemListImage.get(getPosition()).getID_Pic());
//
//                            String a = likes_number.getText().toString();
//                            int A = Integer.parseInt(a);
//                            int ab = A + 1;
//                            String abc = String.valueOf(ab);
//                            likes_number.setText(abc);
//
//                        }
//
//
//                    }
//                }
//            });
//
//
//            full_like.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if (!session.isUserLoggedIn()) {
//
//                        Toast.makeText(context, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();
//
//
//                    } else {
//
//                        like.setVisibility(View.VISIBLE);
//                        full_like.setVisibility(View.INVISIBLE);
//
//                        HashMap<String, String> user = session.getUserDetails();
//                        String IdUser = user.get(UserSessionManager.KEY_ID_USER);
//
//
//                        UnLike unLike = new UnLike(context);
//                        unLike.execute(Integer.valueOf(IdUser), itemListImage.get(getPosition()).getID_Pic());
//
//                        String a = likes_number.getText().toString();
//                        int A = Integer.parseInt(a);
//                        int ab = A - 1;
//
//                        String abc = String.valueOf(ab);
//
//                        likes_number.setText(abc);
//
//                    }
//
//                }
//            });


            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!session.isUserLoggedIn()) {

                        Toast.makeText(context, R.string.plzLogIn, Toast.LENGTH_LONG).show();


                    } else {


                        Intent intent = new Intent(parent.getContext(), CommentGalleryFixActivity.class);

                        int pictureID = itemListImage.get(getPosition()).getID_Pic();

                        int nComments = itemListImage.get(getPosition()).getnComments();
                        String pictureUrl = itemListImage.get(getPosition()).getPic_Url();
                        String foodName = itemListImage.get(getPosition()).getFoodName();
                        String userImage = itemListImage.get(getPosition()).getPic_url_user();
                        String userName = itemListImage.get(getPosition()).getName();

                        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String s = formatter.format(itemListImage.get(getPosition()).getDate());

                        DateParser dp = new DateParser(s);
                        DateModel dm = dp.dateAndTimeParser();

                        String temp = dm.toString();

                        intent.putExtra("Date", temp);
                        intent.putExtra("PicFoodId", pictureID);
                        intent.putExtra("PicFoodUrl", pictureUrl);
                        intent.putExtra("PicFoodName", foodName);
                        intent.putExtra("nComments", nComments);
                        intent.putExtra("nLikes", String.valueOf(itemListImage.get(getPosition()).getLikes()));
                        intent.putExtra("UserImage", userImage);
                        intent.putExtra("UserName", userName);
                        intent.putExtra("isLiked", String.valueOf(itemListImage.get(getPosition()).getLiked()));
                        context.startActivity(intent);

                    }
                }
            });

//            UnSave.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (!session.isUserLoggedIn()) {
//
//                        Toast.makeText(context, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();
//
//
//                    } else {
//
//
//                        HashMap<String, String> user = session.getUserDetails();
//                        String IdUser = user.get(UserSessionManager.KEY_ID_USER);
//                        Save save = new Save(context);
//                        save.execute(Integer.valueOf(IdUser), itemListImage.get(getPosition()).getID_Pic());
//                        Save.setVisibility(View.VISIBLE);
//                        UnSave.setVisibility(View.INVISIBLE);
//
//                    }
//                }
//            });
//
//
//
//            Save.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (!session.isUserLoggedIn()) {
//
//                        Toast.makeText(context, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();
//
//
//                    } else {
//
//
//
//                        HashMap<String, String> user = session.getUserDetails();
//                        String IdUser = user.get(UserSessionManager.KEY_ID_USER);
//
//
//                        UnSave unSave = new UnSave(context);
//                        unSave.execute(Integer.valueOf(IdUser), itemListImage.get(getPosition()).getID_Pic());
//
//                        UnSave.setVisibility(View.VISIBLE);
//                        Save.setVisibility(View.INVISIBLE);
//                    }
//                }
//            });
//

            Menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!session.isUserLoggedIn()) {

                        Toast.makeText(context, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();


                    } else {

//                                Toast.makeText(parent.getContext(), "Position:" + Integer.toString(getPosition()), Toast.LENGTH_SHORT).show();


                        final Dialog dialog = new Dialog(context, R.style.Dialog);
                        dialog.setContentView(R.layout.dialog_report);
                        dialog.show();
//                              dialog.setCancelable(false_email);

                        report_txt = (EditText) dialog.findViewById(R.id.dialog_report_ET);
                        report_btn = (Button) dialog.findViewById(R.id.dialog_report_BTN);


//
                        report_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (HttpManager.checkNetwork(context)) {

                                    String Report_reason = report_txt.getText().toString();

                                    Send_Report SR = new Send_Report(context);
                                    SR.execute(String.valueOf(itemListImage.get(getPosition()).getID_Pic()), Report_reason);

                                    dialog.dismiss();

                                } else {

                                    Toast.makeText(context, R.string.Disconnect, Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

                    }
                }
            });

        }
    }

    private class Like extends AsyncTask<Integer, String, String> {

        Context context;


        JSONObject object = new JSONObject();

        public Like(Context context) {
            object = new JSONObject();

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... integers) {

            String jsonText = HttpManager.gallerylike(integers[0], integers[1]);

            return jsonText;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


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

                String jsonText = HttpManager.recipeFavorite(integers[0], integers[1]);

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

                String jsonText = HttpManager.recipeUnFavorite(integers[0], integers[1]);

                return jsonText;

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

            }


        }

    }

    private class UnLike extends AsyncTask<Integer, String, String> {

        Context context;


        JSONObject object = new JSONObject();

        public UnLike(Context context) {
            object = new JSONObject();

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(Integer... integers) {

            String jsonText = HttpManager.galleryUnLike(integers[0], integers[1]);

            return jsonText;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


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

    private class Send_Report extends AsyncTask<String, String, String> {

        Context context;


        JSONObject object = new JSONObject();

        public Send_Report(Context context) {
            object = new JSONObject();

            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... strings) {

            String jsonText = HttpManager.reportGallery(Integer.parseInt(strings[0]), strings[1]);

            return jsonText;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            int res = Integer.parseInt(String.valueOf(result.charAt(0)));

            if (res == 1) {
                Toast.makeText(context, "با تشکر از همکاری شما ", Toast.LENGTH_SHORT).show();

            } else if (res == 0) {

                Toast.makeText(context, "خطا! لطفا دوباره امتحان کنید", Toast.LENGTH_SHORT).show();

            }

        }


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
