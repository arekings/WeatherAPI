package com.example.weatherapi;

import java.util.Date;
import java.util.List;

public class WeatherForecast {

    private final String description;
    private final String temperature;
    private final String time;

    public WeatherForecast(Class<?> description, String temperature, String time) {
        this.description = String.valueOf(description);
        this.temperature = temperature;
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getTime() {
        return time;
    }

    public List<Object> getWeather() {
        return getWeather();
    }

    public CurrentWeatherResponse.Main getMain() {
        return getMain();
    }

    public String getDtTxt() {
        return getDescription();
    }

    public Date getDateTime() {
        return getDateTime();
    }
}
