package com.example.openweather;

import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.openweather.Current.CurWeather;
import com.example.openweather.Forecast.Forecast;
import com.example.openweather.Weather.Weather;
import com.example.openweather.Current.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

@RequiresApi(api = Build.VERSION_CODES.O)
public class WeatherDownloader implements Runnable {

    private static final String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd"));
    private final MainActivity mainActivity;

    private String unit;

    private double latitude;
    private double longitude;

    private static final String weatherURL = "https://api.openweathermap.org/data/2.5/onecall";      //weather URL
    private static final String yourAPIKey = "API KEY";            //API key

    private Util util;

    public WeatherDownloader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setLoc(double latitude, double longitude) {             //Setting Latitude and Longitude for main activity
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setUnit(String unit) {              //setting unit
        this.unit = unit;
    }       //setting Unit from main activity

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {
        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();               //building URL to fetch Data from Weather API
        buildURL.appendQueryParameter("lat", String.valueOf(latitude));
        buildURL.appendQueryParameter("lon", String.valueOf(longitude));
        buildURL.appendQueryParameter("appid", yourAPIKey);
        buildURL.appendQueryParameter("units", unit);
        buildURL.appendQueryParameter("lang", "en");
        buildURL.appendQueryParameter("exclude", "minutely");
        String urlToUse = buildURL.build().toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL weatherurl = new URL(urlToUse);
            HttpsURLConnection conn = (HttpsURLConnection) weatherurl.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {     //return error if any
                mainActivity.showtoast("Error in fetching Data");
                return;
            }

            InputStream istream = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(istream)));

            String s;
            while ((s = reader.readLine()) != null) {
                sb.append(s).append('\n');
            }
        } catch (Exception e) {
            mainActivity.showtoast("Error in fetching Data");
            return;
        }
        util = parseJSON(sb.toString());
        mainActivity.runOnUiThread(() -> {mainActivity.getWeather(util);});
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private Util parseJSON(String s) {          //method to parse the json we get from API
        try {
            JSONObject jObjMain = new JSONObject(s);
            long timezoneOffset = jObjMain.getLong("timezone_offset");

            return new Util(
                    getCurWeather(jObjMain.getJSONObject("current"), timezoneOffset),               //get array containing current weather information
                    getWeatherArr(jObjMain.getJSONArray("hourly"), timezoneOffset),             //get array containing hourly weather information
                    getForecastArr(jObjMain.getJSONArray("daily"), timezoneOffset),             //get array containing daily weather information
                    unit,                                                                           //set unit
                    locationInfo(latitude, longitude)              //get city name from geocoder
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private CurWeather getCurWeather(JSONObject current, long timezoneOffset) throws JSONException {
        JSONArray weather = current.getJSONArray("weather");            //get current weather information
        JSONObject jObjWeather = (JSONObject) weather.get(0);

        String date = dtFormatter(current.getLong("dt")+timezoneOffset,"EEE MMM dd h:mm a, yyyy");
        String temp = getTemp(current.getDouble("temp"));
        String feelslike = getTemp(current.getDouble("feels_like"));
        String desc = jObjWeather.getString("description") + " (" + current.getString("clouds") + "% " + jObjWeather.getString("main") + ")";
        String winds = String.valueOf(current.getDouble("wind_deg"));
        String humidity = current.getString("humidity");
        String uvi = current.getString("uvi");
        String vis = (current.getDouble("visibility")/1000)+(unit.equals("metric") ? " km" : " mi");
        String sunrise = dtFormatter(current.getLong("sunrise")+timezoneOffset,"h:mm a");
        String sunset = dtFormatter(current.getLong("sunset")+timezoneOffset,"h:mm a");
        String windspeed = current.getString("wind_speed");
        String icon = jObjWeather.getString("icon");

        return new CurWeather(date, temp, feelslike, desc, winds, humidity, uvi,vis, sunrise, sunset,windspeed, icon);      //setting all the values
    }

    private ArrayList<Weather> getWeatherArr(JSONArray weather, long timezoneOffset) throws JSONException {
        ArrayList<Weather> weatherArrayList = new ArrayList<>();            //get hourly weather information
        LocalDateTime dt;

        for (int i = 0; i < weather.length(); i++) {
            JSONObject jweather = (JSONObject) weather.get(i);
            JSONObject jObjWeather = (JSONObject) jweather.getJSONArray("weather").get(0);

            dt = LocalDateTime.ofEpochSecond(jweather.getLong("dt"), 0 , ZoneOffset.UTC);
            ZonedDateTime zdt = ZonedDateTime.of(dt, ZoneId.systemDefault());
            long originaldt = zdt.toInstant().toEpochMilli();

            String day = (today.equals(dtFormatter(jweather.getLong("dt")+timezoneOffset,"dd"))) ? "Today" : dtFormatter(jweather.getLong("dt")+timezoneOffset,"EEE");
            String temp= getTemp(jweather.getDouble("temp"));
            String time = dtFormatter(jweather.getLong("dt")+timezoneOffset,"h:mm a");
            String desc = jObjWeather.getString("description");
            String icon = jObjWeather.getString("icon");

            weatherArrayList.add(new Weather(day, temp, time,desc,icon, originaldt));       //setting all vales
        }
        return weatherArrayList;            //returning the array list for hourly weather
    }

    private ArrayList<Forecast> getForecastArr(JSONArray forecast, long timezoneOffset) throws JSONException {
        ArrayList<Forecast> forecastArrayList = new ArrayList<>();          //get daily weather info

        for (int i = 0; i < forecast.length(); i++) {
            JSONObject jObjForecast = (JSONObject) forecast.get(i);
            JSONObject temp = jObjForecast.getJSONObject("temp");
            JSONObject jObjWeather = (JSONObject) jObjForecast.getJSONArray("weather").get(0);

            int prep = (int) Math.round((Double.parseDouble(jObjForecast.getString("pop"))) * 100);

            String day = dtFormatter(jObjForecast.getLong("dt")+timezoneOffset,"EEEE,MM/dd");
            String mintemp = getTemp(temp.getDouble("min"));
            String maxtemp = getTemp(temp.getDouble("max"));
            String description = jObjWeather.getString("description");
            String prec = String.valueOf(prep);
            String uvind = jObjForecast.getString("uvi");
            String mtemp = getTemp(temp.getDouble("morn"));
            String dtemp = getTemp(temp.getDouble("day"));
            String etemp = getTemp(temp.getDouble("eve"));
            String ntemp = getTemp(temp.getDouble("night"));
            String icon = jObjWeather.getString("icon");

            forecastArrayList.add(new Forecast( day,mintemp, maxtemp,description,prec, uvind, mtemp,dtemp, etemp,ntemp,icon));          //setting all the values
        }
        return forecastArrayList;           //returning array list of forcast weather
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String dtFormatter(long time,String formate) {               //return formated date using dt and offset information
        LocalDateTime dt = LocalDateTime.ofEpochSecond(time, 0 , ZoneOffset.UTC);
        DateTimeFormatter df = DateTimeFormatter.ofPattern(formate,Locale.getDefault());
        String date = dt.format(df);
        return date;
    }

    private String getTemp(double temp) {           //return string with appropriate unit
        String unitformate = "";
        if(unit.equals("metric"))
             unitformate = "°C";
        else
            unitformate = "°F";

        String tempformate = (long)Math.ceil(temp) + unitformate;
        return tempformate;
    }

    public String locationInfo(double loc, double lon) {             //used to get location using Latitude and Longitude
        Geocoder geocoder = new Geocoder(mainActivity);
        String plce = "";
        String state = "";
        try {
            List<Address> address = geocoder.getFromLocation(loc, lon, 1);
            String country = address.get(0).getCountryCode();
            if (address == null || address.isEmpty()) {
                return null;
            }
            if (!country.equals("US")) {                //If country is not USA then return city name and Country
                plce = address.get(0).getLocality();
                if (plce == null)
                    plce = address.get(0).getSubAdminArea();
                state = address.get(0).getCountryName();

            } else {               //if country is USA then return name of city and State
                plce = address.get(0).getLocality();
                state = address.get(0).getAdminArea();
            }
            return plce + ", " + state;         //return location info
        } catch (IOException e) {
            return null;
        }
    }
}
