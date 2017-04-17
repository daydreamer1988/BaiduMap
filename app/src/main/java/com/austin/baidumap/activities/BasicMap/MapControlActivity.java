package com.austin.baidumap.activities.BasicMap;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.TextureMapView;

public class MapControlActivity extends AppCompatActivity {
    private Button mZoombutton;
    private EditText mZoomlevel;
    private Button mRotatebutton;
    private EditText mRotateangle;
    private Button mOverlookbutton;
    private EditText mOverlookangle;
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private Button mUpdateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_control);
        findById();
        mBaiduMap = mMapView.getMap();

        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                addButton();
            }
        });

    }

    private void addButton() {
        mUpdateButton = new Button(this);
        mUpdateButton.setText("更新状态");

        MapViewLayoutParams layoutParams = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .align(MapViewLayoutParams.ALIGN_CENTER_HORIZONTAL, MapViewLayoutParams.ALIGN_BOTTOM)
                .point(new Point(mMapView.getWidth()/2, mMapView.getHeight()))
                .build();

        mMapView.addView(mUpdateButton, layoutParams);

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zoomLevel = mZoomlevel.getText().toString();
                String rotateAngle = mRotateangle.getText().toString();
                String overlookAngle = mOverlookangle.getText().toString();
                try{
                    float v1 = Float.parseFloat(zoomLevel);
                    if(v1<3){
                        v1 = 3;
                        mZoomlevel.setText("3");
                    }else if(v1>21){
                        v1 = 21;
                        mZoomlevel.setText("21");
                    }


                    float v2 = Float.parseFloat(rotateAngle);
                    v2%=360;

                    float v3 = Float.parseFloat(overlookAngle);
                    if(v3>0){
                        mOverlookangle.setText("0");
                    }
                    if(v3<-45){
                        mOverlookangle.setText("-45");
                    }

                    MapStatus mapStatus = new MapStatus.Builder().zoom(v1).rotate(v2).overlook(v3).build();
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus), 1000);
                }catch (Exception ex){
                    Toast.makeText(MapControlActivity.this, "请输入正确的格式", Toast.LENGTH_SHORT).show();

                }

            }
        });

        mZoombutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zoomLevel = mZoomlevel.getText().toString();
                try {
                    float v1 = Float.parseFloat(zoomLevel);
                    if(v1<3){
                        Toast.makeText(MapControlActivity.this, "最小值为3", Toast.LENGTH_SHORT).show();
                        v1 = 3;
                        mZoomlevel.setText("3");
                    }else if(v1>21){
                        Toast.makeText(MapControlActivity.this, "最大值为21", Toast.LENGTH_SHORT).show();
                        v1 = 21;
                        mZoomlevel.setText("21");
                    }


                    MapStatusUpdate mapStatus = MapStatusUpdateFactory.zoomTo(v1);
                    mBaiduMap.animateMapStatus(mapStatus, 1000);

                }catch (Exception ex){
                    Toast.makeText(MapControlActivity.this, "请输入正确的格式", Toast.LENGTH_SHORT).show();
                    mZoomlevel.requestFocus();
                }
            }
        });

        mRotatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mRotateangle.getText().toString();
                try{
                    float v1 = Float.parseFloat(s);
                    v1%=360;
                    mRotateangle.setText(""+v1);
                    MapStatus status = new MapStatus.Builder().rotate(v1).build();
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(status),1000);
                }catch (Exception ex){
                    Toast.makeText(MapControlActivity.this, "请输入正确的格式", Toast.LENGTH_SHORT).show();
                    mRotateangle.requestFocus();
                }
            }
        });

        mOverlookbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mOverlookangle.getText().toString();
                try {
                    float v1 = Float.parseFloat(s);
                    if(v1>0){
                        Toast.makeText(MapControlActivity.this, "最大值为0度", Toast.LENGTH_SHORT).show();
                        mOverlookangle.setText("0");
                    }
                    if(v1<-45){
                        Toast.makeText(MapControlActivity.this, "最小值为-45度", Toast.LENGTH_SHORT).show();
                        mOverlookangle.setText("-45");
                    }

                    MapStatus mapStatus = new MapStatus.Builder().overlook(v1).build();
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus), 1000);
                }catch (Exception ex){
                    Toast.makeText(MapControlActivity.this, "请输入正确的格式", Toast.LENGTH_SHORT).show();
                    mOverlookangle.requestFocus();
                }
            }
        });
    }

    private void findById() {
        mZoombutton = (Button) findViewById(R.id.zoombutton);
        mZoomlevel = (EditText) findViewById(R.id.zoomlevel);
        mRotatebutton = (Button) findViewById(R.id.rotatebutton);
        mRotateangle = (EditText) findViewById(R.id.rotateangle);
        mOverlookbutton = (Button) findViewById(R.id.overlookbutton);
        mOverlookangle = (EditText) findViewById(R.id.overlookangle);
        mMapView = (TextureMapView) findViewById(R.id.mapView);

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
