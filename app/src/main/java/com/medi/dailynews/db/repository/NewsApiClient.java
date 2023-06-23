package com.medi.dailynews.db.repository;

public class NewsApiClient {

    private static NewsApiClient instance;

    private NewsApiClient() {
    }

    public static NewsApiClient getInstance() {
        if (instance == null) {
            instance = new NewsApiClient();
        }
        return instance;
    }
}