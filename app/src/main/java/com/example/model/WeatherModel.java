package com.example.model;

public class WeatherModel {
    private String location;
    private double temperature;
    private String weatherCondition;
    private int iconWeather;
    private String dateTime;

    public WeatherModel() {
    }

    public WeatherModel(String location, double temperature, String weatherCondition, int iconWeather) {
        this.location = location;
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
        this.iconWeather = iconWeather;
    }

    public WeatherModel(double temperature, int iconWeather, String dateTime) {
        this.temperature = temperature;
        this.iconWeather = iconWeather;
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public int getIconWeather() {
        return iconWeather;
    }

    public void setIconWeather(int iconWeather) {
        this.iconWeather = iconWeather;
    }
}
