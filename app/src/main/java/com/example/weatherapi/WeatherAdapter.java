package com.example.weatherapi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private List<WeatherForecast> weatherForecastList;

    public WeatherAdapter(List<WeatherForecast> weatherForecastList) {
        this.weatherForecastList = weatherForecastList;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        WeatherForecast forecast = weatherForecastList.get(position);
        holder.temperatureTextView.setText("Temperature: " + forecast.getTemperature() + "°C");
        holder.descriptionTextView.setText("Description: " + forecast.getDescription());
    }

    @Override
    public int getItemCount() {
        return weatherForecastList.size();
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView temperatureTextView, descriptionTextView;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            temperatureTextView = itemView.findViewById(R.id.temperature);
            descriptionTextView = itemView.findViewById(R.id.description);
        }
    }
}
