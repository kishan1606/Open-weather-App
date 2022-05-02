package com.example.openweather.Weather;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openweather.MainActivity;
import com.example.openweather.R;

import java.util.ArrayList;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> implements View.OnClickListener {
    private ArrayList<Weather> weatherArrayList;
    private MainActivity mainActivity;

    public WeatherAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setHourlyArrayList(ArrayList<Weather> weatherArrayList) {
        this.weatherArrayList = weatherArrayList;
    }
    long dt = 0;

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_hrly_weather, parent, false);
        view.setOnClickListener(this);              //used to navigate to calender on specific date
        return new WeatherViewHolder(view);
    }

    public void onClick(View v) {           //redirect to specific day on calender using unformatted date
        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, dt);          //passing date to calender
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
        mainActivity.startActivity(intent);         //starting activity
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        Weather weather = weatherArrayList.get(position);           //setting all data to recycler view
        dt = weather.getOriginaltime();         //getting date for reaching to specific time in calender
        holder.dayName.setText(weather.getDay());
        holder.time.setText(weather.getTime());
        holder.temp.setText(weather.getTemperature());
        holder.tempDesc.setText(weather.getDescription());
        holder.imageView.setImageResource(mainActivity.getIcon(weather.getIcon()));
    }

    @Override
    public int getItemCount() {
        if (weatherArrayList == null) {
            return 0;
        }
        return weatherArrayList.size();
    }
}
