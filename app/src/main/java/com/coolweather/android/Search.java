package com.coolweather.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.coolweather.android.gson.Weather;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Search extends AppCompatActivity {
    private Button back;
    private Button search;
    private EditText searchText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        back=findViewById(R.id.back_button_2);
        search=findViewById(R.id.click_search_button);
        searchText=findViewById(R.id.search_input);

//        返回事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        搜索事件
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityID=searchText.getText().toString();
                checkID(cityID);
            }
        });
    }





//    从服务器搜索
    public void checkID(final String weatherId)
    {
        String weatherUrl="http://t.weather.sojson.com/api/weather/city/"+weatherId;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Weather weather=Utility.handleWeatherInfoResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather!=null && weather.status==200)
                        {
                            Intent intent=new Intent(Search.this,WeatherActivity.class);
                            intent.putExtra("weather_id",weatherId);
                            startActivity(intent);
                            finish();

                        }else {
                            Toast.makeText(Search.this,"搜索城市不存在",Toast.LENGTH_SHORT).show();
                            searchText.setText(null);
                        }
                    }
                });
            }
        });


    }
}
