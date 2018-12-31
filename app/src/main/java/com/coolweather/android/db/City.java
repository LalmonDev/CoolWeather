package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

public class City extends DataSupport {
    private int id;
    private String cityName;
    private String city_code;
    private int provinceId;
    private String idx;

    public void setId(int id){
        this.id=id;
    }
    public String getCityName(){
        return cityName;
    }
    public void setCityName(String cityName){
        this.cityName=cityName;
    }
    public String getCity_code(){
        return city_code;
    }
    public void setCityCode(String city_code){
        this.city_code=city_code;
    }
    public int getProvinceId(){
        return provinceId;
    }
    public void setProvinceId(int provinceId){
        this.provinceId=provinceId;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public int getId() {
        Integer integer;
        integer = Integer.valueOf(idx);
        return integer.intValue();
    }

}
