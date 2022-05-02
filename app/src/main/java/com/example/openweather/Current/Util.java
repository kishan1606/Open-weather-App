package com.example.openweather.Current;

import android.util.JsonWriter;

import com.example.openweather.Forecast.Forecast;
import com.example.openweather.Weather.Weather;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

public class Util {
    private CurWeather curWeather;
    ArrayList<Weather> weatherArrayList;
    ArrayList<Forecast> forecastArrayList;
    private String unit;
    private String timezone;

    public Util() {
    }

    public Util(String unit, String timezone) {
        this.unit = unit;
        this.timezone = timezone;
    }

    public Util(CurWeather curWeather, ArrayList<Weather> weatherArrayList, ArrayList<Forecast> forecastArrayList, String unit, String timezone) {
        this.timezone = timezone;
        this.curWeather = curWeather;
        this.forecastArrayList = forecastArrayList;
        this.weatherArrayList = weatherArrayList;
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public CurWeather getCurrent() {
        return curWeather;
    }

    public ArrayList<Forecast> getDailyArrayList() {
        return forecastArrayList;
    }

    public ArrayList<Weather> getHourlyArrayList() {
        return weatherArrayList;
    }

    public String getTimezone() {
        return timezone;
    }

    @Override
    public String toString() {

        try {
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name("timezone").value(getTimezone());
            jsonWriter.name("unit").value(getUnit());
            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Util{" +
                "timezone='" + timezone + '\'' +
                ", curWeather=" + curWeather +
                ", forecastArrayList=" + forecastArrayList +
                ", weatherArrayList=" + weatherArrayList +
                '}';
    }
}
