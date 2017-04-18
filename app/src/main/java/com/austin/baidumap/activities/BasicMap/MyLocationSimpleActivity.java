package com.austin.baidumap.activities.BasicMap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.austin.baidumap.BaiduLocationManager;
import com.austin.baidumap.MyApplication;
import com.austin.baidumap.R;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

public class MyLocationSimpleActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private BaiduLocationManager locationManager;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);

        mMapView = (TextureMapView) findViewById(R.id.mapView);

        mBaiduMap = mMapView.getMap();
        //设置空白地图，直到定位成功后，再显示普通地图，这样解决了一开始定位北京的问题。
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);

        //此方法不能解决一直显示空白地图的问题
        mMapView.setBackgroundColor(Color.TRANSPARENT);

        mMapView.postDelayed(new Runnable() {
            @Override
            public void run() {
                startLocation();
            }
        }, 1000);

    }

    private void startLocation() {
        locationManager = MyApplication.getLocationManager();

        mBaiduMap.setMyLocationEnabled(true);

        locationManager.setOnReceiveBaiduLocationListener(false, new BaiduLocationManager.OnReceiveBaiduLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                MyLocationData myLocationData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        .direction(location.getDirection())
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build();

                mBaiduMap.setMyLocationData(myLocationData);

                MapStatus mapStatus = new MapStatus.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .build();


                if(isFirst) {
                    isFirst = false;

                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));

                    //地图下面显示一个加载地图时间较长的点位图，这样解决了网速慢的情况下一直显示空白页的问题。
                    final View viewById = findViewById(R.id.loading);
                    AlphaAnimation animation = new AlphaAnimation(1, 0);
                    animation.setDuration(500);
                    animation.setFillAfter(true);
                    viewById.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            viewById.setVisibility(View.GONE);
                            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });

                }




            }
        });

        locationManager.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        locationManager.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
}
