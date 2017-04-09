package com.austin.baidumap.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Austin on 2017/4/10.
 */

public class GpsUtil {

    public static boolean isGpsOpened(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void changeGPSState(Context context)throws Exception {
        boolean before = isGpsOpened(context);
        ContentResolver resolver = context.getContentResolver();
        if (before){
            Settings.Secure.putInt(resolver,Settings.Secure.LOCATION_MODE,Settings.Secure.LOCATION_MODE_OFF);
        }else {
            Settings.Secure.putInt(resolver,Settings.Secure.LOCATION_MODE,Settings.Secure.LOCATION_MODE_HIGH_ACCURACY);
        }
    }
}
