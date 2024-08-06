package com.example.weatherapi;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherForecastResponse {

    @SerializedName("list")
    private List<ForecastItem> list;

    public List<ForecastItem> getList() {
        return list;
    }

    public class ForecastItem {
        @SerializedName("main")
        private Main main;

        @SerializedName("weather")
        private List<Weather> weather;

        @SerializedName("dt_txt")
        private String dtTxt;

        public Main getMain() {
            return main;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public String getDtTxt() {
            return dtTxt;
        }

        public class Main {
            @SerializedName("temp")
            private double temp;

            public double getTemp() {
                return temp;
            }
        }

        public class Weather {
            @SerializedName("description")
            private String description;

            public String getDescription() {
                return description;
            }
        }
    }
}
