package com.medi.dailynews.viewmodels.WorldNewsView;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.medi.dailynews.Api.ApiService;
import com.medi.dailynews.models.Article;
import com.medi.dailynews.viewmodels.CategoryView.CatNewsRepository;

import java.util.List;



public class WorldNewsViewModel extends AndroidViewModel {

    private WorldNewsRepository newsRepository;
    private LiveData<PagedList<Article>> newsArticles;

    public WorldNewsViewModel(Context application) {
        super((Application) application);
        newsRepository = new WorldNewsRepository(application);
    }


    public void fetchNewsArticles(String country) {
        newsArticles = newsRepository.getNewsArticles(country);
    }

    public LiveData<PagedList<Article>> getNewsArticles() {
        return newsArticles;
    }
}