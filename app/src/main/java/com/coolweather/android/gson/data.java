package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class data {
    @SerializedName("wendu")
    public String wendu;//当前温度

    @SerializedName("quality")
    public String quality;//空气质量


    public double pm25;

    @SerializedName("forecast")
    public List<Forecast> forecastList;
}
