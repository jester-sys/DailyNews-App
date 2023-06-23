package com.medi.dailynews.pagination.CategoryPaging;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.medi.dailynews.Api.ApiService;
import com.medi.dailynews.R;
import com.medi.dailynews.models.Article;
import com.medi.dailynews.models.NewsResponse;
import com.medi.dailynews.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatDataSource extends PageKeyedDataSource<Integer, Article> {

    private static final int PAGE_SIZE = 20; // Number of items to be loaded per page
    private static final int INITIAL_PAGE = 1; // Initial page number

    private ApiService apiService;
    private Context context;
    private String category;

    public CatDataSource(ApiService apiService, Context context, String category) {
        this.apiService = apiService;
        this.context = context;
        this.category = category;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Article> callback) {
        int currentPage = INITIAL_PAGE;

        // Calculate the number of items to be loaded
        int pageSize = params.requestedLoadSize;

        // Make a network request to fetch the initial page of articles
        apiService.getNewsArticles("in", category, null, Utils.API_KEY, currentPage, pageSize)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (response.isSuccessful()) {
                            List<Article> articles = response.body().getArticles();
                            if (articles != null) {
                                // Pass the loaded data to the callback
                                callback.onResult(articles, null, currentPage + 1);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        // Handle the failure scenario
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Article> callback) {
        int currentPage = params.key;

        // Calculate the number of items to be loaded
        int pageSize = params.requestedLoadSize;

        // Make a network request to fetch the next page of articles
        apiService.getNewsArticles("in", category, null, Utils.API_KEY, currentPage, pageSize)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (response.isSuccessful()) {
                            List<Article> articles = response.body().getArticles();
                            if (articles != null) {
                                // Pass the loaded data to the callback
                                callback.onResult(articles, currentPage + 1);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        // Handle the failure scenario
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Article> callback) {

    }
}
