package com.medi.weatherApp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


import com.google.android.gms.tasks.OnSuccessListener;
import com.medi.dailynews.MainActivity;
import com.medi.dailynews.R;
import com.medi.dailynews.UI.SearchinActivity;
import com.medi.dailynews.databinding.ActivityWeatherMainBinding;
import com.medi.weatherApp.Api.RetrofitUtilities;
import com.medi.weatherApp.WHModels.WeatherModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WeatherMainActivity extends AppCompatActivity {

    ActivityWeatherMainBinding binding;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProvider;
    private static final int LOCATION_REQUEST_CODE = 101;
    public static final String apiKey = "19bbe4c80bc95682ef4399d690f397de";

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWeatherMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);
//        getCurrentLocation();
        fusedLocationProvider.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null) {

                    currentLocation = location;
                    binding.progressBar.setVisibility(View.VISIBLE);
                    fetchCurrentLocationWeather(
                            String.valueOf(location.getLatitude()),
                            String.valueOf(location.getLongitude())
                    );
                }

            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherMainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        binding.citySearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getCityWeather(binding.citySearch.getText().toString());
                    View focusedView = getCurrentFocus();
                    if (focusedView != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                        binding.citySearch.clearFocus();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

    }


    private void fetchCurrentLocationWeather(String latitude, String longitude) {
        RetrofitUtilities.getApiService().getCurrentWeatherData(latitude, longitude, apiKey)
                .enqueue(new Callback<WeatherModel>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                        if (response.isSuccessful()) {
                            binding.progressBar.setVisibility(View.GONE);

                            WeatherModel weatherModel = response.body();
                            if (weatherModel != null) {
                                setData(weatherModel);
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
        binding.progressBar.setVisibility(View.VISIBLE);
        RetrofitUtilities.getApiService().getCityWeatherData(city, apiKey).enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                if (response.isSuccessful()) {
                    binding.progressBar.setVisibility(View.GONE);
                    WeatherModel weatherModel = response.body();
                    if (weatherModel != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            setData(weatherModel);
                        }
                    }
                } else {
                    Toast.makeText(WeatherMainActivity.this, "No City Found", Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
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
        binding.maxTemp.setText("Max " + k2c(body.getMain().getTemp_max()) + "°");
        binding.minTemp.setText("Min " + k2c(body.getMain().getTemp_min()) + "°");
        binding.temp.setText("" + k2c(body.getMain().getTemp()) + "°");
        binding.weatherTitle.setText(body.getWeather().get(0).getMain());

        binding.sunriseValue.setText(ts2td(body.getSys().getSunrise()));
        binding.sunsetValue.setText(ts2td(body.getSys().getSunset()));
        binding.pressureValue.setText(String.valueOf(body.getMain().getPressure()));
        binding.humidityValue.setText(body.getMain().getHumidity() + "%");
        binding.tempFValue.setText(String.valueOf(Math.round(k2c(body.getMain().getTemp()) * 1.8 + 32)) + "°");
        binding.citySearch.setText(body.getName());
        binding.feelsLike.setText("" + k2c(body.getMain().getFeels_like()) + "°");
        binding.windValue.setText(body.getWind().getSpeed() + "m/s");
        binding.groundValue.setText(String.valueOf(body.getMain().getGrnd_level()));
        binding.seaValue.setText(String.valueOf(body.getMain().getSea_level()));
        binding.countryValue.setText(body.getSys().getCountry());
        updateUI(body.getWeather().get(0).getId());

    }





    @RequiresApi(Build.VERSION_CODES.O)
    private String ts2td(long ts) {
        LocalTime localTime = Instant.ofEpochSecond(ts)
                .atZone(ZoneId.systemDefault())
                .toLocalTime();
        return localTime.toString();
    }

    private double k2c(double t) {
        double intTemp = t - 273;
        return BigDecimal.valueOf(intTemp).setScale(1, RoundingMode.UP).doubleValue();
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    private void updateUI(int id) {
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
                binding.mainLayout.setBackground(ContextCompat.getDrawable(WeatherMainActivity.this, R.drawable.thunderstrom_bg));
                binding.optionsLayout.setBackground(ContextCompat.getDrawable(WeatherMainActivity.this, R.drawable.thunderstrom_bg));
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
                binding.mainLayout.setBackground(ContextCompat.getDrawable(WeatherMainActivity.this, R.drawable.drizzle_bg));
                binding.optionsLayout.setBackground(ContextCompat.getDrawable(WeatherMainActivity.this, R.drawable.drizzle_bg));
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
                binding.mainLayout.setBackground(ContextCompat.getDrawable(WeatherMainActivity.this, R.drawable.rain_bg));
                binding.optionsLayout.setBackground(ContextCompat.getDrawable(WeatherMainActivity.this, R.drawable.rain_bg));
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
                binding.mainLayout.setBackground(ContextCompat.getDrawable(WeatherMainActivity.this, R.drawable.snow_bg));
                binding.optionsLayout.setBackground(ContextCompat.getDrawable(WeatherMainActivity.this, R.drawable.snow_bg));
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
                binding.mainLayout.setBackground(ContextCompat.getDrawable(WeatherMainActivity.this, R.drawable.atmosphere_bg));
                binding.optionsLayout.setBackground(ContextCompat.getDrawable(WeatherMainActivity.this, R.drawable.atmosphere_bg));
                break;
            //Clear
            case 800:
                binding.weatherImg.setImageResource(R.drawable.ic_clear_day);
                binding.mainLayout.setBackground(ContextCompat.getDrawable(WeatherMainActivity.this, R.drawable.clear_bg));
                binding.optionsLayout.setBackground(ContextCompat.getDrawable(WeatherMainActivity.this, R.drawable.clear_bg));
                break;
            //Clouds
            case 801:
            case 802:
            case 803:
            case 804:
                binding.weatherImg.setImageResource(R.drawable.ic_cloudy_weather);
                binding.mainLayout.setBackground(ContextCompat.getDrawable(WeatherMainActivity.this, R.drawable.clouds_bg));
                binding.optionsLayout.setBackground(ContextCompat.getDrawable(WeatherMainActivity.this, R.drawable.clouds_bg));
                break;
            //unknown
            default:
                binding.weatherImg.setImageResource(R.drawable.ic_unknown);
                binding.mainLayout.setBackground(ContextCompat.getDrawable(WeatherMainActivity.this, R.drawable.unknown_bg));
                binding.optionsLayout.setBackground(ContextCompat.getDrawable(WeatherMainActivity.this, R.drawable.unknown_bg));
                break;
        }
    }
}