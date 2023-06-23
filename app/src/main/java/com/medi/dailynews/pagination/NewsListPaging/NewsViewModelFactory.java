package com.medi.dailynews.pagination.NewsListPaging;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.medi.dailynews.viewmodels.NewsListView.NewsViewModel;

public class NewsViewModelFactory implements ViewModelProvider.Factory {

    private Context context;

    public NewsViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NewsViewModel.class)) {
            return (T) new NewsViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
