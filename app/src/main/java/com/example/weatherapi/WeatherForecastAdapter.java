// WeatherForecastAdapter.java
package com.example.weatherapi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.ViewHolder> {

    private List<WeatherForecastResponse.ForecastItem> forecastList;

    public WeatherForecastAdapter(List<WeatherForecastResponse.ForecastItem> forecastList) {
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherForecastResponse.ForecastItem forecast = forecastList.get(position);
        holder.temperatureTextView.setText("Temperature: " + forecast.getMain().getTemp() + " °C");
        holder.descriptionTextView.setText("Description: " + forecast.getWeather().get(0).getDescription());
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView temperatureTextView;
        TextView descriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            temperatureTextView = itemView.findViewById(R.id.forecast_temperature);
            descriptionTextView = itemView.findViewById(R.id.forecast_description);
        }
    }
}
