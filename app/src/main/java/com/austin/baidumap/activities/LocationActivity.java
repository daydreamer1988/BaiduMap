package com.austin.baidumap.activities;

import android.database.ContentObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.austin.baidumap.BaiduLocationManager;
import com.austin.baidumap.MyApplication;
import com.austin.baidumap.R;
import com.austin.baidumap.utils.GpsUtil;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;

public class LocationActivity extends AppCompatActivity {

    private LocationClient clientclient;
    private BDLocationListener listener;
    private BaiduLocationManager mBaiduLocationManager;
    private CheckBox mCheckBox;
    private TextView mTextView;
    private ContentObserver contentObserver;
    private BDNotifyListener notifyListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mTextView = (TextView) findViewById(R.id.textView);
        mCheckBox = (CheckBox) findViewById(R.id.checkBox);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mBaiduLocationManager.onStop();
                    mTextView.setText("定位暂停");
                }else{
                    mBaiduLocationManager.onStart();
                    mTextView.setText("定位开始");
                }
            }
        });


        if (!GpsUtil.isGpsOpened(this))
            Toast.makeText(this, "gps off", Toast.LENGTH_SHORT).show();


        processGpsListen();

    }

    private void processGpsListen() {
        /**
         * 方法一 回调在主线程
         */
        GpsUtil.registerGpsReceiver(this, new GpsUtil.OnRegistGpsStateListener() {
            @Override
            public void onReceiveGspState(boolean isGpsOpened) {
                Toast.makeText(LocationActivity.this, "gps opened: " + isGpsOpened, Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 方法二，回调在子线程中，所以更新UI有些麻烦， 并且在onDestory时需要将返回的ContentObserver注销掉。
         *       当然也可以在GpsUtil中通过Handler切换到主线程。
         */
//        contentObserver = GpsUtil.registerGpsObserver(this, new GpsUtil.OnRegistGpsStateListener() {
//            @Override
//            public void onReceiveGspState(final boolean isGpsOpened) {
//                mTextView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(MainActivity.this, "gps opened ?" + isGpsOpened, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mBaiduLocationManager!=null) {
            //stop location
            mBaiduLocationManager.onDestroy();
            //stop notify
            mBaiduLocationManager.unregistNotifyListener();
        }
        //stop gps listener
//      GpsUtil.unregisterContentObserver(this,contentObserver);
    }

    //点击开始定位
    public void startLocate(View view) {

        mBaiduLocationManager = MyApplication.getLocationManager();

        mBaiduLocationManager.setOnReceiveBaiduLocationListener(true, new BaiduLocationManager.OnReceiveBaiduLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                mTextView.setText(mBaiduLocationManager.showDefaultInfo(location, false));
                mBaiduLocationManager.registNotifyListener(
                        location.getLatitude(),
                        location.getLongitude(),
                        3000,
                        location.getCoorType(),
                        true,
                        new BaiduLocationManager.OnNotifyListener() {
                            @Override
                            public void onNotify(BDLocation bdLocation, float distance) {
                                Toast.makeText(LocationActivity.this, "到达指定地点", Toast.LENGTH_SHORT).show();
                            }
                        });

                location.getGpsAccuracyStatus();
            }
        });

        mBaiduLocationManager.onStart();
    }

}
