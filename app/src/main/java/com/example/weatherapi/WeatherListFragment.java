package com.example.weatherapi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherListFragment extends Fragment {

    private RecyclerView recyclerView;
    private WeatherAdapter weatherAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch weather forecast data
        fetchWeatherForecast();

        return view;
    }

    private void fetchWeatherForecast() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeatherMapApi api = retrofit.create(OpenWeatherMapApi.class);
        String apiKey = "085320b0b3de9226f283ee15d62774ca";
        String lat = "0"; // replace with actual latitude
        String lon = "0"; // replace with actual longitude

        Call<WeatherForecastResponse> call = api.getWeatherForecast(lat, lon, apiKey);
        call.enqueue(new Callback<WeatherForecastResponse>() {
            @Override
            public void onResponse(Call<WeatherForecastResponse> call, Response<WeatherForecastResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<WeatherForecast> forecastList = response.body().getList().stream()
                            .map(item -> new WeatherForecast(
                                    item.getWeather().get(0).toString().getClass(),
                                    String.valueOf(item.getMain().getTemp()),
                                    item.getDtTxt()))
                            .collect(Collectors.toList());

                    weatherAdapter = new WeatherAdapter(forecastList);
                    recyclerView.setAdapter(weatherAdapter);
                }
            }

            @Override
            public void onFailure(Call<WeatherForecastResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
