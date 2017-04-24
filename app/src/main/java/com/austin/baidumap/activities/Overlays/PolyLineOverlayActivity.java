package com.austin.baidumap.activities.Overlays;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.austin.baidumap.BaiduLocationManager;
import com.austin.baidumap.R;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PolyLineOverlayActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private BaiduLocationManager mLocationManager;

    private List<LatLng> mPoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poly_line_overlay);

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
                        .zoom(15)
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .build();

                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
            }
        });

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mPoints.add(latLng);
                addPoint(latLng);

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                mPoints.add(mapPoi.getPosition());
                addPoint(mapPoi.getPosition());
                return false;
            }
        });

    }

    private void addPoint(LatLng latLng) {
        DotOptions options = new DotOptions();
        options.center(latLng)
                .color(getResources().getColor(R.color.colorAccent))
                .radius(6);

        mBaiduMap.addOverlay(options);

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

    public void clearLine(View view) {
        mBaiduMap.clear();
    }

    public void drawLine(View view) {
        if(mPoints.size()==0){
            Toast.makeText(this, "请点击屏幕，添加点", Toast.LENGTH_SHORT).show();
            return;
        }
        PolylineOptions options = new PolylineOptions();
        options.width(10)
                .color(getResources().getColor(R.color.colorAccent))
                .points(mPoints);

        mBaiduMap.addOverlay(options);
        mPoints.clear();

    }
}
