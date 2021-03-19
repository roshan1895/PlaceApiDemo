package com.codehelper.mapdemo;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codehelper.mapdemo.POJO.Example;
import com.codehelper.mapdemo.POJO.Result;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFrag extends Fragment {
    GPSTracker gps;
    List<Result> results;
    RecyclerView recyclerView;
    DataAdapter adapter;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Fragment fragment=null;
    Bundle args=new Bundle();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_frag,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gd=new GestureDetector(getActivity(),new GestureDetector.SimpleOnGestureListener(){
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null && gd.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
                    fragment=new PlaceDetails();
                    Bundle args = new Bundle();
                    passInfo(position);

                    if (fragment != null) {
                        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);


                        ft.commit();
                    }


                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }
    private void initViews(){
        recyclerView = (RecyclerView)getView().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        build_retrofit_and_get_response("restaurant");

    }
    private void build_retrofit_and_get_response(String type) {

        String url = "https://maps.googleapis.com/maps/";
        double latitude;
        double longitude;

        gps = new GPSTracker(getActivity());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        results = new ArrayList<>();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);
        if (gps.canGetLocation()) {
            final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Fetching Location");
            pDialog.setCancelable(false);
            pDialog.show();

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();


            Call<Example> call = service.getNearbyPlaces(type, latitude + "," + longitude, "distance");

            call.enqueue(new Callback<Example>() {
                @Override
                public void onResponse(Call<Example> call, Response<Example> response) {
                    try {
                        // This loop will go through all the results and add marker on each location.
                        if(response.body().getStatus().equalsIgnoreCase("OK")) {

                            Log.e("results", response.body().getResults() + "");
                            results = response.body().getResults();
                            adapter = new DataAdapter(getActivity(), results);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            pDialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "look like url is missing something ", Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                        }




                    } catch (Exception e) {
                        Log.d("onResponse", "There is an error");
                        e.printStackTrace();
                        pDialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<Example> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                    pDialog.dismiss();


                }

            });

        }


    }

    public  void passInfo(int i)
    {

        args.putString("placeid",results.get(i).getPlaceId());

        fragment.setArguments(args);

    }
}
