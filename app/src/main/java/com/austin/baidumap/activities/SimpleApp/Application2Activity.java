package com.austin.baidumap.activities.SimpleApp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.austin.baidumap.BaiduLocationManager;
import com.austin.baidumap.R;
import com.austin.baidumap.utils.MapUtil;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

public class Application2Activity extends AppCompatActivity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private BaiduLocationManager mLocationManager;
    private MapStatus mapStatus;
    private boolean isFirstLoc = true;


    private float targetZoom = 7;
    private LatLng targetLatLng;
    private MapStatus.Builder mapStatus1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application2);

        mMapView = (TextureMapView) findViewById(R.id.mapView);

        mBaiduMap = mMapView.getMap();

        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mLocationManager = BaiduLocationManager.getInstance(Application2Activity.this);
                mLocationManager.setOnReceiveBaiduLocationListener(false, new BaiduLocationManager.OnReceiveBaiduLocationListener() {
                    @Override
                    public void onReceiveLocation(BDLocation location) {
                        MyLocationData myLocationData = new MyLocationData.Builder()
                                .accuracy(location.getRadius())
                                .direction(location.getDirection())
                                .latitude(location.getLatitude())
                                .longitude(location.getLongitude())
                                .build();

                        mBaiduMap.setMyLocationEnabled(true);

                        mBaiduMap.setMyLocationData(myLocationData);


                        if(isFirstLoc) {
                            isFirstLoc = false;

                            targetLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                            mapStatus = new MapStatus.Builder()
                                    .target(targetLatLng)
                                    .zoom(targetZoom)
                                    .build();

                            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus), 1000);
                        }

                    }
                });
                mLocationManager.onStart();

            }
        });

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationManager.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mLocationManager.onDestroy();
    }

    public void clearCircle(View view) {
        mBaiduMap.clear();
    }

    public void clearText(View view) {
        mBaiduMap.clear();
    }

    public void startZoom(View view) {
        MapUtil.animateOutAndIn(mBaiduMap, 2000, targetLatLng);
    }


}
