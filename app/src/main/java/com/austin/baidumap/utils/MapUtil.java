package com.austin.baidumap.utils;

import android.graphics.Point;
import android.os.Handler;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;


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

    /**
     * 设置地图ZoomOut ZoomIn动画
     * @param mBaiduMap
     * @param duration
     * @param targetLatLng
     */
    public static void animateOutAndIn(final BaiduMap mBaiduMap, final int duration, final LatLng targetLatLng) {
        LatLngBounds bound = mBaiduMap.getMapStatus().bound;
        if(!bound.contains(targetLatLng)) {
            final int zoomDelta = 2;
            final float startZoom = mBaiduMap.getMapStatus().zoom;

            final MapStatus mapStatus = new MapStatus.Builder()
                    .target(targetLatLng)
                    .zoom(startZoom-zoomDelta)
                    .build();

            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus), duration);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MapStatus mapStatus = new MapStatus.Builder()
                            .target(targetLatLng)
                            .zoom(startZoom)
                            .build();
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus), duration / 2);
                }
            }, duration / 4);
        }else{//在屏幕内直接移动，不绽放
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(targetLatLng), duration/2);
        }

    }
}
