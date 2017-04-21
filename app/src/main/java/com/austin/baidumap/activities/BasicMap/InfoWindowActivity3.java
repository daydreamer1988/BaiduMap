package com.austin.baidumap.activities.BasicMap;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import java.util.UUID;


/**
 * InfoWindow 在状态改变的时候显示一层，状态不改变的情况下，感觉是两个图片叠在了一起（两层）
 *
 *
 */
public class InfoWindowActivity3 extends AppCompatActivity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private Marker mMarker;
    private int index = 1;
    private TextView tv;
    private InfoWindow infoWindow;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private Runnable action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_window);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();

        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                addMarker(latLng);
            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showInfoWindow(marker);
                return false;
            }
        });

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mBaiduMap.clear();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                mBaiduMap.clear();
                return false;
            }
        });
    }

    private void showInfoWindow(Marker marker) {
        //先取消前一次的runnable,不然导致infowindow切换问题
        handler.removeCallbacks(action);

        final Marker localMarker = marker;
        infoWindow = getInfoWindow(marker);
        mBaiduMap.showInfoWindow(infoWindow);

        action = new Runnable() {
            @Override
            public void run() {
                //更新内容
                Bundle extraInfo = localMarker.getExtraInfo();
                extraInfo.putString("key", "fuck");
                infoWindow = getInfoWindow(localMarker);
                mBaiduMap.showInfoWindow(infoWindow);
            }
        };
        handler.postDelayed(action, 2000);
    }

    private InfoWindow getInfoWindow(Marker marker) {
        Bundle extraInfo = marker.getExtraInfo();
        String value = extraInfo.getString("key");
        String uuid = extraInfo.getString("uuid");
        tv = new TextView(this);
        //必须设置，不然显示infowindow异常崩溃
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundResource(R.mipmap.location_tips);
        tv.setText(value+":"+uuid);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(tv);
        InfoWindow infoWindow = new InfoWindow(bitmapDescriptor, marker.getPosition(), -getResourcesHeight(), new InfoWindow.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick() {
                Toast.makeText(InfoWindowActivity3.this, "infowindow clicked", Toast.LENGTH_SHORT).show();
            }
        });
        return infoWindow;
    }

    private int getResourcesHeight() {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.marker);
        return bitmapDescriptor.getBitmap().getHeight();
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
