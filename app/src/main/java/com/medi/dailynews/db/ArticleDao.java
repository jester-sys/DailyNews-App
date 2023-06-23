package com.medi.dailynews.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.medi.dailynews.models.Article;

import java.util.List;

@Dao
public interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Article article);

    @Query("SELECT * FROM saved_articles ORDER BY id DESC")
    LiveData<List<Article>> getAllSavedArticles();

    @Delete
    void deleteArticle(Article article);

    @Query("DELETE FROM saved_articles")
    void deleteAllSavedArticles();
}
