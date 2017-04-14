package com.austin.baidumap.activities.BasicMap;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;

public class ZoomControlActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private boolean isShowing = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_control);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mMapView.getMap().setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMapView.getMap().setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
                    @Override
                    public void onTouch(MotionEvent motionEvent) {
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        mMapView.setZoomControlsPosition(new Point(x, y));

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

    public void toggle(View view) {
        mMapView.showZoomControls(!isShowing);
        isShowing = !isShowing;
    }
}
