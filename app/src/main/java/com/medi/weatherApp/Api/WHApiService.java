package com.medi.weatherApp.Api;

import com.medi.weatherApp.WHModels.WeatherModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WHApiService {
    @GET("weather")
    Call<WeatherModel> getCurrentWeatherData(
            @Query("lat")
            String lat,
            @Query("lon")
            String lon,
            @Query("APPID")
            String appid
    );

    @GET("weather")
    Call<WeatherModel> getCityWeatherData(
            @Query("q") String q,
            @Query("APPID") String appid
    );

}
