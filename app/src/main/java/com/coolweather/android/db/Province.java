package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

public class Province  extends DataSupport {
    private int id;
    private String provinceName;
    private String city_code;
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getProvinceName(){
        return provinceName;
    }
    public void setProvinceName(String provinceName){
        this.provinceName=provinceName;
    }
    public String getCity_code(){
        return city_code;
    }
    public void setProvinceCode(String city_code){
        this.city_code=city_code;
    }

}
