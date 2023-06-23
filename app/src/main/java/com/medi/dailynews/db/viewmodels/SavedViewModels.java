package com.medi.dailynews.db.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.medi.dailynews.db.ArticleDao;
import com.medi.dailynews.db.repository.NewsItemRepository;
import com.medi.dailynews.models.Article;

import java.io.Closeable;
import java.util.List;

public class SavedViewModels extends AndroidViewModel {

    private NewsItemRepository mRepository;
    private LiveData<List<Article>> mAllSavedArticles;

    public SavedViewModels(@NonNull Application application) {
        super(application);
        mRepository = new NewsItemRepository(application);
        mAllSavedArticles = mRepository.getAllSavedArticles();
    }

    public LiveData<List<Article>> getAllSavedArticles() {
        return mAllSavedArticles;
    }

    public void insertArticle(Article newsItem) {
        mRepository.insert(newsItem);
    }

    public void deleteArticle(Article newsItem) {
        mRepository.delete(newsItem);
    }

    public void deleteAllSavedArticles() {
        mRepository.deleteAllNotes();
    }
}
