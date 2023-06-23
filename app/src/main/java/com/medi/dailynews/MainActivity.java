package com.medi.dailynews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import com.medi.OpenAI.ChatBotActivity;

import com.medi.dailynews.UI.WorldNewsFragment;
import com.medi.dailynews.databinding.ActivityMainBinding;

import com.medi.dailynews.UI.BookMarkFragment;


import com.medi.dailynews.UI.CategoryFragment;
import com.medi.dailynews.UI.NewsFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.Supportfab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ChatBotActivity.class);
            startActivity(intent);

        });
        bottomNavigationView = findViewById(R.id.Bootom_nav);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container_view, new NewsFragment())
                    .setReorderingAllowed(true)
                    .commit();
        }


        binding.BootomNav.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.page_news:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container_view, new NewsFragment())
                            .setReorderingAllowed(true)
                            .commit();
                    break;
                case R.id.page_catigary:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container_view, new CategoryFragment())
                            .setReorderingAllowed(true)
                            .commit();
                    break;
                case R.id.page_articles:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container_view, new WorldNewsFragment())
                            .setReorderingAllowed(true)
                            .commit();
                    break;
                case R.id.page_favorites:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container_view, new BookMarkFragment())
                            .setReorderingAllowed(true)
                            .commit();
                    break;
                default:
                    break;
            }
            return true;
        });
    }
}

