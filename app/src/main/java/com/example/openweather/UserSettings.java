package com.example.openweather;

import android.util.JsonWriter;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;

public class UserSettings {

    private String unit;
    private double latitude;
    private double longitude;

    public UserSettings(String unit, double latitude, double longitude) {
        this.unit = unit;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getunit() {
        return unit;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @NonNull
    public String toString() {

        try {
            StringWriter sw = new StringWriter();           //passing value to local json for storing last user data
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name("unit").value(getunit());
            jsonWriter.name("latitude").value(getLatitude());
            jsonWriter.name("longitude").value(getLongitude());
            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
