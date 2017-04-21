package com.austin.baidumap.activities.BasicMap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.austin.baidumap.BaiduLocationManager;
import com.austin.baidumap.R;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;


/**
 * 多个页面的解决方法：在父类的setContentView()方法中
 *
 */
//    @Override
//    public void setContentView(@LayoutRes int layoutResID) {
//        super.setContentView(layoutResID);
//        View map = findViewById(R.id.mapView);
//        if(map instanceof MapView){
//            ((MapView) map).removeViewAt(1);
//            ((MapView) map).removeViewAt(2);
//        }else if(map instanceof TextureMapView){
//            ((TextureMapView) map).removeViewAt(1);
//            ((TextureMapView) map).removeViewAt(2);
//        }
//
//        View map1 = findViewById(R.id.mMapView);
//        if(map1 instanceof MapView){
//           ((MapView) map1).removeViewAt(1);
//           ((MapView) map1).removeViewAt(2);
//        }else if(map1 instanceof TextureMapView){
//            ((TextureMapView) map1).removeViewAt(1);
//            ((TextureMapView) map1).removeViewAt(2);
//        }
//    }


public class RemoveBaiduLogoActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private BaiduLocationManager instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_baidu_logo);

        mMapView = (TextureMapView) findViewById(R.id.mapView);

        mBaiduMap = mMapView.getMap();


        //取消logo
        mMapView.removeViewAt(1);

        //取消比例尺
        mMapView.removeViewAt(2);

        instance = BaiduLocationManager.getInstance(this);

        instance.setOnReceiveBaiduLocationListener(false, new BaiduLocationManager.OnReceiveBaiduLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if(location==null) return;

                mBaiduMap.setMyLocationEnabled(true);

                MyLocationData data = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        .direction(location.getDirection())
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build();

                mBaiduMap.setMyLocationData(data);

                MyLocationConfiguration configuration = new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.NORMAL,
                        true,
                        null
                );

                mBaiduMap.setMyLocationConfiguration(configuration);

                MapStatus status = new MapStatus.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(15)
                        .build();

                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(status));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        instance.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        instance.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance.onDestroy();
        mMapView.onDestroy();
    }
}
