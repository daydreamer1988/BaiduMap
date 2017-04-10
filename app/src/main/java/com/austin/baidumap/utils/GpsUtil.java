package com.austin.baidumap.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.location.LocationManager;
import android.provider.Settings;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Austin on 2017/4/10.
 */

public class GpsUtil {



    public interface OnRegistGpsStateListener{
        void onReceiveGspState(boolean isGpsOpened);
    }



    public static boolean isGpsOpened(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 推荐使用的gps监听方法， 与registerGpsReceiver2相比，该方法回调是在主线程中
     * @param context
     * @param listener
     */
    public static void registerGpsReceiver(Context context, final OnRegistGpsStateListener listener) {
        IntentFilter intentfilter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent==null) return;
                String action = intent.getAction();
                if (action.equals(LocationManager.PROVIDERS_CHANGED_ACTION)){
                    listener.onReceiveGspState(isGpsOpened(context));
                }
            }
        }, intentfilter);
    }

    /**
     *
     * @param context
     * @param listener
     * @return 注意在Activity onDestory时记得unregisterContentObserver
     */
    public static ContentObserver registerGpsObserver(final Context context, final OnRegistGpsStateListener listener) {
        ContentObserver observer = new ContentObserver(null) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                listener.onReceiveGspState(isGpsOpened(context));
            }
        };
        context.getContentResolver().registerContentObserver(Settings.Secure.getUriFor(Settings.System.LOCATION_PROVIDERS_ALLOWED), false, observer);
        return observer;
    }

    public static void unregisterContentObserver(Context context, ContentObserver contentObserver) {
        if(contentObserver==null) return;
        context.getContentResolver().unregisterContentObserver(contentObserver);
    }
}
