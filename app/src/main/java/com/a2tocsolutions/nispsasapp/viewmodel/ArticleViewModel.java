package com.a2tocsolutions.nispsasapp.viewmodel;

import android.app.Application;
import android.util.Log;

import com.a2tocsolutions.nispsasapp.database.AppDatabase;
import com.a2tocsolutions.nispsasapp.database.ArticleEntry;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ArticleViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = ArticleViewModel.class.getSimpleName();

    private LiveData<List<ArticleEntry>> article;

    public ArticleViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the articles from the DataBase");
        article = database.nispsasDao().loadAllArticles();
    }

    public LiveData<List<ArticleEntry>> getArticle() {
        return article;
    }
}

