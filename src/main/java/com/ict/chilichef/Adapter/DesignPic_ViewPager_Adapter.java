package com.ict.chilichef.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ict.chilichef.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.ict.chilichef.MainActivity.URL_DesignPics;

/**
 * Created by Elham on 8/25/2017.
 */

// this is an adapter help us to have more than one pic in viewPager
//we use it in NotesActivity and DesignDetailsActivity

public class DesignPic_ViewPager_Adapter extends PagerAdapter {

    Activity activity;
    List<String> images;
    LayoutInflater inflater;


    public DesignPic_ViewPager_Adapter(Activity activity, List<String> images) {
        this.activity = activity;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.row_design_pic, container, false);

        ImageView image;
        image = (ImageView) itemView.findViewById(R.id.design_pic);

        DisplayMetrics dis = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);
        int height = dis.heightPixels;
        int width = dis.widthPixels;
        image.setMinimumHeight(height);
        image.setMinimumWidth(width);
        try {

            Picasso.with(activity.getApplication()).load(URL_DesignPics + images.get(position)).placeholder(R.drawable.icon).into(image);

        } catch (Exception ex) {

        }

        container.addView(itemView);
        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
