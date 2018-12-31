package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class Forecast {
    @SerializedName("ymd")
    public String ymd;

    @SerializedName("type")
    public String type;

    @SerializedName("high")
    public String high;

    @SerializedName("low")
    public String low;

    @SerializedName("week")
    public String week;

    @SerializedName("notice")
    public String notice;
}
