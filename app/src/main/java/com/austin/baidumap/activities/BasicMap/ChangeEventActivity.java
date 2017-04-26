package com.austin.baidumap.activities.BasicMap;

import android.app.Activity;
import android.os.Bundle;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

public class ChangeEventActivity extends Activity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_event);

        mMapView = (TextureMapView) findViewById(R.id.mapView);

        mBaiduMap = mMapView.getMap();

        mBaiduMap.setOnMapDoubleClickListener(new BaiduMap.OnMapDoubleClickListener() {
            @Override
            public void onMapDoubleClick(LatLng latLng) {
                mMapView.post(new Runnable() {
                    @Override
                    public void run() {
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomBy(-1));
                    }
                });

            }
        });

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

}
