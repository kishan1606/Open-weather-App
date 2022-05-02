package com.example.openweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.openweather.Forecast.ForecastAdapter;
import com.example.openweather.Forecast.Forecast;

import java.util.ArrayList;

public class DailyForeCastActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ForecastAdapter forecastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_forecast);

        ArrayList<Forecast> forecastArrayList = (ArrayList<Forecast>) getIntent().getSerializableExtra("dailyList");

        recyclerView = findViewById(R.id.rv_forecast);
        Intent intent = getIntent();
        String s = intent.getStringExtra("loc");            //getting city name from the main activity and setting it to the title
        this.setTitle(s);

        forecastAdapter = new ForecastAdapter(forecastArrayList, this);
        recyclerView.setAdapter(forecastAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}