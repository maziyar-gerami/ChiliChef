package com.ict.chilichef.Adapter;

/**
 * Created by Mahshid on 6/16/2017.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ict.chilichef.CommentGalleryActivity;
import com.ict.chilichef.Manage.HttpManager;
import com.ict.chilichef.Model.GalleryCommentsModel;
import com.ict.chilichef.R;
import com.ict.chilichef.Sessions.UserSessionManager;
import com.ict.chilichef.UsersManage.UsersActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.LAYOUT_DIRECTION_RTL;

public class Recycler_GalleryComment_Adapter extends RecyclerView.Adapter<Recycler_GalleryComment_Adapter.ViewHolder> {

    protected Context context;
    UserSessionManager session;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    List<GalleryCommentsModel> itemListComment = Collections.emptyList();
    private ClickListener clicklistener = null;
    private ImageView deleteCM, ReportCM;
    private EditText report_txt;
    private Button report_btn;


    public Recycler_GalleryComment_Adapter(List<GalleryCommentsModel> itemListComment, Context context, RecyclerView recyclerView) {
        this.itemListComment = itemListComment;
        this.context = context;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return itemListComment.get(position) != null ? 1 : 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        ViewHolder viewHolder = null;
        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment_layout, parent, false);
            viewHolder = new ViewHolder(v);
//         else {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
//            viewHolder = new ProgressViewHolder(v);
//        }
        }
        return viewHolder;


    }

    public void setClickListener(ClickListener clickListener) {
        this.clicklistener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView

        session = new UserSessionManager(context);


        if (holder instanceof ViewHolder) {
            holder.comment.setText(itemListComment.get(position).getComment());
            Picasso.with(context).load(itemListComment.get(position).getPic_Url_User()).into((holder.profileimage));

            if (session.isUserLoggedIn()) {

                HashMap<String, String> user = session.getUserDetails();
                String IdUser = user.get(UserSessionManager.KEY_ID_USER);
//
                if (IdUser.equals(String.valueOf(itemListComment.get(position).getID_Writer()))) {

                    deleteCM.setVisibility(View.VISIBLE);
                    holder.cv.setCardBackgroundColor(Color.parseColor("#FCC534"));
                } else {

                    ReportCM.setVisibility(View.VISIBLE);
                    holder.cv.setCardBackgroundColor(Color.parseColor("#efeeee"));


                }
            }

        } else {

            holder.progressBar.setIndeterminate(true);
        }
    }

    public void setLoad() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return itemListComment.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface ClickListener {
        public void itemOnClicked(View view, int position);
    }

    public void swap(List<GalleryCommentsModel> ListComment) {
        itemListComment.clear();
        itemListComment.addAll(ListComment);
        notifyDataSetChanged();
    }

    public void remove(GalleryCommentsModel data) {
        int position = itemListComment.indexOf(data);
        itemListComment.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name, comment, Date_comment;
        CircleImageView profileimage;
        private LinearLayout main;
        public ProgressBar progressBar;


        ViewHolder(final View parent) {
            super(parent);
            cv = (CardView) parent.findViewById(R.id.cardViewComment);
//            name = (TextView) parent.findViewById(R.id.TV_name_comment);
            profileimage = (CircleImageView) parent.findViewById(R.id.IV_profile_comment);
            comment = (TextView) parent.findViewById(R.id.TV_comment);
//            Date_comment = (TextView) parent.findViewById(R.id.TV_Date_comment);
            main = (LinearLayout) parent.findViewById(R.id.Relative_comment);
            deleteCM = (ImageView) parent.findViewById(R.id.TV_DeleteComment);
            ReportCM = (ImageView) parent.findViewById(R.id.TV_ReportComment);


            deleteCM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    UserSessionManager session = new UserSessionManager(context);

                    if (!session.isUserLoggedIn()) {

                        Toast.makeText(context, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();


                    } else {

                        HashMap<String, String> user = session.getUserDetails();
                        String IdUser = user.get(UserSessionManager.KEY_ID_USER);
//
                        if (IdUser.equals(String.valueOf(itemListComment.get(getPosition()).getID_Writer()))) {
                            PopupMenu popupMenu = new PopupMenu(parent.getContext(), main);
                            popupMenu.getMenuInflater().inflate(R.menu.menu_comment, popupMenu.getMenu());
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {

                                    if (HttpManager.checkNetwork(context)) {
                                        DeleteComment deleteComment = new DeleteComment(context);
                                        deleteComment.execute(itemListComment.get(getPosition()).getID_Comment());
                                        remove(itemListComment.get(getPosition()));
                                    } else {

                                        Toast.makeText(context, R.string.Disconnect, Toast.LENGTH_LONG).show();

                                    }

                                    return true;

                                }
                            });

                            popupMenu.show();


                        }

                    }


                }
            });


            ReportCM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    UserSessionManager session = new UserSessionManager(context);

                    if (!session.isUserLoggedIn()) {

                        Toast.makeText(context, "برای دسترسی به این بخش، لطفا به برنامه وارد شوید", Toast.LENGTH_LONG).show();


                    } else {

                        HashMap<String, String> user = session.getUserDetails();
                        String IdUser = user.get(UserSessionManager.KEY_ID_USER);
//
                        if (IdUser.equals(String.valueOf(itemListComment.get(getPosition()).getID_Writer()))) {

                        } else {

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
                                        if ( Report_reason.length()>5) {
                                            Send_Report SR = new Send_Report(context);
                                            SR.execute(String.valueOf(itemListComment.get(getPosition()).getID_Comment()), Report_reason);

                                            dialog.dismiss();
                                        }else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            // Creates textview for centre title
                                            TextView myMsg = new TextView(context);
                                            myMsg.setText("خطا!");
                                            myMsg.setGravity(Gravity.RIGHT);
                                            myMsg.setPadding(10,20,50,0);

                                            myMsg.setTextSize(20);
                                            myMsg.setTextColor(ContextCompat.getColor(context, (R.color.colorAccent)));
                                            //set custom title
                                            builder.setCustomTitle(myMsg);
                                            builder.setMessage("لطفا علت گزارش را کامل بیان کنید");
                                            builder.setPositiveButton("باشه", null);
                                            AlertDialog dialog = builder.show();
                                            //Create custom message
//                    TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
//                    messageText.setGravity(Gravity.RIGHT);
                                        }

                                    } else {
                                        Toast.makeText(context, R.string.Disconnect, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                        }

                    }

                }
            });


            profileimage.setOnClickListener(new View.OnClickListener() {@Override
                                            public void onClick(View view) {

                                                UserSessionManager session = new UserSessionManager(context);

                                                HashMap<String, String> user = session.getUserDetails();
                                                String IdUser = user.get(UserSessionManager.KEY_ID_USER);

                                                if (IdUser.equals(String.valueOf(itemListComment.get(getPosition()).getID_Writer()))) {


                                                } else {


                                                    Intent j = new Intent(context, UsersActivity.class);
                                                    j.putExtra("IdUSER", itemListComment.get(getPosition()).getID_Writer());
                                                    j.putExtra("PIC_URL", itemListComment.get(getPosition()).getPic_Url_User());
                                                    j.putExtra("UserName", itemListComment.get(getPosition()).getName());
                                                    j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    context.startActivity(j);
                                                }
                                            }
                                            }

            );
        }
    }

    public class ProgressViewHolder extends ViewHolder {
        public ProgressViewHolder(View parent) {
            super(parent);
//            progressBar = (ProgressBar) parent.findViewById(R.id.progressBar);
        }
    }


    private class DeleteComment extends AsyncTask<Integer, String, String> {

        Context context;


        JSONObject object = new JSONObject();

        public DeleteComment(Context context) {
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

            String jsonText = null;
            try {
                jsonText = HttpManager.deleteCommentGallery(integers[0]);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

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

            String jsonText = HttpManager.reportCommentsGallery(Integer.parseInt(strings[0]), strings[1]);

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


}

