package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

public class County extends DataSupport {
    private int id;
    private String countyName;
    private String city_code;
    private int cityId;
    private String cityIdx;
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getCountyName(){
        return countyName;
    }
    public void setCountyName(String countyName){
        this.countyName=countyName;
    }
    public String getCity_code(){
        return city_code;
    }
    public void setWeatherId(String city_code){
        this.city_code=city_code;
    }
    public int getCityId(){
        return cityId;
    }
    public void setCityId(int cityId){
        this.cityId=cityId;
    }

    public String getCityIdx() {
        return cityIdx;
    }

    public void setCityIdx(String cityIdx) {
        this.cityIdx = cityIdx;
    }

}
