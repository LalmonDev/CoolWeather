package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

public class SaveData extends DataSupport {
    public String csName;
    public String csCode;

    public String getCsCode() {
        return csCode;
    }

    public void setCsCode(String csCode) {
        this.csCode = csCode;
    }

    public String getCsName() {

        return csName;
    }

    public void setCsName(String csName) {
        this.csName = csName;
    }
}
