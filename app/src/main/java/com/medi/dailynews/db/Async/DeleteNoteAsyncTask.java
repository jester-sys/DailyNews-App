package com.medi.dailynews.db.Async;

import android.os.AsyncTask;

import com.medi.dailynews.db.ArticleDao;
import com.medi.dailynews.models.Article;

import javax.xml.transform.Result;

public class DeleteNoteAsyncTask extends AsyncTask<Article, Void, Void> {
    private ArticleDao articleDao;

    public DeleteNoteAsyncTask(ArticleDao noteDao) {
        articleDao = noteDao;
    }
    @Override
    protected Void doInBackground(Article... articles) {
        articleDao.deleteArticle(articles[0]);
        return null;
    }
}
