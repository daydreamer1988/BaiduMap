package com.austin.baidumap.activities.BasicMap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.austin.baidumap.BaiduLocationManager;
import com.austin.baidumap.MyApplication;
import com.austin.baidumap.R;
import com.austin.baidumap.utils.SensorUtil;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;


public class MyLocationConfigurationActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private RadioGroup mRadioGroup;
    private TextView mUpdating;
    private BaiduLocationManager mLocationManager;
    private boolean isFirstLoc = true;
    private int accuracyCircleFillColor = 0x77BE3322;
    private int accuracyCircleStrokeColor = 0xFFBE3322;
    private com.baidu.mapapi.map.BitmapDescriptor mCurrentBitmapDescriptor;

    private double mRealtimeDirection = 0;
    private float mRealtimeRadius = 0;
    private double mRealtimeLatitude = 0;
    private double mRealtimeLongitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location_configuration);
        mUpdating = (TextView) findViewById(R.id.loading);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        mBaiduMap = mMapView.getMap();

        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);

        startLocation();

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.loc_default:
                        mCurrentBitmapDescriptor = null;
                        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                                mBaiduMap.getLocationConfiguration().locationMode,
                                true,
                                mCurrentBitmapDescriptor
                        ));
                        break;

                    case R.id.loc_custom:
                        mCurrentBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
                        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                                mBaiduMap.getLocationConfiguration().locationMode,
                                true,
                                mCurrentBitmapDescriptor,
                                accuracyCircleFillColor,
                                accuracyCircleStrokeColor
                        ));
                        break;
                }
            }
        });


    }

    private void startLocation() {
        mLocationManager = MyApplication.getLocationManager();
        mBaiduMap.setMyLocationEnabled(true);
        mLocationManager.setOnReceiveBaiduLocationListener(true, new BaiduLocationManager.OnReceiveBaiduLocationListener(){
            @Override
            public void onReceiveLocation(BDLocation location) {
                mRealtimeRadius = location.getRadius();
                mRealtimeLatitude = location.getLatitude();
                mRealtimeLongitude = location.getLongitude();
                MyLocationData myLocationData = new MyLocationData.Builder()
                        .accuracy(mRealtimeRadius)
                        .latitude(mRealtimeLatitude)
                        .longitude(mRealtimeLongitude)
                        //.direction(location.getDirection())
                        .direction((float) mRealtimeDirection)
                        .build();

                mBaiduMap.setMyLocationData(myLocationData);

                if(isFirstLoc){
                    isFirstLoc = false;

                    //移动到定位点
                    MapStatus mapStatus = new MapStatus.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))
                            .zoom(18.0f)
                            .build();
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));

                    //设置定位配置
                    mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                            MyLocationConfiguration.LocationMode.NORMAL,
                            true,
                            mCurrentBitmapDescriptor
                    ));

                    //点位图消失
                    Animation animation = new AlphaAnimation(1, 0);
                    animation.setDuration(500);
                    animation.setFillAfter(true);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    mUpdating.startAnimation(animation);

                }

            }
        });

        mLocationManager.onStart();
    }



    public void switchLocMode(View view) {
        Button button = (Button) view;
        MyLocationConfiguration locationConfiguration = mBaiduMap.getLocationConfiguration();



        switch(locationConfiguration.locationMode){
            case NORMAL:
                button.setText("跟随模式");
                locationConfiguration = new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.FOLLOWING,
                        true,
                        mCurrentBitmapDescriptor
                        );
                break;
            case FOLLOWING:
                button.setText("罗盘模式");
                locationConfiguration = new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.COMPASS,
                        true,
                        mCurrentBitmapDescriptor
                );
                break;

            case COMPASS:
                button.setText("普通模式");
                locationConfiguration = new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.NORMAL,
                        true,
                        mCurrentBitmapDescriptor
                );
                break;

        }

        mBaiduMap.setMyLocationConfiguration(locationConfiguration);

    }



    @Override
    protected void onStop() {
        super.onStop();
        mLocationManager.onStop();
        SensorUtil.unRegisterSensor();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationManager.onStart();
        SensorUtil.registerSensor(this, new SensorUtil.OnSensorChangedListener() {
            @Override
            public void onSensorChanged(double direction) {
                mRealtimeDirection = direction;
                MyLocationData myLocationData = new MyLocationData.Builder()
                        .direction((float) mRealtimeDirection)
                        .latitude(mRealtimeLatitude)
                        .longitude(mRealtimeLongitude)
                        .accuracy(mRealtimeRadius)
                        .build();

                mBaiduMap.setMyLocationData(myLocationData);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mLocationManager.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
    }


}
