package com.codehelper.mapdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.codehelper.mapdemo.POJO.Photo;
import com.codehelper.mapdemo.POJO.Result;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private List<Result> results;
    List<Photo> photos=new ArrayList<>();
    RequestManager glide;
    Context context;

    public DataAdapter(Context context, List<Result> results) {
        this.context=context;
        this.results = results;
        glide = Glide.with(context);
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.place_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tv_name.setText(results.get(i).getName());
        viewHolder.tv_vicinity.setText(results.get(i).getVicinity());
       // float ratingValue=(float )results.get(i).getRating();
       // photos.add()
        photos=results.get(i).getPhotos();
        if(photos.size()!=0)
        {
            Log.e("photoreference:",photos.get(0).getPhotoReference()+"");
            String url="https://maps.googleapis.com/maps/api/place/photo?maxwidth=150&maxheight=150&photoreference="+photos.get(0).getPhotoReference()+"&key=AIzaSyBYVsQ0igUPEFoKpFMpsMDGzGMCvM5beFE";
Log.e("url",url+"");
            glide.load(url).placeholder(R.drawable.placeholder).into(viewHolder.pic);
        }
        else {
viewHolder.pic.setImageResource(R.drawable.no_image);

        }
       Log.e("photos",photos+"");
        viewHolder.ratingBar.setRating(results.get(i).getRating());
        viewHolder.tv_name.setText(results.get(i).getName());


    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pic;
        private TextView tv_name,tv_vicinity,tv_open;
        RatingBar ratingBar;
        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView)view.findViewById(R.id.Name);
            tv_vicinity = (TextView)view.findViewById(R.id.vicinity);
           // tv_open = (TextView)view.findViewById(R.id.open_txt);
            ratingBar=view.findViewById(R.id.ratings);
            pic=view.findViewById(R.id.thumbnail);

        }
    }

}
