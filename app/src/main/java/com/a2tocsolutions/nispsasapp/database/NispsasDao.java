package com.a2tocsolutions.nispsasapp.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface NispsasDao {

    @Query("SELECT * FROM articleentry")
    LiveData<List<ArticleEntry>> loadAllArticles();

    @Insert
    void insertArticle(ArticleEntry articleEntry);

    @Query("SELECT * FROM articleentry WHERE id = :id")
    LiveData<ArticleEntry> loadArticleById(int id);

    @Query("DELETE FROM articleentry")
    void deleteAll();
}
