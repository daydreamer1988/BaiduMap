package com.austin.baidumap.activities.Overlays;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.austin.baidumap.BaiduLocationManager;
import com.austin.baidumap.R;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.ArcOptions;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class ArcOverlayActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private BaiduLocationManager mLocationManager;
    private int index = 0;
    private List<LatLng> points = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc_overlay);

        mMapView = (TextureMapView) findViewById(R.id.mapView);

        mBaiduMap = mMapView.getMap();

        mLocationManager = BaiduLocationManager.getInstance(this);

        mLocationManager.setOnReceiveBaiduLocationListener(false, new BaiduLocationManager.OnReceiveBaiduLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if(location==null) return;


                mBaiduMap.setMyLocationEnabled(true);

                MyLocationData myLocationData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .direction(location.getDirection())
                        .build();

                mBaiduMap.setMyLocationData(myLocationData);

                MyLocationConfiguration mylocationConfig = new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.FOLLOWING,
                        true,
                        null
                );

                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.marker);
                bitmapDescriptor.getBitmap();

                mBaiduMap.setMyLocationConfiguration(mylocationConfig);

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
                if(points.size()==3){
                    points.clear();
                }
                points.add(points.size(), latLng);

                if(points.size()==3) {
                    addArc(latLng);
                }
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                if(points.size()==3){
                    points.clear();
                }
                points.add(points.size(), mapPoi.getPosition());

                if(points.size()==3) {
                    addArc(mapPoi.getPosition());
                }
                return false;
            }
        });


    }

    private void addArc(LatLng latLng) {
        Bundle bundle = new Bundle();
        bundle.putInt("index", ++index);

        ArcOptions options = new ArcOptions();
        options.color(getResources().getColor(R.color.colorAccent))
                .extraInfo(bundle)
                .points(points.get(0), points.get(1), points.get(2))
                .width(10);

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

    public void clearArc(View view) {
        mBaiduMap.clear();
    }
}
