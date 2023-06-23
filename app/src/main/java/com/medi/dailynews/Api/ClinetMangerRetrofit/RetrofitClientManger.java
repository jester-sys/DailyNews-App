package com.medi.dailynews.Api.ClinetMangerRetrofit;

import android.content.Context;


import androidx.paging.DataSource;

import com.medi.dailynews.Api.ApiService;
import com.medi.dailynews.pagination.NewsListPaging.NewsDataSourceFactory;
import com.medi.dailynews.models.Article;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClientManger {

    private static final String BASE_URL = "https://newsapi.org/V2/";

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RetrofitClientManger(Context context) {
    }

    public DataSource.Factory<Integer, Article> getNewsArticles(Context context) {
        ApiService apiService = retrofit.create(ApiService.class);
        return new NewsDataSourceFactory(apiService, context);
    }

    public DataSource.Factory<Integer, Article> getNewsArticlesByKeyword(String keyword, Context context) {
        ApiService apiService = retrofit.create(ApiService.class);
        return new NewsDataSourceFactory(apiService, context, keyword);
    }
}
