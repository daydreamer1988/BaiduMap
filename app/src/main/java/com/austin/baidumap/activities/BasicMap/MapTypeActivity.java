package com.austin.baidumap.activities.BasicMap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;

public class MapTypeActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private RadioGroup mRadioGroup;
    private CheckBox mCheckBox3;
    private CheckBox mCheckBox2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_type);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mCheckBox3 = (CheckBox) findViewById(R.id.checkBox3);
        mCheckBox2 = (CheckBox) findViewById(R.id.checkBox2);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton1:
                        mMapView.getMap().setMapType(BaiduMap.MAP_TYPE_NORMAL);
                        break;
                    case R.id.radioButton2:
                        mMapView.getMap().setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                        break;
                }
            }
        });

        mCheckBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mMapView.getMap().setTrafficEnabled(isChecked);
            }
        });

        mCheckBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mMapView.getMap().setBaiduHeatMapEnabled(isChecked);
            }
        });
    }


}
