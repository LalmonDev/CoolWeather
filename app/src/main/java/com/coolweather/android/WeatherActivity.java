package com.coolweather.android;

import android.os.Handler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolweather.android.db.SaveData;
import com.coolweather.android.gson.Forecast;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private TextView todayText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView weekText;
    private TextView noticeText;
    private List<SaveData> saveDataList;
    private ImageView bagImg;

    public SwipeRefreshLayout swipeRefresh;
    private String mWeatherId;

    private String savecsName;
    private String savecsCode;

    public DrawerLayout drawerLayout;
    private Button navButton;
    private Button searchBt;

    public FloatingActionButton addButton;
    public FloatingActionButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);


//        初始化控件
        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        todayText = findViewById(R.id.today_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        weekText = findViewById(R.id.week_text);
        noticeText = findViewById(R.id.notice_text);

        bagImg = findViewById(R.id.bag_img);

        drawerLayout = findViewById(R.id.drawer_layout);
        navButton = findViewById(R.id.nav_button);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        searchBt = findViewById(R.id.search_button);
        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, Search.class);
                startActivity(intent);
            }
        });

        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, CityManage.class);
                startActivity(intent);
            }
        });

        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                从数据库拉出来，鉴定是否已存在，以达到存单一城市
                saveDataList = DataSupport.where("csName = ?", savecsName).find(SaveData.class);
                if (saveDataList.size() > 0) {
                    Toast.makeText(WeatherActivity.this, "已收藏" + " " + savecsName + " " + savecsCode, Toast.LENGTH_SHORT).show();
                } else {
                    SaveData saveData = new SaveData();
                    saveData.setCsName(savecsName);
                    saveData.setCsCode(savecsCode);
                    saveData.save();
                    Toast.makeText(WeatherActivity.this, "收藏成功" + " " + savecsName + " " + savecsCode, Toast.LENGTH_SHORT).show();
                }
            }
        });

        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        Weather weatherload = Utility.handleWeatherInfoResponse(weatherString);

        String bagPic = prefs.getString("bag_pic", null);
        if (bagPic != null) {
            Glide.with(this).load(bagPic).into(bagImg);
        } else {
            loadBagPic();
        }

        mWeatherId = getIntent().getStringExtra("weather_id");
        System.out.println("mWeatherId"+mWeatherId);
//        weatherLayout.setVisibility(View.INVISIBLE);
        if (weatherString!=null) {
            System.out.println("Sadsadsads0");
            if (weatherload.cityInfo.cityId.equals(mWeatherId)) {
//            有缓存，直接解析天气数据
                    Weather weather = Utility.handleWeatherInfoResponse(weatherString);
//            mWeatherId = weather.cityInfo.cityId;
                    showWeatherInfo(weather);
                //requestWeather(mWeatherId);
            } else {
                System.out.println("Sadsadsads");
                weatherLayout.setVisibility(View.INVISIBLE);
                System.out.println("Sadsadsads-1");
                requestWeather(mWeatherId);
                System.out.println("Sadsadsads-2");
            }
        } else {
            System.out.println("Sadsadsads");
            weatherLayout.setVisibility(View.INVISIBLE);
            System.out.println("Sadsadsads1");
            requestWeather(mWeatherId);
            System.out.println("Sadsadsads2");
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
                Toast.makeText(WeatherActivity.this, "天气更新成功", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //    根据id请求天气数据
    public void requestWeather(String weatherId) {
        String weatherUrl = "http://t.weather.sojson.com/api/weather/city/" + weatherId;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherInfoResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && weather.status == 200) {

                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();

                            mWeatherId = weather.cityInfo.cityId;
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBagPic();
    }


    //    处理并展示Weather实体类中的数据
    public void showWeatherInfo(Weather weather) {
        savecsName = weather.cityInfo.city;
        savecsCode = weather.cityInfo.cityId;

        String cityName = weather.cityInfo.city;
        String updateTime = weather.cityInfo.updateTime;
        String degree = weather.data.wendu + "°C";
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        int num = 0;
        Forecast forecast1 = new Forecast();
        for (Forecast forecast : weather.data.forecastList) {
            if (num == 0)
                forecast1 = forecast;
            num++;
        }
        String weatherInfo = forecast1.ymd;
        todayText.setText(weatherInfo);
        weatherInfoText.setText(forecast1.type);
        weekText.setText(forecast1.week);
        noticeText.setText(forecast1.notice);
        forecastLayout.removeAllViews();

        for (Forecast forecast : weather.data.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dataText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dataText.setText(forecast.ymd);
            infoText.setText(forecast.type);
            maxText.setText("最" + forecast.high);
            minText.setText("最" + forecast.low);
            forecastLayout.addView(view);
        }
        aqiText.setText(weather.data.quality);
        pm25Text.setText("" + weather.data.pm25);
        weatherLayout.setVisibility(View.VISIBLE);
    }


    //  三个函数实现每刷新一次用消息队列更换背景照片
    //在消息队列中实现对控件的更改
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bitmap bmp = (Bitmap) msg.obj;
                    bagImg.setImageBitmap(bmp);
                    break;
            }
        }

        ;
    };

    private void loadBagPic() {
        final String url = "https://uploadbeta.com/api/pictures/random/";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bmp = getURLimage(url);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bmp;
                handle.sendMessage(msg);
            }
        }).start();
    }

    //加载图片
    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

//不返回上一活动，但是可以直接退出
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;//不执行父类点击事件
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }


}
