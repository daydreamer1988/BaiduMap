package com.austin.baidumap.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Austin on 2017/4/18.
 */

public class SensorUtil implements SensorEventListener {

    private static SensorManager mSensorManager;
    private static Context context;
    private static OnSensorChangedListener listener;
    private static SensorUtil instance;
    private double lastX;

    public interface OnSensorChangedListener{
        void onSensorChanged(double direction);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            if(listener!=null){
                listener.onSensorChanged(x);
            }
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public static void registerSensor(Context context, OnSensorChangedListener listener){
        SensorUtil.listener = listener;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        instance = new SensorUtil();
        mSensorManager.registerListener(
                instance,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    public static void unRegisterSensor(){
        mSensorManager.unregisterListener(instance);
    }
}
