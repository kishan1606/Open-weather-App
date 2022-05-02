package com.example.openweather.Weather;

public class Weather {

    private String day;
    private String temperature;
    private String time;
    private String description;
    private String icon;
    private Long originaltime;          //used to navigate to a specific date in calender as calender need unformatted date and time

    public Weather(){

    }
    public Weather(String day, String temperature,String time,String description, String icon,Long originaltime) {
        this.day = day;
        this.temperature = temperature;
        this.time = time;
        this.description = description;
        this.icon = icon;
        this.originaltime = originaltime;
    }

    public String getDay() {
        return day;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public Long getOriginaltime() {
        return originaltime;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "day='" + day + '\'' +
                ", temperature='" + temperature + '\'' +
                ", time='" + time + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
