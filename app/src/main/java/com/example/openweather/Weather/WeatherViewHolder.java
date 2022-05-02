package com.example.openweather.Weather;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openweather.R;

public class WeatherViewHolder extends RecyclerView.ViewHolder{
    TextView dayName;
    TextView time;
    TextView temp;
    TextView tempDesc;
    ImageView imageView;

    public WeatherViewHolder(@NonNull View itemView) {
        super(itemView);
        dayName = itemView.findViewById(R.id.rec_day);
        time = itemView.findViewById(R.id.rec_time);
        temp = itemView.findViewById(R.id.rec_temp);
        tempDesc = itemView.findViewById(R.id.rec_description);
        imageView = itemView.findViewById(R.id.rec_img);
    }
}
