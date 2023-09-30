package com.example.API;

import com.example.model.WeatherModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("locations/v1/cities/search")
    Call<ResponseBody> getCity(
            @Query("q") String locationKey,
            @Query("apikey") String apiKey
    );
    @GET("currentconditions/v1/{locationKey}")
    Call<ResponseBody> getCurrentConditions(
            @Path("locationKey") String locationKey,
            @Query("apikey") String apiKey,
            @Query("language") String language,
            @Query("details") boolean details
    );
    @GET("forecasts/v1/daily/5day/{locationKey}")
    Call<ResponseBody> getForecast5Days(
            @Path("locationKey") String locationKey,
            @Query("apikey") String apiKey
    );
    @GET("forecasts/v1/hourly/12hour/{locationKey}")
    Call<ResponseBody> getForecast12Hour(
            @Path("locationKey") String locationKey,
            @Query("apikey") String apiKey
    );
}
