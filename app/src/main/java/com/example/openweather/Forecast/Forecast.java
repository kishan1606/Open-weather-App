package com.example.openweather.Forecast;

import java.io.Serializable;

public class Forecast implements Serializable {

    private String day;
    private String mintemp;
    private String maxtemp;
    private String description;
    private String prec;
    private String uvind;
    private String mtemp;
    private String dtemp;
    private String etemp;
    private String ntemp;
    private String icon;

    public Forecast(String day, String mintemp,String maxtemp,String description,String prec, String uvind, String mtemp,
                    String dtemp, String etemp, String ntemp, String icon) {
        this.day = day;
        this.mintemp = mintemp;
        this.maxtemp = maxtemp;
        this.description = description;
        this.prec = prec;
        this.uvind = uvind;
        this.mtemp = mtemp;
        this.dtemp = dtemp;
        this.etemp = etemp;
        this.ntemp = ntemp;
        this.icon = icon;
    }

    public String getDay() {
        return day;
    }

    public String getMintemp() {
        return mintemp;
    }

    public String getMaxtemp() {
        return maxtemp;
    }

    public String getDescription() {
        return description;
    }

    public String getPrec() {
        return prec;
    }

    public String getUvind() {
        return uvind;
    }

    public String getMtemp() {
        return mtemp;
    }

    public String getDtemp() {
        return dtemp;
    }

    public String getEtemp() {
        return etemp;
    }

    public String getNtemp() {
        return ntemp;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "day='" + day + '\'' +
                ", mintemp='" + mintemp + '\'' +
                ", maxtemp='" + maxtemp + '\'' +
                ", description='" + description + '\'' +
                ", prec='" + prec + '\'' +
                ", uvind='" + uvind + '\'' +
                ", mtemp='" + mtemp + '\'' +
                ", dtemp='" + dtemp + '\'' +
                ", etemp='" + etemp + '\'' +
                ", ntemp='" + ntemp + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
