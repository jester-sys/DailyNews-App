package com.medi.dailynews.pagination.NewsListPaging;

import android.content.Context;

import androidx.paging.DataSource;

import com.medi.dailynews.Api.ApiService;
import com.medi.dailynews.models.Article;


public class NewsDataSourceFactory extends DataSource.Factory<Integer, Article> {

        private ApiService apiService;
        private Context context;
        private String keyword;

        public NewsDataSourceFactory(ApiService apiService, Context context) {
                this.apiService = apiService;
                this.context = context;
        }

        public NewsDataSourceFactory(ApiService apiService, Context context, String keyword) {
                this.apiService = apiService;
                this.context = context;
                this.keyword = keyword;
        }

        @Override
        public DataSource<Integer, Article> create() {
                if (keyword != null) {
                        return new NewsDataSource(apiService, context, keyword);
                } else {
                        return new NewsDataSource(apiService, context);
                }
        }
}
