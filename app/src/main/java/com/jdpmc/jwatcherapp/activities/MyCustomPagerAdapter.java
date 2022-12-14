package com.jdpmc.jwatcherapp.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.jdpmc.jwatcherapp.R;

public class MyCustomPagerAdapter extends PagerAdapter {
    Context context;
    int image1[];
    int image2[];
    int image3[];
    LayoutInflater layoutInflater;



    public MyCustomPagerAdapter(MainActivity mainActivity, int[] image1) {
        this.context = context;
        this.image1 = image1;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return image1.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.activity_adminis, container, false);




        container.addView(itemView);



        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
