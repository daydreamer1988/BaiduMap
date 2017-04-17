package com.austin.baidumap.activities.BasicMap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.TextureMapView;

public class IsShowPoiActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private CheckBox mCheckBox4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_show_poi);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mCheckBox4 = (CheckBox) findViewById(R.id.checkBox4);

        mCheckBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mMapView.getMap().showMapPoi(isChecked);
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
