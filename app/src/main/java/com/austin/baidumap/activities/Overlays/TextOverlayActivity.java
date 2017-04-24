package com.austin.baidumap.activities.Overlays;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;

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
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

public class TextOverlayActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private BaiduLocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_overlay);

        mMapView = (TextureMapView) findViewById(R.id.mapView);

        mBaiduMap = mMapView.getMap();

        mLocationManager = BaiduLocationManager.getInstance(this);

        mLocationManager.setOnReceiveBaiduLocationListener(false, new BaiduLocationManager.OnReceiveBaiduLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                MyLocationData myLocationData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .direction(location.getDirection())
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
                addText(latLng);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                addText(mapPoi.getPosition());
                return false;
            }
        });



    }

    private void addText(LatLng latLng) {

        DotOptions dotOptions = new DotOptions();
        dotOptions.radius(5)
                .color(getResources().getColor(R.color.colorPrimaryDark))
                .center(latLng);
        mBaiduMap.addOverlay(dotOptions);



        TextOptions options = new TextOptions();
        options.text(latLng.toString());
        options.fontColor(Color.parseColor("#77ff0000"))
                .fontSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, Resources.getSystem().getDisplayMetrics()))
                .bgColor(Color.parseColor("#77ffff00"))
                //.align(TextOptions.ALIGN_CENTER_HORIZONTAL, TextOptions.ALIGN_CENTER_VERTICAL)
                .align(TextOptions.ALIGN_LEFT, TextOptions.ALIGN_BOTTOM)

                .position(latLng)
                .rotate(30);

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

    public void clearText(View view) {
        mBaiduMap.clear();
    }
}
