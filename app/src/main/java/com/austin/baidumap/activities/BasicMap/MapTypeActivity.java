package com.austin.baidumap.activities.BasicMap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;

public class MapTypeActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private RadioGroup mRadioGroup;
    private CheckBox mCheckBox3;
    private CheckBox mCheckBox2;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_type);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mCheckBox3 = (CheckBox) findViewById(R.id.checkBox3);
        mCheckBox2 = (CheckBox) findViewById(R.id.checkBox2);
        mBaiduMap = mMapView.getMap();

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton1:
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                        getMapStatus();
                        break;
                    case R.id.radioButton2:
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                        getMapStatus();
                        break;
                    case R.id.radioButton3:
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
                        getMapStatus();
                        break;
                }
            }
        });

        mCheckBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mBaiduMap.setTrafficEnabled(isChecked);
            }
        });

        mCheckBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBaiduMap.setBaiduHeatMapEnabled(isChecked);
            }
        });

        getMapStatus();

    }

    private void getMapStatus() {
        int mapType = mBaiduMap.getMapType();
        if(mapType == BaiduMap.MAP_TYPE_NONE){
            Toast.makeText(this, "普通地图", Toast.LENGTH_SHORT).show();
        }else if(mapType == BaiduMap.MAP_TYPE_NORMAL){
            Toast.makeText(this, "正常地图", Toast.LENGTH_SHORT).show();
        }else if(mapType == BaiduMap.MAP_TYPE_SATELLITE){
            Toast.makeText(this, "卫星地图", Toast.LENGTH_SHORT).show();
        }
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

    public void isTrafficEnabled(View view) {
        boolean trafficEnabled = mBaiduMap.isTrafficEnabled();
        Toast.makeText(this, "isTrafficEnabled:"+trafficEnabled, Toast.LENGTH_SHORT).show();
    }
}
