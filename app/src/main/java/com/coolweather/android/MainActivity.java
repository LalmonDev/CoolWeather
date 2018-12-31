package com.coolweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.coolweather.android.gson.Weather;
import com.coolweather.android.util.Utility;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("weather",null)!=null)
        {
            String weatherString=prefs.getString("weather",null);
            Weather weather=Utility.handleWeatherInfoResponse(weatherString);
            System.out.println("weather"+weather.cityInfo.cityId);
            Intent intent=new Intent(this,WeatherActivity.class);
            intent.putExtra("weather_id",weather.cityInfo.cityId);
            startActivity(intent);
            finish();
        }
    }
}
