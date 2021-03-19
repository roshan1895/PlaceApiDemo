package com.codehelper.mapdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codehelper.mapdemo.DetailsPOJO.DetailsResponse;
import com.codehelper.mapdemo.DetailsPOJO.Result;
import com.codehelper.mapdemo.POJO.Example;
import com.github.vivchar.viewpagerindicator.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceDetails extends Fragment {
    String Placeid;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8;
    RatingBar ratingBar;
    ViewPager pager;
    SliderAdapter adapter;
    ViewPagerIndicator indicator;
    List<String> UrlList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Placeid = getArguments().getString("placeid");
        return inflater.inflate(R.layout.place_details,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("place_id",Placeid+"");
        initViews();


    }
    private void build_retrofit_and_get_response(String place) {

        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);
            final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading Place Details");
            pDialog.setCancelable(false);
            pDialog.show();
            UrlList=new ArrayList<>();



            Call<DetailsResponse> call = service.getPlaceDetails(place);
            call.enqueue(new Callback<DetailsResponse>() {
                @Override
                public void onResponse(Call<DetailsResponse> call, Response<DetailsResponse> response) {
                    try {
                        if(response.body().getStatus().equalsIgnoreCase("OK"))
                        {   pDialog.dismiss();
                            Log.e("results", response.body().getResult() + "");
                            Result result = response.body().getResult();
                            tv1.setText(result.getName());
                            ratingBar.setRating(result.getRating());
                            tv2.setText(result.getRating().toString());
                            if(result.getOpeningHours().getOpenNow()==true) {
                                tv3.setText("Open Now");
                            }
                            else
                            {
                                tv3.setText("Closed");
                                tv3.setTextColor(getResources().getColor(R.color.error_color));
                            }
                            tv4.setText(result.getFormattedAddress());
                            tv6.setText(result.getInternationalPhoneNumber());
                            tv7.setText(result.getWebsite());
                            String url;
                            for(int i=0;i<result.getPhotos().size();i++)
                            {
                                url="https://maps.googleapis.com/maps/api/place/photo?maxwidth=500&maxheight=500&photoreference="+result.getPhotos().get(i).getPhotoReference()+"&key=AIzaSyBYVsQ0igUPEFoKpFMpsMDGzGMCvM5beFE";
                                UrlList.add(url);
                            }
                            Log.e("url_list",UrlList+"");
                            pager.setAdapter(new SliderAdapter(getActivity(),UrlList));
                            indicator.setupWithViewPager(pager);
                            indicator.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int i, float v, int i1) {

                                }

                                @Override
                                public void onPageSelected(int i) {

                                }

                                @Override
                                public void onPageScrollStateChanged(int i) {

                                }
                            });


                        }
                        else
                        {
                            Toast.makeText(getActivity(), "look like url is missing something ", Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        }
                    }
                    catch (Exception e) {
                        Log.d("onResponse", "There is an error");
                        e.printStackTrace();
                        pDialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<DetailsResponse> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                    pDialog.dismiss();

                }
            });





    }
    public void initViews()
    {   pager=getView().findViewById(R.id.viewpager);
        indicator=getView().findViewById(R.id.view_pager_indicator);
        tv1=getView().findViewById(R.id.place_name);
        tv2=getView().findViewById(R.id.rating_text);
        tv3=getView().findViewById(R.id.open_text);
        tv4=getView().findViewById(R.id.place_address);
        tv5=getView().findViewById(R.id.place_time);
        tv6=getView().findViewById(R.id.place_contact);
        tv7=getView().findViewById(R.id.place_website);
        ratingBar=getView().findViewById(R.id.rating_bar);
        build_retrofit_and_get_response(Placeid);

    }

}
