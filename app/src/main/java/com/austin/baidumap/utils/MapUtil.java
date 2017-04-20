package com.austin.baidumap.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.austin.baidumap.R;
import com.austin.baidumap.activities.BasicMap.InfoWindowActivity3;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

import static android.R.attr.value;


/**
 * Created by Austin on 2017/4/19.
 */

public class MapUtil {


    /**
     * 获取当前屏幕TextureMapView的LatLngBounds
     * @param mMapView
     * @return
     */
    public static LatLngBounds getScreenBounds(TextureMapView mMapView) {
        return getScreenBounds(mMapView.getMap(),mMapView.getWidth(), mMapView.getHeight());
    }

    /**
     * 获取当前屏幕MapView的LatLngBounds
     * @param mMapView
     * @return
     */
    public static LatLngBounds getScreenBounds(MapView mMapView){
        return getScreenBounds(mMapView.getMap(),mMapView.getWidth(), mMapView.getHeight());
    }


    private static LatLngBounds getScreenBounds(BaiduMap map, int width, int height) {
        Projection projection = map.getProjection();
        LatLng topRight = projection.fromScreenLocation(new Point(width, 0));
        LatLng bottomLeft = projection.fromScreenLocation(new Point(0, height));
        LatLngBounds.Builder builder = new LatLngBounds.Builder().include(topRight).include(bottomLeft);
        return builder.build();
    }
}
