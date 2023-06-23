package com.medi.dailynews.Api.ClinetMangerRetrofit;

import android.content.Context;

import androidx.paging.DataSource;

import com.medi.dailynews.Api.ApiService;
import com.medi.dailynews.pagination.CategoryPaging.CatNewsDataSourceFactory;
import com.medi.dailynews.models.Article;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Catretrofit {

    private static final String BASE_URL = "https://newsapi.org/V2/";

    private Retrofit retrofit;
    private Context context;

 public  Catretrofit(Context context){
        this.context = context;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public DataSource.Factory<Integer, Article> getNewsArticles(String category) {
        ApiService apiService = retrofit.create(ApiService.class);
        return new CatNewsDataSourceFactory(apiService, context, category);
    }


}
