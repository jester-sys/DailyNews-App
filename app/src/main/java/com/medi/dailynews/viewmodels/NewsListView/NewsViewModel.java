package com.medi.dailynews.viewmodels.NewsListView;

import android.content.Context;
import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.medi.dailynews.models.Article;
import com.medi.dailynews.utils.DataStatus;

//public class NewsViewModel extends ViewModel {
//
//    private NewsRepository newsRepository;
//    private LiveData<PagedList<Article>> newsArticles;
//
//    public NewsViewModel(Context context) {
//        newsRepository = new NewsRepository(context);
//        newsArticles = newsRepository.getNewsArticles();
//    }
//
//    public LiveData<PagedList<Article>> getNewsArticles() {
//        return newsArticles;
//    }
//
//}

//public class NewsViewModel extends ViewModel {
//
//    private NewsRepository newsRepository;
//    private LiveData<PagedList<Article>> newsArticles;
//private  DataStatus dataStatus;
//    public NewsViewModel(Context context) {
//        newsRepository = new NewsRepository(context);
//        newsArticles = newsRepository.getNewsArticles();
//    }
//
//    public LiveData<PagedList<Article>> getNewsArticles() {
//        return newsArticles;
//    }
//
//    public LiveData<PagedList<Article>> getNewsArticlesByKeyword(String keyword) {
//        return newsRepository.getNewsArticlesByKeyword(keyword);
//    }
//    public LiveData<DataStatus> getDataStatus() {
//        // Assuming you have a MutableLiveData<DataStatus> object named dataStatus in your newsViewModel
//
//        // Check if the dataStatus object is already initialized
//        if (dataStatus == null) {
//            // Initialize the dataStatus object
//            dataStatus = new MutableLiveData<>();
//            // Perform any necessary initialization or data loading here
//
//            // Example: Simulating data loading with a delay of 2 seconds
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    // Assuming you have a method to update the dataStatus value based on the loading result
//                    dataStatus.setValue(DataStatus.LOADED);
//                }
//            }, 2000);
//        }
//
//        // Return the dataStatus LiveData object
//        return dataStatus;
//    }
//
//}
public class NewsViewModel extends ViewModel {

    private NewsRepository newsRepository;
    public LiveData<PagedList<Article>> newsArticles;
    private MutableLiveData<DataStatus> dataStatus;

    public NewsViewModel(Context context) {
        newsRepository = new NewsRepository(context);
        newsArticles = newsRepository.getNewsArticles();
        dataStatus = new MutableLiveData<>();
    }
    public NewsViewModel() {

    }
    public LiveData<PagedList<Article>> getNewsArticles() {
        return newsArticles;
    }

    public LiveData<PagedList<Article>> getNewsArticlesByKeyword(String keyword) {
        return newsRepository.getNewsArticlesByKeyword(keyword);
    }

    public LiveData<DataStatus> getDataStatus() {
        return dataStatus;
    }

    public void loadData() {
        // Set dataStatus to LOADING before starting the data loading process
        dataStatus.setValue(DataStatus.LOADING);

        // Perform your data loading logic here
        // Example: Simulating data loading with a delay of 2 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Assuming you have a method to update the dataStatus value based on the loading result
                dataStatus.setValue(DataStatus.LOADED);
            }
        }, 2000);
    }

}
