package com.austin.baidumap.activities.BasicMap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.TextureMapView;

public class ZoomLevelActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_level);
        mMapView = (TextureMapView) findViewById(R.id.mapView);

        mBaiduMap = mMapView.getMap();

        float maxZoomLevel = mBaiduMap.getMaxZoomLevel();
        float minZoomLevel = mBaiduMap.getMinZoomLevel();

        Toast.makeText(this, "初始maxZoomLevel:"+maxZoomLevel + " minZoomLevel:" + minZoomLevel, Toast.LENGTH_SHORT).show();

        mBaiduMap.setMaxAndMinZoomLevel(13, 11);

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                Toast.makeText(ZoomLevelActivity.this, "当前zoom值："+mapStatus.zoom, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
