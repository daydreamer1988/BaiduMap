package com.austin.baidumap.activities.Overlays;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.austin.baidumap.BaiduLocationManager;
import com.austin.baidumap.R;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

public class CircleOverlayActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private BaiduLocationManager mLocationManager;
    private int strokeWidth = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_overlay);
        mMapView = (TextureMapView) findViewById(R.id.mapView);

        mBaiduMap = mMapView.getMap();

        mLocationManager = BaiduLocationManager.getInstance(this);

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

                MyLocationConfiguration configuration = new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.FOLLOWING,
                        true,
                        null
                );

                mBaiduMap.setMyLocationConfiguration(configuration);

                MapStatus mapStatus = new MapStatus.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(15)
                        .build();

                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));

            }
        });

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addCircle(latLng);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                addCircle(mapPoi.getPosition());

                return false;
            }
        });

    }

    private void addCircle(LatLng latLng) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng)
                .radius(500)//米
                .fillColor(getResources().getColor(R.color.colorAccent))
                .stroke(new Stroke(strokeWidth+=5, getResources().getColor(R.color.colorPrimary)));//像素

        mBaiduMap.addOverlay(circleOptions);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationManager.onStart();
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
}
