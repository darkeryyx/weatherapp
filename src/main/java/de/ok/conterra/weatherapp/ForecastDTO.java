package de.ok.conterra.weatherapp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ForecastDTO {

    private long time;
    private double temp;
    @JsonProperty("feels_like")
    private double feelsLike;
    private int humidity;

    public ForecastDTO(long time, double temp, double feelsLike, int humidity) {
        this.time = time;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
    }

    // Getter
    public long getTime() { return time; }
    public double getTemp() { return temp; }
    public double getFeelsLike() { return feelsLike; }
    public int getHumidity() { return humidity; }

}

