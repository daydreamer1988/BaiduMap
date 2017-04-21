package com.austin.baidumap.activities.BasicMap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
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
 * 解决办法：在更新InfoWindow内容前，mBaiduMap.hideInfoWindow();
 *         并在更新后,mBaiduMap.showInfoWindow(infoWindow);
 *
 * 1.已经解决一开始显示InfoWindow就显示两层的问题
 * 2.但是状态改变后又变为两层。todo

 *
 *
 */
public class InfoWindowActivity extends AppCompatActivity {
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

        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                addMarker(latLng);
            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                infoWindow = getInfoWindow(marker);
                mBaiduMap.showInfoWindow(infoWindow);
                //解决一开始显示InfoWindow就显示两层
                //mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().overlook(0).build()));

                //更新数据
                mMapView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBaiduMap.hideInfoWindow();
                        tv.setText("fuck");
                        mBaiduMap.showInfoWindow(infoWindow);
                    }
                }, 1000);

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
        int height = bitmapDescriptor.getBitmap().getHeight();
        Toast.makeText(this, "marker_height:"+ height, Toast.LENGTH_SHORT).show();
        return height;
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
