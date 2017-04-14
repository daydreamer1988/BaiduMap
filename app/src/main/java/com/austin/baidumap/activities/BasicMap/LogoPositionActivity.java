package com.austin.baidumap.activities.BasicMap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.Toast;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.TextureMapView;

public class LogoPositionActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private LogoPosition logoPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_position);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        logoPosition = mMapView.getLogoPosition();
        Toast.makeText(this, "logoPosition:" + logoPosition.toString(), Toast.LENGTH_SHORT).show();

        mMapView.getMap().setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                if(y > mMapView.getHeight()/2){//下
                    if(x > 0 && x < mMapView.getWidth()/3){//左
                        logoPosition = LogoPosition.logoPostionleftBottom;
                    }else if(x > mMapView.getWidth()/3 && x < mMapView.getWidth()/3*2){//中单
                        logoPosition = LogoPosition.logoPostionCenterBottom;
                    }else if(x > mMapView.getWidth()/3*2){//右
                        logoPosition = LogoPosition.logoPostionRightBottom;
                    }
                }else{//上
                    if(x > 0 && x < mMapView.getWidth()/3){//左
                        logoPosition = LogoPosition.logoPostionleftTop;
                    }else if(x > mMapView.getWidth()/3 && x < mMapView.getWidth()/3*2){//中单
                        logoPosition = LogoPosition.logoPostionCenterTop;
                    }else if(x > mMapView.getWidth()/3*2){//右
                        logoPosition = LogoPosition.logoPostionRightTop;
                    }
                }
                mMapView.setLogoPosition(logoPosition);

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
