package com.austin.baidumap.activities.BasicMap;

import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.TextureMapView;

public class CompassActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private CheckBox mCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mCheckbox = (CheckBox) findViewById(R.id.checkbox);

        mBaiduMap = mMapView.getMap();


        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Point compassPosition = mBaiduMap.getCompassPosition();
                Toast.makeText(CompassActivity.this, "CompassPosition:" + compassPosition.toString(), Toast.LENGTH_SHORT).show();
                addButton();
            }
        });

        //是否显示指南针
        mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBaiduMap.getUiSettings().setCompassEnabled(isChecked);
            }
        });
    }

    private void addButton() {
        Button button = new Button(this);
        button.setText("改变指南针位置，图标");
        MapViewLayoutParams layoutParams = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .point(new Point(mMapView.getWidth()/2, mMapView.getHeight()))
                .align(MapViewLayoutParams.ALIGN_CENTER_HORIZONTAL, MapViewLayoutParams.ALIGN_BOTTOM)
                .build();

        mMapView.addView(button, layoutParams);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaiduMap.setCompassIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                mBaiduMap.setCompassPosition(new Point(mMapView.getWidth()/2, mMapView.getHeight()/2));
                mMapView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Point compassPosition = mBaiduMap.getCompassPosition();
                        Toast.makeText(CompassActivity.this, "CompassPosition:" + compassPosition.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, 300);
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
