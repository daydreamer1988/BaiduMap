package com.austin.baidumap.activities.BasicMap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.TextureMapView;


/**
 * 两个方法：
 * （一）mMapView.setPadding(left, top, right, bottom);
 * 发现设置padding后，有时不会立即更新，当mapview 状态发生变化（双击，移动等）后，padding生效。
 * 解决方法，addView() 随意加一个布局，使其调用MapView.onLayout()方法重绘。
 *
 *
 * （二）推荐使用，mBaiduMap.setPadding(left, top, right, bottom);
 * 该方法不会出现像第一个方法的问题，推荐使用。
 * 注意：只有在 OnMapLoadedCallback.onMapLoaded() 之后设置才生效
 *
 */
public class PaddingActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private int targetPadding = 200;
    private CheckBox mCheckbox;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padding);

        mCheckbox = (CheckBox) findViewById(R.id.checkbox);

        mMapView = (TextureMapView) findViewById(R.id.mapView);

        mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
//                    mMapView.setPadding(0, 0, 0, targetPadding);
//                    addView(mMapView);

                    mMapView.getMap().setViewPadding(0, 0, 0, targetPadding);
                }else{
//                    mMapView.setPadding(0, 0, 0, 0);
//                    mMapView.removeView(tv);

                    mMapView.getMap().setViewPadding(0, 0, 0, 0);
                }
            }
        });
    }
    private void addView(TextureMapView mMapView) {
        tv = new TextView(this);
        mMapView.addView(tv);
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
