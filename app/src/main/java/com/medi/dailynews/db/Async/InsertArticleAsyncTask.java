package com.medi.dailynews.db.Async;

import android.os.AsyncTask;

import com.medi.dailynews.db.ArticleDao;
import com.medi.dailynews.models.Article;


public class InsertArticleAsyncTask extends AsyncTask<Article, Void, Void> {
    private ArticleDao articleDao;

    public InsertArticleAsyncTask(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    @Override
    protected Void doInBackground(Article... articles) {
                articleDao.insert(articles[0]);


        return null;
    }
}
