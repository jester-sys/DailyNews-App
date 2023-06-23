package com.medi.dailynews.viewmodels.CategoryView;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;


import com.medi.dailynews.Api.ClinetMangerRetrofit.Catretrofit;

import com.medi.dailynews.models.Article;


public class CatNewsRepository {
    private Context context;
    private Catretrofit retrofitClientManager;
    private LiveData<PagedList<Article>> newsArticles;

    public CatNewsRepository(Context context) {
        this.context = context;
        retrofitClientManager = new Catretrofit(context);
        newsArticles = fetchNewsArticles(null); // Pass null initially
    }

    public LiveData<PagedList<Article>> getNewsArticles(String category) {
        newsArticles = fetchNewsArticles(category);
        return newsArticles;
    }


    private LiveData<PagedList<Article>> fetchNewsArticles(String category) {
        DataSource.Factory<Integer, Article> dataSourceFactory = retrofitClientManager.getNewsArticles(category);
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(20) // Set your desired page size
                .build();
        return new LivePagedListBuilder<>(dataSourceFactory, config).build();
    }

}
