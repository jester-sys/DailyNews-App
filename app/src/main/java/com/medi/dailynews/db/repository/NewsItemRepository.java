package com.medi.dailynews.db.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.medi.dailynews.db.ArticleDao;
import com.medi.dailynews.db.Async.DeleteAllNotesAsyncTask;
import com.medi.dailynews.db.Async.DeleteNoteAsyncTask;
import com.medi.dailynews.db.Async.InsertArticleAsyncTask;
import com.medi.dailynews.db.NewsRoomDatabase;
import com.medi.dailynews.models.Article;

import java.util.List;

public class NewsItemRepository {

    private ArticleDao mArticleDao;
    private LiveData<List<Article>> mAllSavedArticles;

    private static NewsItemRepository instance;
    private NewsApiClient mNewsApiClient;

    public static NewsItemRepository getInstance() {
        if (instance == null) {
            instance = new NewsItemRepository();
        }
        return instance;
    }

    public NewsItemRepository() {

        mNewsApiClient = NewsApiClient.getInstance();
    }

    public NewsItemRepository(Application application) {
        NewsRoomDatabase db = NewsRoomDatabase.getDatabase(application);
        mArticleDao = db.articleDao();
        mAllSavedArticles = mArticleDao.getAllSavedArticles();
    }

    public LiveData<List<Article>> getAllSavedArticles() {
        return mAllSavedArticles;
    }

    public void insert(Article newsItem) {
        new InsertArticleAsyncTask(mArticleDao).execute(newsItem);
    }

    public void delete(Article newsItem) {
        new DeleteNoteAsyncTask(mArticleDao).execute(newsItem);
    }

    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(mArticleDao).execute();
    }
}
