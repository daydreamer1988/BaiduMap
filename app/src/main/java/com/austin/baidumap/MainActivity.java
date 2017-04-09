package com.austin.baidumap;

import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.austin.baidumap.utils.GpsUtil;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.TextureMapView;

public class MainActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private LocationClient clientclient;
    private BDLocationListener listener;
    private BaiduLocationManager mBaiduLocationManager;
    private CheckBox mCheckBox;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!GpsUtil.isGpsOpened(this))
            Toast.makeText(this, "gps off", Toast.LENGTH_SHORT).show();

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


        listenGpsState();


        //这种方法监听不到
       /* IntentFilter intentfilter = new IntentFilter(Intent.ACTION_PROVIDER_CHANGED);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle extras = intent.getExtras();
                Set<String> strings = extras.keySet();

                for (String key : strings) {
                    Log.e("tag","key:" + key + " value: " + extras.get(key));
                }

                String action = intent.getAction();
                if (action.equals(LocationManager.PROVIDERS_CHANGED_ACTION)){
                    Toast.makeText(context, "gps opened: " + GpsUtil.isGpsOpened(MainActivity.this), Toast.LENGTH_SHORT).show();
                }
            }
        }, intentfilter);*/


    }


    //只第一次起作用
    private ContentObserver gpsObserver = new ContentObserver(null){
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Looper.prepare();
            Toast.makeText(MainActivity.this, "gps opened ?" + GpsUtil.isGpsOpened(MainActivity.this), Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    };
    private void listenGpsState() {
        getContentResolver().registerContentObserver(Settings.Secure.getUriFor(Settings.System.LOCATION_PROVIDERS_ALLOWED), false, gpsObserver);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBaiduLocationManager!=null)
        mBaiduLocationManager.onDestroy();

        getContentResolver().unregisterContentObserver(gpsObserver);
    }

    public void startLocate(View view) {
        mBaiduLocationManager = MyApplication.getLocationManager();
        mBaiduLocationManager.setOnReceiveBaiduLocationListener(new BaiduLocationManager.OnReceiveBaiduLocationListener() {
            @Override
            public void onReceiveLocation(final BDLocation location) {
                mTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText(mBaiduLocationManager.showDefaultInfo(location, false));
                    }
                });
            }
        });
        mBaiduLocationManager.onStart();
    }

}
