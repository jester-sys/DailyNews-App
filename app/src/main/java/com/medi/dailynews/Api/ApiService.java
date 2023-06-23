package com.medi.dailynews.Api;

import com.medi.dailynews.models.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface ApiService {
    @GET("top-headlines")
    Call<NewsResponse> getNewsArticles(
            @Query("country") String country,
            @Query("category") String category,
            @Query("q") String query,
            @Query("apiKey") String apiKey,
            @Query("page") int page,
            @Query("pageSize") int pageSize
    );
}
