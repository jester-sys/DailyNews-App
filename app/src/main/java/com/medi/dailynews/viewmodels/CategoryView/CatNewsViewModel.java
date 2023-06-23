package com.medi.dailynews.viewmodels.CategoryView;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;


import com.medi.dailynews.models.Article;

public class CatNewsViewModel extends AndroidViewModel {

    private CatNewsRepository newsRepository;
    private LiveData<PagedList<Article>> newsArticles;

    public CatNewsViewModel(Context application) {
        super((Application) application);
        newsRepository = new CatNewsRepository(application);
    }


        public void fetchNewsArticles(String category) {
        newsArticles = newsRepository.getNewsArticles(category);
    }

    public LiveData<PagedList<Article>> getNewsArticles() {
        return newsArticles;
    }
}
