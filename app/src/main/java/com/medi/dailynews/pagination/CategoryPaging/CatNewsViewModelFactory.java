package com.medi.dailynews.pagination.CategoryPaging;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.medi.dailynews.viewmodels.CategoryView.CatNewsViewModel;

public class CatNewsViewModelFactory implements ViewModelProvider.Factory {

    private Context context;

    public  CatNewsViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CatNewsViewModel.class)) {
            return (T) new CatNewsViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
