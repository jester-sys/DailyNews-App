package com.medi.dailynews.pagination.CategoryPaging;

import android.content.Context;


import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.medi.dailynews.Api.ApiService;

import com.medi.dailynews.models.Article;

public class CatNewsDataSourceFactory extends DataSource.Factory<Integer, Article> {

    private ApiService apiService;
    private Context context;
    private String category;
    public CatNewsDataSourceFactory(ApiService apiService, Context context, String category ) {
        this.apiService = apiService;
        this.context = context;
        this.category = category;

    }

    @NonNull
    @Override
    public DataSource<Integer, Article> create() {
        return new CatDataSource(apiService, context, category);
    }
}
