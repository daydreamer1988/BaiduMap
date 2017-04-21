package com.austin.baidumap.activities.BasicMap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TextView;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import java.util.UUID;


/**
 * InfoWindow 在状态改变的时候显示一层，状态不改变的情况下，感觉是两个图片叠在了一起（两层）
 * 解决办法：
 *         在状态更新时，让InfoWindow消失，状态结束后再显示。
 *
 *         BUG:要想布局内容变化，布局更新的话，需要更改状态，即：mBaiduMap.setOnMapStatusChangeListener(),mBaiduMap.hideInfoWindow()隐藏后，重新再显示 mBaiduMap.showInfoWindow(infoWindow);
 *
 *         BUG:如果修改overlook,会显示指南针
 *
 *         BUG:状态不更新的情况下，第二次点击Marker会两层重叠。
 *
 */
public class InfoWindowActivity2 extends AppCompatActivity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private Marker mMarker;
    private int index = 1;
    private TextView tv;
    private InfoWindow infoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_window);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mBaiduMap.clear();
                //防止状态改变结束后再次显示
                infoWindow = null;
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                mBaiduMap.clear();
                //防止状态改变结束后再次显示
                infoWindow = null;
                return false;
            }
        });

        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                addMarker(latLng);
            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                infoWindow = getInfoWindow(marker);
                mBaiduMap.showInfoWindow(infoWindow);
                //解决一开始就出现两层的问题
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().overlook(0).build()));

                //更新数据
                mMapView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText("fuck");
                        mBaiduMap.showInfoWindow(infoWindow);
                    }
                }, 1000);
                return false;
            }
        });

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                    mBaiduMap.hideInfoWindow();
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                mBaiduMap.showInfoWindow(infoWindow);

            }
        });

    }

    private InfoWindow getInfoWindow(Marker marker) {
        Bundle extraInfo = marker.getExtraInfo();
        String value = extraInfo.getString("key");
        String uuid = extraInfo.getString("uuid");
        tv = new TextView(this);
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundResource(R.mipmap.location_tips);
        tv.setText(value+":"+uuid);
        InfoWindow infoWindow = new InfoWindow(tv, marker.getPosition(),-getResourcesHeight());
        return infoWindow;
    }

    private int getResourcesHeight() {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.marker);
        return bitmapDescriptor.getBitmap().getHeight();

    }

    private void dismissInfoWindow() {
    }

    private void addMarker(LatLng latLng) {

        Bundle bundle = new Bundle();
        bundle.putString("key", ""+index++);
        bundle.putString("uuid", UUID.randomUUID().toString());
        MarkerOptions option = new MarkerOptions()
                .animateType(MarkerOptions.MarkerAnimateType.grow)
                .extraInfo(bundle)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker))
                .position(latLng);
        mMarker = (Marker) mBaiduMap.addOverlay(option);
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
