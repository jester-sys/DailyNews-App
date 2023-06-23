package com.medi.dailynews.db.Async;

import android.os.AsyncTask;

import com.medi.dailynews.db.ArticleDao;
import com.medi.dailynews.db.NewsRoomDatabase;
import com.medi.dailynews.models.Article;
import com.medi.dailynews.models.Source;

//public class  PopulateDbAsyncTask extends AsyncTask<Void, Void,Void> {
//
//    private final ArticleDao mDao;
//
//    public PopulateDbAsyncTask(NewsRoomDatabase db) {
//        mDao = db.articleDao();
//    }
//
//    @Override
//    protected Void doInBackground(final Void... params) {
//        Article newsItem = new Article();
//        newsItem.setAuthor("AuthorName");
//        newsItem.setTitle("NewsTitle");
//        newsItem.setUrl("imageUrl");
//        newsItem.setSource(new Source("BBC"));
//        newsItem.setDescription("Description");
//        newsItem.setPublishedAt("2 Jan 2020");
//        mDao.insert(newsItem);
//        return null;
//    }
//}