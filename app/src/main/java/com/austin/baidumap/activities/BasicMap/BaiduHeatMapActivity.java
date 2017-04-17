package com.austin.baidumap.activities.BasicMap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;

public class BaiduHeatMapActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private SwitchCompat mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_heat_map);
        mSwitch = (SwitchCompat) findViewById(R.id.switch1);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBaiduMap.setBaiduHeatMapEnabled(isChecked);
                isEnabled(buttonView);
            }
        });

    }

    public void isEnabled(View view) {
        boolean baiduHeatMapEnabled = mBaiduMap.isBaiduHeatMapEnabled();
        Toast.makeText(BaiduHeatMapActivity.this, "BaiduHeatMapEnabled:"+baiduHeatMapEnabled, Toast.LENGTH_SHORT).show();

    }

    public void isSupported(View view) {
        boolean supportBaiduHeatMap = mBaiduMap.isSupportBaiduHeatMap();
        Toast.makeText(BaiduHeatMapActivity.this, "BaiduHeatMapSupported:"+supportBaiduHeatMap, Toast.LENGTH_SHORT).show();

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
