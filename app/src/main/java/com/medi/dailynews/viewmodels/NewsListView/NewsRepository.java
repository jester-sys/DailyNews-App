package com.medi.dailynews.viewmodels.NewsListView;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;


import com.medi.dailynews.Api.ClinetMangerRetrofit.RetrofitClientManger;
import com.medi.dailynews.models.Article;

//public class NewsRepository {
//
//    private Context context;
//    private RetrofitClientManger retrofitClientManager;
//    private LiveData<PagedList<Article>> newsArticles;
//
//    public NewsRepository(Context context) {
//        this.context = context;
//        retrofitClientManager = new RetrofitClientManger(context);
//        newsArticles = fetchNewsArticles();
//    }
//
//    public LiveData<PagedList<Article>> getNewsArticles() {
//        return newsArticles;
//    }
//
//    private LiveData<PagedList<Article>> fetchNewsArticles() {
//        DataSource.Factory<Integer, Article> dataSourceFactory = retrofitClientManager.getNewsArticles(context);
//        PagedList.Config config = new PagedList.Config.Builder()
//                .setEnablePlaceholders(false)
//                .setPageSize(20) // Set your desired page size
//                .build();
//        return new LivePagedListBuilder<>(dataSourceFactory, config).build();
//    }
//}
public class NewsRepository {

    private Context context;
    private RetrofitClientManger retrofitClientManager;
    private LiveData<PagedList<Article>> newsArticles;

    public NewsRepository(Context context) {
        this.context = context;
        retrofitClientManager = new RetrofitClientManger(context);
        newsArticles = fetchNewsArticles();
    }

    public LiveData<PagedList<Article>> getNewsArticles() {
        return newsArticles;
    }

    public LiveData<PagedList<Article>> getNewsArticlesByKeyword(String keyword) {
        DataSource.Factory<Integer, Article> dataSourceFactory = retrofitClientManager.getNewsArticlesByKeyword(keyword, context);
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(20) // Set your desired page size
                .build();
        return new LivePagedListBuilder<>(dataSourceFactory, config).build();
    }

    private LiveData<PagedList<Article>> fetchNewsArticles() {
        DataSource.Factory<Integer, Article> dataSourceFactory = retrofitClientManager.getNewsArticles(context);
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(20) // Set your desired page size
                .build();
        return new LivePagedListBuilder<>(dataSourceFactory, config).build();
    }
}
