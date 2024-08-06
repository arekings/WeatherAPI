package com.example.weatherapi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView temperatureTextView;
    private TextView descriptionTextView;
    private LinearLayout weatherInfoLayout;
    private Button toggleButton;

    private RecyclerView  weatherRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        temperatureTextView = findViewById(R.id.temperature);
        descriptionTextView = findViewById(R.id.description);
        weatherInfoLayout = findViewById(R.id.weather_info_layout);
        toggleButton = findViewById(R.id.toggle_button);
        weatherRecyclerView = findViewById(R.id.weather_recycler_view);


        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weatherInfoLayout.getVisibility() == View.GONE) {
                    weatherInfoLayout.setVisibility(View.VISIBLE);
                    toggleButton.setText("Hide Weather Info");
                } else {
                    weatherInfoLayout.setVisibility(View.GONE);
                    toggleButton.setText("Show Weather Info");
                }
            }
        });

        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        mMap.setMyLocationEnabled(true);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 10));
                            fetchCurrentWeather(currentLatLng);
                            fetchFutureWeather(currentLatLng);
                        }
                    }
                });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                fetchCurrentWeather(latLng);
                fetchFutureWeather(latLng);
            }
        });
    }

    private void fetchCurrentWeather(LatLng latLng) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        OpenWeatherMapApi api = retrofit.create(OpenWeatherMapApi.class);
        String apiKey = "085320b0b3de9226f283ee15d62774ca";

        Call<CurrentWeatherResponse> call = api.getCurrentWeather(String.valueOf(latLng.latitude), String.valueOf(latLng.longitude), apiKey);
        call.enqueue(new Callback<CurrentWeatherResponse>() {
            @Override
            public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    String weatherDescription = response.body().getWeather().get(0).getDescription();
                    String temperature = String.valueOf(response.body().getMain().getTemp());
                    String dateTime = response.body().getDtTxt(); // Assuming you have a date-time field


                    temperatureTextView.setText("Temperature: " + temperature + " °C");
                    descriptionTextView.setText("Description: " + weatherDescription);

                    mMap.addMarker(new MarkerOptions().position(latLng).title("Weather: " + weatherDescription + ", Temp: " + temperature));
                }
            }

            @Override
            public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


private void fetchFutureWeather(LatLng latLng) {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    OpenWeatherMapApi api = retrofit.create(OpenWeatherMapApi.class);
    String apiKey = "085320b0b3de9226f283ee15d62774ca";

    Call<WeatherForecastResponse> call = api.getWeatherForecast(String.valueOf(latLng.latitude), String.valueOf(latLng.longitude), apiKey);
    call.enqueue(new Callback<WeatherForecastResponse>() {
        @Override
        public void onResponse(Call<WeatherForecastResponse> call, Response<WeatherForecastResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
                List<WeatherForecastResponse.ForecastItem> forecastList = response.body().getList();
                WeatherForecastAdapter adapter = new WeatherForecastAdapter(forecastList);
                weatherRecyclerView.setAdapter(adapter);
            }
        }

        @Override
        public void onFailure(Call<WeatherForecastResponse> call, Throwable t) {
            t.printStackTrace();
        }
    });
}



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 10));
                                        fetchCurrentWeather(currentLatLng);
                                    }
                                }
                            });
                }
            }
        }
    }
}
