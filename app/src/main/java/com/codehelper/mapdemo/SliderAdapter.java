package com.codehelper.mapdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roshan on 9/14/2017.
 */

public class SliderAdapter extends PagerAdapter {
   private List<String> images;
    Context context;
    private LayoutInflater layoutInflater;
    //int image[]={R.drawable.me,R.drawable.wall,R.drawable.dummy_profile};

    SliderAdapter(Context context, List<String> images)
    {
        this.context=context;
        this.images=images;
        layoutInflater=LayoutInflater.from(context);


    }

    @NonNull
    public Object instantiateItem(ViewGroup view, int position)
    {
        Log.e("pos",images.get(position)+"");
        View layout=layoutInflater.inflate(R.layout.demo_image,view,false);
        ImageView imageView=(ImageView)layout.findViewById(R.id.iv_gallery);
       Glide.with(context)
                .load(images.get(position))
                .crossFade()
               .placeholder(R.drawable.placeholder)
                .into(imageView);

 //imageView.setImageResource(images.get(position));
        view.addView(layout);
        return layout;

    }
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }



    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
