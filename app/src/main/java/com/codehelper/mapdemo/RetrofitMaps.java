package com.codehelper.mapdemo;

import com.codehelper.mapdemo.DetailsPOJO.DetailsResponse;
import com.codehelper.mapdemo.POJO.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitMaps {
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyCgV8Mygdhr_FuIv9wR0vAQYKACw_UZ768")
    Call<Example> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("rankby") String radius);
    @GET("api/place/details/json?key=AIzaSyCgV8Mygdhr_FuIv9wR0vAQYKACw_UZ768")
    Call<DetailsResponse> getPlaceDetails(@Query("placeid") String place);


}
