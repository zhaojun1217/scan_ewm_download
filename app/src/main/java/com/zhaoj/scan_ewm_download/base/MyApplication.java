package com.zhaoj.scan_ewm_download.base;


import com.uuzuche.lib_zxing.ZApplication;

public class MyApplication extends ZApplication {

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
