package com.medi.dailynews.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.medi.dailynews.adapter.News_List_Adapter;
import com.medi.dailynews.R;
import com.medi.dailynews.databinding.FragmentWorldNewsBinding;
import com.medi.dailynews.models.Article;

import com.medi.dailynews.pagination.WorldNewsPagung.WorldNewsViewModelFactory;
import com.medi.dailynews.viewmodels.WorldNewsView.WorldNewsViewModel;


public class WorldNewsFragment extends Fragment {

    FragmentWorldNewsBinding binding;
    private WorldNewsViewModel worldNewsViewModel;
    private News_List_Adapter adapter;
    private String country = "us";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorldNewsBinding.inflate(inflater, container, false);

        // Create the NewsViewModel instance using a custom factory
        ViewModelProvider.Factory factory = new WorldNewsViewModelFactory(requireActivity().getApplication());
        worldNewsViewModel = new ViewModelProvider(this, factory).get(WorldNewsViewModel.class);

        setupRecyclerView();

        binding.sort.setOnClickListener(v -> {
            binding.emptyView.setVisibility(View.GONE);
            showCountrySelectionDialog();
        });

        binding.searchEditText.setOnClickListener(v -> startActivity( new Intent(getActivity(),SearchinActivity.class)));
        return binding.getRoot();
    }


    private void setupRecyclerView() {
        adapter = new News_List_Adapter(getActivity());
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void showCountrySelectionDialog() {
        String[] menuList = {"us", "in", "de", "au", "ca", "kr"};
        int[] flagResources = {R.drawable.united_states, R.drawable.india, R.drawable.germany, R.drawable.australia, R.drawable.canada, R.drawable.south_korea};
        int[] currentSort = {0}; // Array to store the selected index

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Country");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedCountry = menuList[currentSort[0]];
                String countryName = "";

                // Assign the country name based on the selected position
                switch (currentSort[0]) {
                    case 0:
                        countryName = "USA";
                        binding.ImageCountry.setImageResource(R.drawable.united_states);
                        break;
                    case 1:
                        countryName = "India";
                        binding.ImageCountry.setImageResource(R.drawable.india);
                        break;
                    case 2:
                        countryName = "Germany";
                        binding.ImageCountry.setImageResource(R.drawable.germany);
                        break;
                    case 3:
                        countryName = "Australia";
                        binding.ImageCountry.setImageResource(R.drawable.australia);
                        break;
                    case 4:
                        countryName = "Canada";
                        binding.ImageCountry.setImageResource(R.drawable.canada);
                        break;
                    case 5:
                        countryName = "South Korea";
                        binding.ImageCountry.setImageResource(R.drawable.south_korea);
                        break;
                }

                binding.textviewCountry.setText(countryName);
                country = selectedCountry; // Store the selected country
                fetchNewsArticles(); // Fetch news articles based on the selected country
            }
        });

        // Create a custom adapter with a custom layout for the dialog items
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), 0, menuList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_country_item, parent, false);
                }

                TextView textView = convertView.findViewById(R.id.textCountry);
                ImageView imageView = convertView.findViewById(R.id.ImageCountry);

                // Set the country name next to the menu item
                switch (position) {
                    case 0:
                        textView.setText(menuList[position] + " - USA");
                        break;
                    case 1:
                        textView.setText(menuList[position] + " - India");
                        break;
                    case 2:
                        textView.setText(menuList[position] + " - Germany");
                        break;
                    case 3:
                        textView.setText(menuList[position] + " - Australia");
                        break;
                    case 4:
                        textView.setText(menuList[position] + " - Canada");
                        break;
                    case 5:
                        textView.setText(menuList[position] + " - South Korea");
                        break;
                }

                // Set the country flag image
                imageView.setImageResource(flagResources[position]);

                return convertView;
            }
        };

        builder.setSingleChoiceItems(adapter, currentSort[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentSort[0] = which;
            }
        });

        AlertDialog customDialog = builder.create();
        customDialog.show();

        // Set positive button text color
        customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.RedColor));
    }



    private void fetchNewsArticles() {
        worldNewsViewModel.fetchNewsArticles(country); // Pass the selected country
        observeNewsArticles();
    }

    private void observeNewsArticles() {
        worldNewsViewModel.getNewsArticles().observe(getViewLifecycleOwner(), new Observer<PagedList<Article>>() {
            @Override
            public void onChanged(PagedList<Article> articles) {
                if (articles != null) {
                    adapter.submitList(articles);
                }
            }
        });
    }}