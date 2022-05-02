package com.example.openweather.Forecast;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openweather.R;

public class ForecastViewHolder extends RecyclerView.ViewHolder{

    TextView dayTime;
    TextView tempminmax;
    TextView description;
    TextView pop;
    TextView uvi;
    TextView time8am;
    TextView time1pm;
    TextView time5pm;
    TextView time11pm;
    ImageView imageView;

    public ForecastViewHolder(@NonNull View itemView) {
        super(itemView);

        dayTime = itemView.findViewById(R.id.for_date);
        tempminmax = itemView.findViewById(R.id.for_hltemp);
        description = itemView.findViewById(R.id.for_desc);
        pop = itemView.findViewById(R.id.for_precpt);
        uvi = itemView.findViewById(R.id.for_uvindex);
        time8am = itemView.findViewById(R.id.for_mtemp);
        time1pm = itemView.findViewById(R.id.for_dtemp);
        time5pm = itemView.findViewById(R.id.for_etemp);
        time11pm = itemView.findViewById(R.id.for_ntemp);
        imageView = itemView.findViewById(R.id.for_icon);
    }
}
