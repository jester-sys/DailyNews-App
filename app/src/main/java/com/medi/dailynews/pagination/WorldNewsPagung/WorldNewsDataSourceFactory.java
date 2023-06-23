package com.medi.dailynews.pagination.WorldNewsPagung;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.medi.dailynews.Api.ApiService;
import com.medi.dailynews.models.Article;

public class WorldNewsDataSourceFactory extends DataSource.Factory<Integer, Article> {

    private ApiService apiService;
    private Context context;
    private String country;

    public WorldNewsDataSourceFactory(ApiService apiService, Context context, String country) {
        this.apiService = apiService;
        this.context = context;
        this.country = country;
    }

    @NonNull
    @Override
    public DataSource<Integer, Article> create() {
        return new WorldDataSource(apiService, context, country);
    }
}
