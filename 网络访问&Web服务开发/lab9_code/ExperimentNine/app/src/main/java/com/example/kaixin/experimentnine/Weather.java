package com.example.kaixin.experimentnine;

/**
 * Created by kaixin on 2016/11/27.
 */

public class Weather {
    private String date;
    private String weather;
    private String temperature;

    public Weather(String date, String weather, String temperature) {
        this.date = date;
        this.weather = weather;
        this.temperature = temperature;
    }

    public String getDate() {
        return date;
    }
    public String getWeather() {
        return weather;
    }
    public String getTemperature() {
        return temperature;
    }
}
