package com.austin.baidumap;

import android.app.Application;

/**
 * Created by Austin on 2017/4/10.
 */

public class MyApplication extends Application {

    private static BaiduLocationManager mManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mManager = BaiduLocationManager.getInstance(getApplicationContext());
    }

    public static BaiduLocationManager getLocationManager() {
        return mManager;
    }
}
