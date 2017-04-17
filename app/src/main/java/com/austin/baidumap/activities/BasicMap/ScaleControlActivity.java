package com.austin.baidumap.activities.BasicMap;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;

public class ScaleControlActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    boolean isShowing = true;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_control);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        textView2 = (TextView) findViewById(R.id.textView2);

        mMapView.getMap().setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                textView2.setText("比例尺 宽*高:" + mMapView.getScaleControlViewWidth() + "*" + mMapView.getScaleControlViewHeight()+"\n" +
                        "看原代码，返回的宽高是相同的");


                mMapView.getMap().setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
                    @Override
                    public void onTouch(MotionEvent motionEvent) {
                        int x = (int) motionEvent.getX();
                        int y = (int) motionEvent.getY();
                        mMapView.setScaleControlPosition(new Point(x, y));
                    }
                });
            }
        });

    }



    public void toggle(View view) {
        mMapView.showScaleControl(!isShowing);
        isShowing = !isShowing;
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
