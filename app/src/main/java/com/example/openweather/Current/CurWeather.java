package com.example.openweather.Current;

public class CurWeather {

    private String date;
    private String temperature;
    private String feelslike;
    private String description;
    private String wind;
    private String humidity;
    private String uvindex;
    private String visibility;
    private String sunrise;
    private String sunset;
    private String windSpeed;
    private String icon;

    public CurWeather( String date, String temperature,String feelslike, String description,String wind, String humidity,
                       String uvindex, String visibility, String sunrise, String sunset,String windspeed, String icon) {
        this.date = date;
        this.temperature = temperature;
        this.feelslike = feelslike;
        this.description = description;
        this.wind = wind;
        this.humidity = humidity;
        this.uvindex = uvindex;
        this.visibility = visibility;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.windSpeed = windspeed;
        this.icon = icon;
    }
        public String getDate() {
            return date;
        }

        public String getTemperature() {
            return temperature;
        }

        public String getFeelslike() {
            return feelslike;
        }

        public String getDescription() {
            return description;
        }

        public String getWind() {
            return wind;
        }

        public String getHumidity() {
            return humidity;
        }

        public String getUvindex() {
            return uvindex;
        }

        public String getVisibility() {
            return visibility;
        }

        public String getSunrise() {
            return sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public String getWindSpeed() {
            return windSpeed;
        }

        public String getIcon() {
            return icon;
        }

        @Override
        public String toString() {
            return "CurWeather{" +
                    "date='" + date + '\'' +
                    ", temperature='" + temperature + '\'' +
                    ", feelslike='" + feelslike + '\'' +
                    ", description='" + description + '\'' +
                    ", wind='" + wind + '\'' +
                    ", humidity='" + humidity + '\'' +
                    ", uvindex='" + uvindex + '\'' +
                    ", visibility='" + visibility + '\'' +
                    ", sunrise='" + sunrise + '\'' +
                    ", sunset='" + sunset + '\'' +
                    ", windSpeed='" + windSpeed + '\'' +
                    ", icon='" + icon + '\'' +
                    '}';
        }
}




