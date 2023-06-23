package com.medi.dailynews.pagination.WorldNewsPagung;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.medi.dailynews.viewmodels.WorldNewsView.WorldNewsViewModel;

public class WorldNewsViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    public WorldNewsViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WorldNewsViewModel.class)) {
            return (T) new WorldNewsViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}

