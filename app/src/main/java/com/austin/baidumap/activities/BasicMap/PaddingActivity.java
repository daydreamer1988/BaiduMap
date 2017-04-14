package com.austin.baidumap.activities.BasicMap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.TextureMapView;

public class PaddingActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private int targetPadding = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padding);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mMapView.setPadding(0, 0, 0, targetPadding);
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
