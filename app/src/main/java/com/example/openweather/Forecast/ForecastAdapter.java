package com.example.openweather.Forecast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openweather.DailyForeCastActivity;
import com.example.openweather.R;

import java.util.ArrayList;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastViewHolder> {

    private ArrayList<Forecast> forecastArrayList;
    private DailyForeCastActivity dailyForeCastActivity;

    public ForecastAdapter(ArrayList<Forecast> forecastArrayList, DailyForeCastActivity dailyForeCastActivity) {
        this.forecastArrayList = forecastArrayList;
        this.dailyForeCastActivity = dailyForeCastActivity;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_daily_forecast, parent, false);
        return new ForecastViewHolder(view);
    }

    public int getIcon(String name) {
        return dailyForeCastActivity.getResources().getIdentifier("_"+name, "drawable", dailyForeCastActivity.getPackageName());
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {            //setting all the vales
        Forecast forecast = forecastArrayList.get(position);

        holder.dayTime.setText(forecast.getDay());
        holder.tempminmax.setText(forecast.getMaxtemp()+"/"+ forecast.getMintemp());
        holder.description.setText(forecast.getDescription());
        holder.pop.setText("(" + forecast.getPrec() + "%preciptation)");
        holder.uvi.setText("UV Index: " + forecast.getUvind());
        holder.time8am.setText(forecast.getMtemp());
        holder.time1pm.setText(forecast.getDtemp());
        holder.time5pm.setText(forecast.getEtemp());
        holder.time11pm.setText(forecast.getNtemp());
        holder.imageView.setImageResource(getIcon(forecast.getIcon()));
    }

    @Override
    public int getItemCount() {
        return forecastArrayList.size();
    }

}
