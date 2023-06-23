package com.medi.dailynews.Api.ClinetMangerRetrofit;

import android.content.Context;

import androidx.paging.DataSource;

import com.medi.dailynews.Api.ApiService;
import com.medi.dailynews.models.Article;

import com.medi.dailynews.pagination.WorldNewsPagung.WorldNewsDataSourceFactory;
import com.medi.dailynews.pagination.WorldNewsPagung.WorldNewsViewModelFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//public class WorldNewsRetrofitClintManger {
//}
public class WorldNewsRetrofitClintManger {

    private static final String BASE_URL = "https://newsapi.org/V2/";

    private Retrofit retrofit;
    private Context context;

    public  WorldNewsRetrofitClintManger(Context context){
        this.context = context;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public DataSource.Factory<Integer, Article> getNewsArticles(String country) {
        ApiService apiService = retrofit.create(ApiService.class);
        return new WorldNewsDataSourceFactory(apiService, context, country);
    }


}
