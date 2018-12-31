package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class cityInfo {
    @SerializedName("city")
    public String city;//城市名称

    @SerializedName("cityId")
    public String cityId;//城市Id

    @SerializedName("updateTime")
    public String updateTime;//更新时间
}
