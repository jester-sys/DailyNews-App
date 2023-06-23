package com.medi.dailynews.UI;

import android.app.ProgressDialog;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;



import com.medi.dailynews.adapter.News_List_Adapter;


import com.medi.dailynews.R;
import com.medi.dailynews.databinding.FragmentCatigaryBinding;


import com.medi.dailynews.viewmodels.CategoryView.CatNewsViewModel;
import com.medi.dailynews.pagination.CategoryPaging.CatNewsViewModelFactory;
import com.medi.dailynews.models.Article;




import androidx.annotation.NonNull;


public class CategoryFragment extends Fragment  {
    private FragmentCatigaryBinding binding;
    private News_List_Adapter adapter;
    private ProgressDialog dialog;
    private String category = "";
    private CatNewsViewModel newsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
//            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
//        }
        binding = FragmentCatigaryBinding.inflate(inflater, container, false);
        dialog = new ProgressDialog(requireContext());
        dialog.setTitle("Loading....");
        dialog.show();

        setupSpinner();



        // Create the NewsViewModel instance using a custom factory
        ViewModelProvider.Factory factory = new CatNewsViewModelFactory(requireContext());
        newsViewModel = new ViewModelProvider(this, factory).get(CatNewsViewModel.class);

        setupRecyclerView();

        return binding.getRoot();
    }


    private void setupSpinner() {
        Spinner spinner = binding.spinnerCategory;
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.categories_array, // Array resource containing category names
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        // Handle item selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                category = getCategory(selectedCategory);
                if (category != null) {
                    fetchNewsArticles();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new News_List_Adapter(getActivity());
        binding.recyclerViewCat.setHasFixedSize(true);
        binding.recyclerViewCat.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewCat.setAdapter(adapter);
    }

    private void fetchNewsArticles() {
        newsViewModel.fetchNewsArticles(category); // Pass the selected category
        observeNewsArticles();
    }

    private void observeNewsArticles() {
        newsViewModel.getNewsArticles().observe(getViewLifecycleOwner(), new Observer<PagedList<Article>>() {
            @Override
            public void onChanged(PagedList<Article> articles) {
                if (articles != null) {
                    adapter.submitList(articles);
                    dismissProgressDialog();
                }
            }
        });
    }

    private String getCategory(String selectedCategory) {
        String category = null;
        switch (selectedCategory) {
            case "business":
                category = "business";
                break;
            case "entertainment":
                category = "entertainment";
                break;
            case "health":
                category = "health";
                break;
            case "science":
                category = "science";
                break;
            case "sports":
                category = "sports";
                break;
            case "technology":
                category = "technology";
                break;
        }
        return category;
    }

    private void dismissProgressDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void refreshData() {
        // Perform your data refresh operation here

        // For example, you can call the fetchNewsArticles method again
        fetchNewsArticles();
    }



}

