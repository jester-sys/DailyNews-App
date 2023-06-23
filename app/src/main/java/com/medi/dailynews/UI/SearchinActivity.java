package com.medi.dailynews.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.medi.dailynews.MainActivity;
import com.medi.dailynews.adapter.SearchListAdapter;
import com.medi.dailynews.R;
import com.medi.dailynews.databinding.ActivitySearchinBinding;
import com.medi.dailynews.models.Article;
import com.medi.dailynews.pagination.NewsListPaging.NewsViewModelFactory;
import com.medi.dailynews.viewmodels.NewsListView.NewsViewModel;

public class SearchinActivity extends AppCompatActivity {

    private String keyword = "";
    private NewsViewModel newsViewModel;
    private SearchListAdapter adapter;
//    RecyclerView recyclerView;
//    SearchView searchView;
//    TextView backBtn;
    ActivitySearchinBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



binding.backBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SearchinActivity.this, MainActivity.class);
        startActivity(intent);
    }
});
        ViewModelProvider.Factory factory = new NewsViewModelFactory(this);
        newsViewModel = new ViewModelProvider(this, factory).get(NewsViewModel.class);


        setupRecyclerView();
        observeNewsArticles();
        setupSearchView();

//        // Open the search view programmatically
        binding.searchView.setIconified(false);
        binding.searchView.requestFocus();
    }

    private void setupRecyclerView() {
        adapter = new SearchListAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void observeNewsArticles() {
        if (newsViewModel != null) {
            newsViewModel.getNewsArticles().observe(this, new Observer<PagedList<Article>>() {
                @Override
                public void onChanged(PagedList<Article> articles) {
                    adapter.submitList(articles);
                }
            });
        } else {
            // Handle the case when newsViewModel is null
            // For example, display an error message or perform alternative actions
        }
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) {
                    keyword = newText;
                    performSearch();
                } else {
                    Toast.makeText(SearchinActivity.this, "Type more than two letters!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void performSearch() {
        if (newsViewModel != null) {
            newsViewModel.getNewsArticlesByKeyword(keyword).observe(this, new Observer<PagedList<Article>>() {
                @Override
                public void onChanged(PagedList<Article> articles) {
                    adapter.submitList(articles);
                }
            });
        }
    }
}
