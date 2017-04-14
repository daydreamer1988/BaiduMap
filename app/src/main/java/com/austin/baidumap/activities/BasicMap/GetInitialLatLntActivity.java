package com.austin.baidumap.activities.BasicMap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

public class GetInitialLatLntActivity extends AppCompatActivity {
    private TextureMapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_initial_lat_lnt);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        BaiduMap map = mMapView.getMap();
        MapStatus mapStatus = map.getMapStatus();
        LatLng target = mapStatus.target;
        Toast.makeText(this, ""+target.toString(), Toast.LENGTH_SHORT).show();
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
