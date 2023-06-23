//
//package com.medi.dailynews.UI;
//
//
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.ProgressDialog;
//import android.app.SearchManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.drawable.ColorDrawable;
//import android.location.Location;
//import android.location.LocationManager;
//import android.os.Build;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.SearchView;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.core.view.MenuItemCompat;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.paging.PagedList;
//import androidx.recyclerview.widget.LinearLayoutManager;
//
//import android.provider.Settings;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.EditorInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.medi.dailynews.AdapterClass.HorizontalListAdapter;
//import com.medi.dailynews.AdapterClass.News_List_Adapter;
//
//import com.medi.dailynews.R;
//import com.medi.dailynews.databinding.FragmentNewsBinding;
//
//import com.medi.dailynews.utils.DataStatus;
//import com.medi.dailynews.viewmodels.NewsListView.NewsViewModel;
//import com.medi.dailynews.pagination.NewsListPaging.NewsViewModelFactory;
//import com.medi.dailynews.models.Article;
//import com.medi.weatherApp.Api.RetrofitUtilities;
//import com.medi.weatherApp.WHModels.WeatherModel;
//import com.medi.weatherApp.WeatherMainActivity;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//
//
//
//public class NewsFragment extends Fragment {
//
//    private FragmentNewsBinding binding;
//    private News_List_Adapter adapter;
//    private ProgressDialog dialog;
//    private String keyword = "";
//    private Context mContext;
//    private NewsViewModel newsViewModel;
//    private Location currentLocation;
//    private FusedLocationProviderClient fusedLocationProvider;
//
//    public static final String apiKey = "19bbe4c80bc95682ef4399d690f397de";
//
//    private static final int LOCATION_REQUEST_CODE = 101;
//    private HorizontalListAdapter listAdapter;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        binding = FragmentNewsBinding.inflate(inflater, container, false);
//
//
//        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireActivity());
//        getCurrentLocation();
//        dialog = new ProgressDialog(requireContext());
//        dialog.setTitle("Loading....");
//        dialog.show();
//
//        binding.city.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                getCityWeather(binding.city.getText().toString());
//                View focusedView = requireActivity().getCurrentFocus();
//                if (focusedView != null) {
//                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
//                    binding.city.clearFocus();
//                }
//                return true;
//            } else {
//                return false;
//            }
//        });
//        binding.WeatherMainLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), WeatherMainActivity.class);
//                startActivity(intent);
//            }
//        });
//        binding.searchEditText.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), SearchinActivity.class);
//            startActivity(intent);
//
//        });
//
//        binding.mainLayout.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), WeatherMainActivity.class);
//            startActivity(intent);
//        });
//
//        // Set the refresh listener
//        // Refresh your data here
//        binding.swipeRefresh.setOnRefreshListener(this::refreshData);
//
//        // Create the NewsViewModel instance using a custom factory
//        ViewModelProvider.Factory factory = new NewsViewModelFactory(requireContext());
//        newsViewModel = new ViewModelProvider(this, factory).get(NewsViewModel.class);
//        setupHorizontallRecyclerView();
//        setupRecyclerView();
//        observeNewsArticles();
//        subscribeObservers();
//        setHasOptionsMenu(true);
//        return binding.getRoot();
//    }
//
//    private void subscribeObservers() {
//        newsViewModel.newsArticles.observe(getViewLifecycleOwner(), new Observer<PagedList<Article>>() {
//            @Override
//            public void onChanged(PagedList<Article> articles) {
//                adapter.submitList(articles);
//            }
//        });
//
//        newsViewModel.getDataStatus().observe(getViewLifecycleOwner(), new Observer<DataStatus>() {
//            @Override
//            public void onChanged(DataStatus dataStatus) {
//                switch (dataStatus) {
//
//                    case LOADED:
//                        binding.emptyView.setVisibility(View.INVISIBLE);
//                        binding.swipeRefresh.setRefreshing(false);
//                        binding.textViewTopHeadlines.setVisibility(View.VISIBLE);
//                        break;
//                    case LOADING:
//                        binding.textViewTopHeadlines.setVisibility(View.INVISIBLE);
//                        binding.emptyView.setVisibility(View.INVISIBLE);
//                        binding.swipeRefresh.setRefreshing(true);
//                        break;
//                    case EMPTY:
//                        binding.swipeRefresh.setRefreshing(false);
//                        binding.textViewTopHeadlines.setVisibility(View.INVISIBLE);
//                        binding.emptyView.setVisibility(View.VISIBLE);
//                        binding.emptyView.setText(R.string.no_news_found);
//                        break;
//                    case ERROR:
//                        binding.swipeRefresh.setRefreshing(false);
//                        binding.textViewTopHeadlines.setVisibility(View.INVISIBLE);
//                        binding.emptyView.setVisibility(View.VISIBLE);
//                        binding.emptyView.setText(R.string.no_internet_connection);
//                        break;
//                }
//            }
//        });
//    }
//
//    private  void setupHorizontallRecyclerView(){
//        listAdapter = new HorizontalListAdapter(getActivity());
//        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
//        binding.RecyclerViewHH.setLayoutManager(layoutManager);
//        binding.RecyclerViewHH.setAdapter(listAdapter);
//    }
//    private void setupRecyclerView() {
//        adapter = new News_List_Adapter(getActivity());
//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//        binding.recyclerView.setAdapter(adapter);
//    }
//
//    private void observeNewsArticles() {
//        if (newsViewModel != null) {
//            newsViewModel.getNewsArticles().observe(getViewLifecycleOwner(), new Observer<PagedList<Article>>() {
//                @Override
//                public void onChanged(PagedList<Article> articles) {
//                    adapter.submitList(articles);
//                    dialog.dismiss();
//                }
//            });
//        } else {
//            // Handle the case when newsViewModel is null
//            // For example, display an error message or perform alternative actions
//            dialog.dismiss();
//        }
//
//
//    }
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        mContext = context;
//    }
//
////    @Override
////    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
////        inflater.inflate(R.menu.search_view, menu);
////        searchKeywordFromSearchView(menu);
////        super.onCreateOptionsMenu(menu, inflater);
////    }
////
////    private void searchKeywordFromSearchView(Menu menu) {
////        SearchManager searchManager = (SearchManager) mContext.getSystemService(Context.SEARCH_SERVICE);
////        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
////        MenuItem searchMenuItem = menu.findItem(R.id.search);
////
////        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
////        searchView.setQueryHint("Search Latest News...");
////        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////            @Override
////            public boolean onQueryTextSubmit(String query) {
////                return false;
////            }
////
////            @Override
////            public boolean onQueryTextChange(String newText) {
////                if (newText.length() > 2) {
////                    keyword = newText;
////                    performSearch();
////                } else {
////                    Toast.makeText(getActivity(), "Type more than two letters!", Toast.LENGTH_SHORT).show();
////                }
////                return false;
////            }
////        });
////
////
////        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
////            @Override
////            public boolean onMenuItemActionExpand(MenuItem item) {
////                binding.searchEditText.setVisibility(View.GONE);
////                EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
////                searchEditText.requestFocus();
////                return true;
////            }
////
////            @Override
////            public boolean onMenuItemActionCollapse(MenuItem item) {
////                binding.searchEditText.setVisibility(View.VISIBLE);
////                keyword = "";
////                return true;
////            }
////        });
////        searchMenuItem.getIcon().setVisible(false, false);
////
////
////    }
////    private void performSearch() {
////        if (newsViewModel != null) {
////            newsViewModel.getNewsArticlesByKeyword(keyword).observe(getViewLifecycleOwner(), new Observer<PagedList<Article>>() {
////                @Override
////                public void onChanged(PagedList<Article> articles) {
////                    adapter.submitList(articles);
////                    dialog.dismiss();
////                }
////            });
////        }
////    }
//
//    private void refreshData() {
//        // Perform your data refresh operation here
//
//        // For example, you can call the fetchNewsArticlesByKeyword method again
//        if (newsViewModel != null) {
//            newsViewModel.getNewsArticlesByKeyword(keyword).observe(getViewLifecycleOwner(), new Observer<PagedList<Article>>() {
//                @Override
//                public void onChanged(PagedList<Article> articles) {
//                    adapter.submitList(articles);
//                    dialog.dismiss();
//                    binding.swipeRefresh.setRefreshing(false); // Hide the refresh indicator
//                }
//            });
//        }
//    }
//
//
//    public void getCurrentLocation() {
//        if (checkPermissions()) {
//            if (isLocationEnabled()) {
//                if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                        && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    requestPermission();
//                    return;
//                }
//
//                fusedLocationProvider.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null) {
//
//                            currentLocation = location;
//                            fetchCurrentLocationWeather(
//                                    String.valueOf(location.getLatitude()),
//                                    String.valueOf(location.getLongitude())
//                            );
//                        }
//                    }
//                });
//
//            } else {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//            }
//        } else {
//            requestPermission();
//        }
//    }
//
//    private void requestPermission() {
//        ActivityCompat.requestPermissions(
//                requireActivity(),
//                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
//                LOCATION_REQUEST_CODE
//        );
//    }
//
//    private boolean isLocationEnabled() {
//        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//    }
//
//    private boolean checkPermissions() {
//        return ActivityCompat.checkSelfPermission(
//                requireActivity(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                requireActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void fetchCurrentLocationWeather(String latitude, String longitude) {
//        RetrofitUtilities.getApiService().getCurrentWeatherData(latitude, longitude, apiKey)
//                .enqueue(new Callback<WeatherModel>() {
//                    @RequiresApi(api = Build.VERSION_CODES.O)
//                    @Override
//                    public void onResponse(Call<WeatherModel> call, @NonNull Response<WeatherModel> response) {
//                        if (response.isSuccessful()) {
//                            WeatherModel weatherModel = response.body();
//                            if (weatherModel != null) {
//                                setData(weatherModel); // Set weather data in the UI
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<WeatherModel> call, Throwable t) {
//                        // Handle failure
//                    }
//                });
//    }
//
//    private void getCityWeather(String city) {
//        RetrofitUtilities.getApiService().getCityWeatherData(city, apiKey).enqueue(new Callback<WeatherModel>() {
//            @Override
//            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
//                if (response.isSuccessful()) {
//                    WeatherModel weatherModel = response.body();
//                    if (weatherModel != null) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            setData(weatherModel); // Set weather data in the UI
//                        }
//                    }
//                } else {
//                    Toast.makeText(getActivity(), "No City Found", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<WeatherModel> call, Throwable t) {
//                // Handle failure
//            }
//        });
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void setData(WeatherModel body) {
//        binding.dateTime.setText(getCurrentDate());
//        binding.Temp.setText("" + k2c(body.getMain().getTemp()) + "°");
//        binding.weatherTitle.setText(body.getWeather().get(0).getMain());
//        binding.city.setText(body.getName());
//        updateUI(body.getWeather().get(0).getId());
//        binding.feelsLike.setText("" + k2c(body.getMain().getFeels_like()) + "°");
//
//
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//
//    private double k2c(double t) {
//        double intTemp = t - 273;
//        return BigDecimal.valueOf(intTemp).setScale(1, RoundingMode.UP).doubleValue();
//    }
//
//    private String getCurrentDate() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
//        Date currentDate = new Date();
//        return dateFormat.format(currentDate);
//    }
//
//    // Update app bar background
//
//    private void updateUI ( int id){
//        switch (id) {
//            //Thunderstorm
//            case 200:
//            case 201:
//            case 202:
//            case 210:
//            case 211:
//            case 212:
//            case 221:
//            case 230:
//            case 231:
//            case 232:
//                binding.weatherImg.setImageResource(R.drawable.ic_storm_weather);
//                binding.mainLayout.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.thunderstrom_bg));
//
//                break;
//            //Drizzle
//            case 300:
//            case 301:
//            case 302:
//            case 310:
//            case 311:
//            case 312:
//            case 313:
//            case 314:
//            case 321:
//                binding.weatherImg.setImageResource(R.drawable.ic_few_clouds);
//                binding.mainLayout.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.drizzle_bg));
//                break;
//            //Rain
//            case 500:
//            case 501:
//            case 502:
//            case 503:
//            case 504:
//            case 511:
//            case 520:
//            case 521:
//            case 522:
//            case 531:
//                binding.weatherImg.setImageResource(R.drawable.ic_rainy_weather);
//                binding.mainLayout.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.rain_bg));
//
//                break;
//            //Snow
//            case 600:
//            case 601:
//            case 602:
//            case 611:
//            case 612:
//            case 613:
//            case 615:
//            case 616:
//            case 620:
//            case 621:
//            case 622:
//                binding.weatherImg.setImageResource(R.drawable.ic_snow_weather);
//                binding.mainLayout.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.snow_bg));
//                break;
//            //Atmosphere
//            case 701:
//            case 711:
//            case 721:
//            case 731:
//            case 741:
//            case 751:
//            case 761:
//            case 762:
//            case 771:
//            case 781:
//                binding.weatherImg.setImageResource(R.drawable.ic_broken_clouds);
//                binding.mainLayout.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.atmosphere_bg));
//
//                break;
//            //Clear
//            case 800:
//                binding.weatherImg.setImageResource(R.drawable.ic_clear_day);
//                binding.mainLayout.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.clear_bg));
//
//                break;
//            //Clouds
//            case 801:
//            case 802:
//            case 803:
//            case 804:
//                binding.weatherImg.setImageResource(R.drawable.ic_cloudy_weather);
//                binding.mainLayout.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.clouds_bg));
//
//                break;
//            //unknown
//            default:
//                binding.weatherImg.setImageResource(R.drawable.ic_unknown);
//                binding.mainLayout.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.unknown_bg));
//                break;
//        }
//    }
//}

package com.medi.dailynews.UI;



import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.medi.LoginPage.AccountActivity;
import com.medi.dailynews.adapter.News_List_Adapter;

import com.medi.dailynews.R;
import com.medi.dailynews.databinding.FragmentNewsBinding;

import com.medi.dailynews.utils.DataStatus;
import com.medi.dailynews.viewmodels.NewsListView.NewsViewModel;
import com.medi.dailynews.pagination.NewsListPaging.NewsViewModelFactory;
import com.medi.dailynews.models.Article;
import com.medi.weatherApp.Api.RetrofitUtilities;
import com.medi.weatherApp.WHModels.WeatherModel;
import com.medi.weatherApp.WeatherMainActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;
    private News_List_Adapter adapter;
    private ProgressDialog dialog;
    private final String keyword = "";
    private NewsViewModel newsViewModel;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProvider;

    public static final String apiKey = "19bbe4c80bc95682ef4399d690f397de";

    private static final int LOCATION_REQUEST_CODE = 101;

    private final List<Article> recentlyClickedArticles = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);

        binding.AccountBtn.setOnClickListener(v -> startActivity( new Intent(getActivity(),AccountActivity.class)));


        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireActivity());
        getCurrentLocation();
        dialog = new ProgressDialog(requireContext());
        dialog.setTitle("Loading....");
        dialog.show();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (account != null) {
            String name = account.getDisplayName();
            binding.username.setText(name);

        }
        binding.city.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getCityWeather(binding.city.getText().toString());
                View focusedView = requireActivity().getCurrentFocus();
                if (focusedView != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                    binding.city.clearFocus();
                }
                return true;
            } else {
                return false;
            }
        });
        binding.WeatherMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WeatherMainActivity.class);
                startActivity(intent);
            }
        });
        binding.searchEditText.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchinActivity.class);
            startActivity(intent);

        });

        binding.mainLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WeatherMainActivity.class);
            startActivity(intent);
        });



        // Set the refresh listener
        // Refresh your data here
        binding.swipeRefresh.setOnRefreshListener(this::refreshData);

        // Create the NewsViewModel instance using a custom factory
        ViewModelProvider.Factory factory = new NewsViewModelFactory(requireContext());
        newsViewModel = new ViewModelProvider(this, factory).get(NewsViewModel.class);

        setupRecyclerView();
        observeNewsArticles();
        subscribeObservers();
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    private void subscribeObservers() {
        newsViewModel.newsArticles.observe(getViewLifecycleOwner(), new Observer<PagedList<Article>>() {
            @Override
            public void onChanged(PagedList<Article> articles) {
                adapter.submitList(articles);
            }
        });

        newsViewModel.getDataStatus().observe(getViewLifecycleOwner(), new Observer<DataStatus>() {
            @Override
            public void onChanged(DataStatus dataStatus) {
                switch (dataStatus) {

                    case LOADED:
                        binding.emptyView.setVisibility(View.INVISIBLE);
                        binding.swipeRefresh.setRefreshing(false);
                        binding.textViewTopHeadlines.setVisibility(View.VISIBLE);
                        break;
                    case LOADING:
                        binding.textViewTopHeadlines.setVisibility(View.INVISIBLE);
                        binding.emptyView.setVisibility(View.INVISIBLE);
                        binding.swipeRefresh.setRefreshing(true);
                        break;
                    case EMPTY:
                        binding.swipeRefresh.setRefreshing(false);
                        binding.textViewTopHeadlines.setVisibility(View.INVISIBLE);
                        binding.emptyView.setVisibility(View.VISIBLE);
                        binding.emptyView.setText(R.string.no_news_found);
                        break;
                    case ERROR:
                        binding.swipeRefresh.setRefreshing(false);
                        binding.textViewTopHeadlines.setVisibility(View.INVISIBLE);
                        binding.emptyView.setVisibility(View.VISIBLE);
                        binding.emptyView.setText(R.string.no_internet_connection);
                        break;
                }
            }
        });
    }


    private void setupRecyclerView() {
        adapter = new News_List_Adapter(getActivity());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void observeNewsArticles() {
        if (newsViewModel != null) {
            newsViewModel.getNewsArticles().observe(getViewLifecycleOwner(), new Observer<PagedList<Article>>() {
                @Override
                public void onChanged(PagedList<Article> articles) {
                    adapter.submitList(articles);
                    dialog.dismiss();
                }
            });
        } else {
            // Handle the case when newsViewModel is null
            // For example, display an error message or perform alternative actions
            dialog.dismiss();
        }


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private void refreshData() {
        // Perform your data refresh operation here

        // For example, you can call the fetchNewsArticlesByKeyword method again
        if (newsViewModel != null) {
            newsViewModel.getNewsArticlesByKeyword(keyword).observe(getViewLifecycleOwner(), new Observer<PagedList<Article>>() {
                @Override
                public void onChanged(PagedList<Article> articles) {
                    adapter.submitList(articles);
                    dialog.dismiss();
                    binding.swipeRefresh.setRefreshing(false); // Hide the refresh indicator
                }
            });
        }
    }


    public void getCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermission();
                    return;
                }

                fusedLocationProvider.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {

                            currentLocation = location;
                            fetchCurrentLocationWeather(
                                    String.valueOf(location.getLatitude()),
                                    String.valueOf(location.getLongitude())
                            );
                        }
                    }
                });

            } else {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                requireActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_REQUEST_CODE
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void fetchCurrentLocationWeather(String latitude, String longitude) {
        RetrofitUtilities.getApiService().getCurrentWeatherData(latitude, longitude, apiKey)
                .enqueue(new Callback<WeatherModel>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(Call<WeatherModel> call, @NonNull Response<WeatherModel> response) {
                        if (response.isSuccessful()) {
                            WeatherModel weatherModel = response.body();
                            if (weatherModel != null) {
                                setData(weatherModel); // Set weather data in the UI
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherModel> call, Throwable t) {
                        // Handle failure
                    }
                });
    }

    private void getCityWeather(String city) {
        RetrofitUtilities.getApiService().getCityWeatherData(city, apiKey).enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                if (response.isSuccessful()) {
                    WeatherModel weatherModel = response.body();
                    if (weatherModel != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            setData(weatherModel); // Set weather data in the UI
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "No City Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                // Handle failure
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setData(WeatherModel body) {
        binding.dateTime.setText(getCurrentDate());
        binding.Temp.setText("" + k2c(body.getMain().getTemp()) + "°");
        binding.weatherTitle.setText(body.getWeather().get(0).getMain());
        binding.city.setText(body.getName());
        updateUI(body.getWeather().get(0).getId());
        binding.feelsLike.setText("" + k2c(body.getMain().getFeels_like()) + "°");


    }

    @RequiresApi(Build.VERSION_CODES.O)

    private double k2c(double t) {
        double intTemp = t - 273;
        return BigDecimal.valueOf(intTemp).setScale(1, RoundingMode.UP).doubleValue();
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    // Update app bar background

    private void updateUI ( int id){
        switch (id) {
            //Thunderstorm
            case 200:
            case 201:
            case 202:
            case 210:
            case 211:
            case 212:
            case 221:
            case 230:
            case 231:
            case 232:
                binding.weatherImg.setImageResource(R.drawable.ic_storm_weather);
                binding.mainLayout.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.thunderstrom_bg));

                break;
            //Drizzle
            case 300:
            case 301:
            case 302:
            case 310:
            case 311:
            case 312:
            case 313:
            case 314:
            case 321:
                binding.weatherImg.setImageResource(R.drawable.ic_few_clouds);
                binding.mainLayout.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.drizzle_bg));
                break;
            //Rain
            case 500:
            case 501:
            case 502:
            case 503:
            case 504:
            case 511:
            case 520:
            case 521:
            case 522:
            case 531:
                binding.weatherImg.setImageResource(R.drawable.ic_rainy_weather);
                binding.mainLayout.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.rain_bg));

                break;
            //Snow
            case 600:
            case 601:
            case 602:
            case 611:
            case 612:
            case 613:
            case 615:
            case 616:
            case 620:
            case 621:
            case 622:
                binding.weatherImg.setImageResource(R.drawable.ic_snow_weather);
                binding.mainLayout.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.snow_bg));
                break;
            //Atmosphere
            case 701:
            case 711:
            case 721:
            case 731:
            case 741:
            case 751:
            case 761:
            case 762:
            case 771:
            case 781:
                binding.weatherImg.setImageResource(R.drawable.ic_broken_clouds);
                binding.mainLayout.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.atmosphere_bg));

                break;
            //Clear
            case 800:
                binding.weatherImg.setImageResource(R.drawable.ic_clear_day);
                binding.mainLayout.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.clear_bg));

                break;
            //Clouds
            case 801:
            case 802:
            case 803:
            case 804:
                binding.weatherImg.setImageResource(R.drawable.ic_cloudy_weather);
                binding.mainLayout.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.clouds_bg));

                break;
            //unknown
            default:
                binding.weatherImg.setImageResource(R.drawable.ic_unknown);
                binding.mainLayout.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.unknown_bg));
                break;
        }
    }
}
