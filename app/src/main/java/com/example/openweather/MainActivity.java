package com.example.openweather;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.openweather.Forecast.Forecast;
import com.example.openweather.Weather.WeatherAdapter;
import com.example.openweather.Current.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView location;
    TextView timestamp;
    TextView temperature;
    TextView feelLike;
    TextView winds;
    TextView brokenCloudAndWind;
    TextView humidity;
    TextView uvIndex;
    TextView visibility;
    TextView mtemp;
    TextView dtemp;
    TextView etemp;
    TextView ntemp;
    TextView mtime;
    TextView dtime;
    TextView etime;
    TextView ntime;
    TextView sunrise;
    TextView sunset;
    ImageView imageView;

    RecyclerView recyclerView;
    WeatherAdapter weatherAdapter;
    private static final String TAG = "MainActivity";
    private String unit = "imperial";           //initial unit ans lat, lon
    private double lat = 41.8675766;
    private double lon = -87.616232;
    String loc;

    private boolean isNW = true;
    private Menu menu;
    private MenuItem umenu;
    private ArrayList<Forecast> forecastArrayList;
    private int ct = 0;
    Geocoder geocoder;
    Intent i;
    private SwipeRefreshLayout swiper;
    private final ArrayList<UserSettings> userset = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean b = hasNetworkConnection();             //check internet connection
        if (!b) {
            isNW = false;
            setContentView(R.layout.nointernetconn);
        } else {
            isNW = true;

            setContentView(R.layout.activity_main);

            swiper = findViewById(R.id.swiper);
            location = findViewById(R.id.main_location);
            timestamp = findViewById(R.id.main_datetime);
            temperature = findViewById(R.id.main_temperature);
            feelLike = findViewById(R.id.main_feelslike);
            winds = findViewById(R.id.main_wind);
            brokenCloudAndWind = findViewById(R.id.main_desc);
            humidity = findViewById(R.id.main_humidity);
            uvIndex = findViewById(R.id.main_uvindex);
            visibility = findViewById(R.id.main_visibility);
            mtemp = findViewById(R.id.main_mtemp);
            dtemp = findViewById(R.id.main_dtemp);
            etemp = findViewById(R.id.main_etemp);
            ntemp = findViewById(R.id.main_ntemp);
            mtime = findViewById(R.id.main_mtime);
            dtime = findViewById(R.id.main_dtime);
            etime = findViewById(R.id.main_etime);
            ntime = findViewById(R.id.main_ntime);
            sunrise = findViewById(R.id.main_sunrise);
            sunset = findViewById(R.id.main_sunset);
            imageView = findViewById(R.id.imageView);

            this.setTitle("Open Weather");
            recyclerView = findViewById(R.id.rv_dailyweather);
            weatherAdapter = new WeatherAdapter(this);
            recyclerView.setAdapter(weatherAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            userset.clear();
            userset.addAll(loadFile());         //load user settings from json
            UserSettings us = userset.get(0);
            unit = us.getunit();
            lat = us.getLatitude();
            lon = us.getLongitude();

            doDownload();

            swiper.setOnRefreshListener(() -> {             //refresh
                boolean b1 = hasNetworkConnection();
                if (!b1) {
                    isNW = false;
                    showtoast("no internet connection");
                } else {
                    doDownload();
                    showtoast("Refreshing...");
                }
                swiper.setRefreshing(false);
            });
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void doDownload() {
        WeatherDownloader weatherDownloader = new WeatherDownloader(this);
        weatherDownloader.setUnit(unit);
        weatherDownloader.setLoc(lat, lon);
        new Thread(weatherDownloader).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onPause() {
        boolean b = hasNetworkConnection();
        if(!b){
            super.onPause();
        }else {
            UserSettings us = new UserSettings(unit,lat,lon);
            userset.clear();
            userset.add(us);
            saveProduct();
            super.onPause();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void NWConn(){                        //check internet connection;
        boolean b = hasNetworkConnection();
        if (!b) {
            ct = 1;
            isNW = false;
            setContentView(R.layout.nointernetconn);
        } else if(!isNW) {
            ct = 0;
            setContentView(R.layout.activity_main);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setContentView(R.layout.activity_main);
                doDownload();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {         //creating option menu
        MenuInflater inf = getMenuInflater();
        inf.inflate( R.menu.weather_menu, menu );
        umenu = menu.findItem(R.id.menu_unit);
        updatePlayStatus();
        return true;
    }

    private void updatePlayStatus() {           //setting initial menu icon of unit saved in json
        if (!unit.equals("metric")) {
            umenu.setIcon(R.drawable.units_f);
        } else {
            umenu.setIcon(R.drawable.units_c);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_unit){
            NWConn();
            if (ct == 1){
                return true;
            }
            WeatherDownloader weatherDownloader = new WeatherDownloader(this);
            if (unit.equals("metric")) {            //changing units
                unit = "imperial";
                item.setIcon(R.drawable.units_f);
                showtoast("Changing Unit to °F");
            } else {
                unit = "metric";
                item.setIcon(R.drawable.units_c);
                showtoast("Changing Unit to °C");
            }

            UserSettings us = new UserSettings(unit, lat, lon);
            userset.clear();
            userset.addAll(Collections.singleton(us));
            saveProduct();
            weatherDownloader.setUnit(unit);
            weatherDownloader.setLoc(lat, lon);
            new Thread(weatherDownloader).start();
            return true;
        }
        else if(item.getItemId() == R.id.menu_dforcast){            //opening daily forecast
            NWConn();
            if (ct == 1){
                return true;
            }
            Intent forecast = new Intent(this, DailyForeCastActivity.class);
            forecast.putExtra("dailyList", forecastArrayList);
            forecast.putExtra("loc", loc);
            startActivity(forecast);
            return true;
        }
        else if(item.getItemId() == R.id.menu_location){            //opening dialog box for passing location
            NWConn();
            if (ct == 1){
                return true;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            EditText et = new EditText(this);
            WeatherDownloader wd = new WeatherDownloader(this);
            wd.setUnit(unit);
            et.setInputType(InputType.TYPE_CLASS_TEXT);
            et.setGravity(Gravity.CENTER_HORIZONTAL);
            builder.setView(et);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String txt = et.getText().toString();
                    if (txt == null) {
                        showtoast("Enter valid location");
                    } else {
                        double[] arr = latLon(txt);
                        if (arr == null) {
                            showtoast("enter valid location");
                        } else {
                            lat = arr[0];
                            lon = arr[1];

                            UserSettings us = new UserSettings(unit, lat, lon);
                            userset.clear();
                            userset.addAll(Collections.singleton(us));
                            saveProduct();

                            wd.setLoc(arr[0], arr[1]);
                            new Thread(wd).start();
                        }
                    }
                }
            });
            builder.setNegativeButton("CANCEL", null);

            builder.setMessage("For US locations, enter as 'City', or 'City,State' \n \n For International locations enter as 'City,Country'");
            builder.setTitle("Enter a Location");

            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        }
        else {
            showtoast("error");
        }
        return super.onOptionsItemSelected(item);
    }

    void showtoast(String message) {            //show toast message
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public int getIcon(String name) {       //get icon from drawable
        return this.getResources().getIdentifier("_"+name, "drawable", this.getPackageName());
    }

    private String getDirection(double degrees) {       //setting wind direction using degrees
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X";
    }

    @SuppressLint({"SetTextI18n", "NewApi"})
    public void getWeather(Util util) {         //get the weather data and set it in layout

        forecastArrayList = util.getDailyArrayList();
        Forecast forecast = forecastArrayList.get(0);
        weatherAdapter.setHourlyArrayList(util.getHourlyArrayList());
        recyclerView.setAdapter(weatherAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        loc = util.getTimezone();
        location.setText(loc);
        timestamp.setText(util.getCurrent().getDate());
        temperature.setText(util.getCurrent().getTemperature());
        imageView.setImageResource(getIcon(util.getCurrent().getIcon()));
        feelLike.setText("Feels Like "+util.getCurrent().getFeelslike());
        winds.setText("Winds: " + getDirection(Double.parseDouble(util.getCurrent().getWind())) + " at " + Math.ceil(Double.parseDouble(util.getCurrent().getWindSpeed())) + (unit.equals("metric") ? " kmph" : " mph"));
        brokenCloudAndWind.setText(util.getCurrent().getDescription());
        humidity.setText("Humidity: "+util.getCurrent().getHumidity()+"%");
        uvIndex.setText("UV Index: "+util.getCurrent().getUvindex());
        visibility.setText("Visibility: "+util.getCurrent().getVisibility());
        mtemp.setText(forecast.getMtemp());
        dtemp.setText(forecast.getDtemp());
        etemp.setText(forecast.getEtemp());
        ntemp.setText(forecast.getNtemp());
        mtime.setText("8AM");
        dtime.setText("1PM");
        etime.setText("6PM");
        ntime.setText("11PM");
        sunrise.setText("Sunrise: "+util.getCurrent().getSunrise());
        sunset.setText("Sunset: "+util.getCurrent().getSunset());
    }

    public double[] latLon(String userProvidedLocation) {           //get Latitude and Longitude of place inserted
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addr = geocoder.getFromLocationName(userProvidedLocation, 1);
            if (addr == null || addr.isEmpty()) {
                return null;
            }
            return new double[] {addr.get(0).getLatitude(), addr.get(0).getLongitude()};
        } catch (IOException e) {
            return null;
        }
    }

    public ArrayList<UserSettings> loadFile() {             //load user settings from local JSON file WeatherSettings.json

        ArrayList<UserSettings> setlist = new ArrayList<>();
        try {
            InputStream is = getApplicationContext().openFileInput("WeatherSettings.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String unt = jsonObject.getString("unit");
                double lat = jsonObject.getDouble("latitude");
                double lon = jsonObject.getDouble("longitude");
                UserSettings us = new UserSettings(unt, lat, lon);
                setlist.add(us);
            }
        } catch (FileNotFoundException e) {
            UserSettings us = new UserSettings(unit, lat, lon);
            userset.clear();
            userset.addAll(Collections.singleton(us));
            saveProduct();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return setlist;
    }

    private void saveProduct() {                //save user settings to local JSON file WeatherSettings.json
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("WeatherSettings.json", Context.MODE_PRIVATE);
            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(userset);
            printWriter.close();
            fos.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

}