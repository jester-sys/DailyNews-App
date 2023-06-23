package com.medi.dailynews.viewmodels.WorldNewsView;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.medi.dailynews.Api.ClinetMangerRetrofit.Catretrofit;
import com.medi.dailynews.Api.ClinetMangerRetrofit.WorldNewsRetrofitClintManger;
import com.medi.dailynews.models.Article;

public class WorldNewsRepository {
    private Context context;
    private WorldNewsRetrofitClintManger retrofitClientManager;
    private LiveData<PagedList<Article>> newsArticles;

    public WorldNewsRepository(Context context) {
        this.context = context;
        retrofitClientManager = new WorldNewsRetrofitClintManger(context);
        newsArticles = fetchNewsArticles(null); // Pass null initially
    }
    public LiveData<PagedList<Article>> getNewsArticles(String country) {
        newsArticles = fetchNewsArticles(country);
        return newsArticles;
    }




    private LiveData<PagedList<Article>> fetchNewsArticles(String Country) {
        DataSource.Factory<Integer, Article> dataSourceFactory = retrofitClientManager.getNewsArticles(Country);
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(20) // Set your desired page size
                .build();
        return new LivePagedListBuilder<>(dataSourceFactory, config).build();
    }

}
