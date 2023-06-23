package com.medi.weatherApp.Api;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtilities {
    private  static  final  String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private Context context;
     private static Retrofit  retrofit;


    public  static  WHApiService getApiService(){
        if(retrofit == null){
              retrofit  = new Retrofit.Builder()
              .baseUrl(BASE_URL)
              .addConverterFactory(GsonConverterFactory.create())
              .build();
        }

return  retrofit.create(WHApiService.class);

  }
}
