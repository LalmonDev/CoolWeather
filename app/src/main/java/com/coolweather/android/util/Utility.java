package com.coolweather.android.util;

import android.text.TextUtils;

import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;
import com.coolweather.android.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    /*
    解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvinces=new JSONArray(response);
                for (int i=0;i<allProvinces.length();i++){
                    JSONObject provinceObject=allProvinces.getJSONObject(i);
                    if (provinceObject.getString("pid").equals("0")) {
                        Province province = new Province();
                        province.setId(provinceObject.getInt("id"));
                        province.setProvinceName(provinceObject.getString("city_name"));
                        province.setProvinceCode(provinceObject.getString("city_code"));
                        province.save();
                    }
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }


    /*
    解析和处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response,int provinceId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities=new JSONArray(response);
                for (int i=0;i<allCities.length();i++){
                    JSONObject cityObject=allCities.getJSONObject(i);
                    if (cityObject.getInt("pid") == provinceId) {
                        City city = new City();
                        city.setIdx(String.valueOf(cityObject.getInt("id")));
                        city.setCityName(cityObject.getString("city_name"));
                        city.setCityCode(cityObject.getString("city_code"));
                        city.setProvinceId(provinceId);
                        city.save();
                    }
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
    解析和处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(String response,int cityId){
        int num=0;
        if (!TextUtils.isEmpty(response)){
            try{
                JSONArray allCounties=new JSONArray(response);
                for (int i=0;i<allCounties.length();i++){
                    JSONObject countyObject=allCounties.getJSONObject(i);
                    if (countyObject.getInt("pid")==cityId) {
                        num++;
                        County county = new County();
                        county.setCountyName(countyObject.getString("city_name"));
                        county.setWeatherId(countyObject.getString("city_code"));
                        county.setId(countyObject.getInt("id"));
                        county.setCityId(cityId);
                        county.save();
                    }
                }

                if(num==0) {
                    return false;
                }
                else
                    return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /*解析Weather实体类*/
    public static Weather handleWeatherInfoResponse(String response) {
        return new Gson().fromJson(response, Weather.class);
    }

}
