package com.medi.dailynews.db.Async;

import android.os.AsyncTask;

import com.medi.dailynews.db.ArticleDao;
import com.medi.dailynews.models.Article;

public class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void>{
    private ArticleDao articleDao;

    public DeleteAllNotesAsyncTask(ArticleDao dao) {
        articleDao = dao;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            articleDao.deleteAllSavedArticles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
