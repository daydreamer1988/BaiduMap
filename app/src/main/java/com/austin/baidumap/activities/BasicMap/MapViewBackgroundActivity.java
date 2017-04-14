package com.austin.baidumap.activities.BasicMap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.TextureMapView;

public class MapViewBackgroundActivity extends AppCompatActivity {
    private TextureMapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view_background);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mMapView.setBackgroundColor(Color.parseColor("#00afec"));
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
