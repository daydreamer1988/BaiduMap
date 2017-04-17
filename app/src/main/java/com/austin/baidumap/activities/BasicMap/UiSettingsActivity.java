
package com.austin.baidumap.activities.BasicMap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.map.UiSettings;

public class UiSettingsActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private CheckBox mZoom;
    private CheckBox mScroll;
    private CheckBox mRotate;
    private CheckBox mOverlook;
    private CheckBox mCompass;
    private CheckBox mAllGesture;
    private UiSettings uiSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_settings);
        findById();

        uiSettings = mMapView.getMap().getUiSettings();

        mZoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                uiSettings.setZoomGesturesEnabled(isChecked);
            }
        });

        mScroll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                uiSettings.setScrollGesturesEnabled(isChecked);
            }
        });

        mRotate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                uiSettings.setRotateGesturesEnabled(isChecked);
            }
        });

        mOverlook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                uiSettings.setOverlookingGesturesEnabled(isChecked);
            }
        });

        mCompass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                uiSettings.setCompassEnabled(isChecked);
            }
        });

        mAllGesture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                mZoom.setChecked(!isChecked);
                mScroll.setChecked(!isChecked);
                mRotate.setChecked(!isChecked);
                mOverlook.setChecked(!isChecked);
                uiSettings.setAllGesturesEnabled(!isChecked);
            }
        });


    }

    private void findById() {
        mZoom = (CheckBox) findViewById(R.id.zoom);
        mScroll = (CheckBox) findViewById(R.id.scroll);
        mRotate = (CheckBox) findViewById(R.id.rotate);
        mOverlook = (CheckBox) findViewById(R.id.overlook);
        mCompass = (CheckBox) findViewById(R.id.compass);
        mAllGesture = (CheckBox) findViewById(R.id.allGesture);
        mMapView = (TextureMapView) findViewById(R.id.bmapView);
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

    public void getCurrentUiSetting(View view) {
        boolean zoomGesturesEnabled = uiSettings.isZoomGesturesEnabled();
        boolean scrollGesturesEnabled = uiSettings.isScrollGesturesEnabled();
        boolean rotateGesturesEnabled = uiSettings.isRotateGesturesEnabled();
        boolean overlookingGesturesEnabled = uiSettings.isOverlookingGesturesEnabled();
        boolean compassEnabled = uiSettings.isCompassEnabled();
        Toast.makeText(this, " 缩放："+zoomGesturesEnabled+
                "\n 平移："+scrollGesturesEnabled+
                "\n 旋转："+rotateGesturesEnabled+
                "\n 俯视："+overlookingGesturesEnabled+
                "\n 指南针是否开启："+compassEnabled,
                Toast.LENGTH_SHORT).show();
    }
}
