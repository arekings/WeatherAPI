package com.example.weatherapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapApi {
    @GET("weather")
    Call<CurrentWeatherResponse> getCurrentWeather(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("appid") String apiKey
    );

    @GET("forecast")
    Call<WeatherForecastResponse> getWeatherForecast(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("appid") String apiKey
    );
}
