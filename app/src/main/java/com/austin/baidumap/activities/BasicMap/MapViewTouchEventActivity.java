package com.austin.baidumap.activities.BasicMap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import javax.microedition.khronos.opengles.GL10;

public class MapViewTouchEventActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private TextView mTextView;
    private BaiduMap mBaiduMap;

    private String touchData = "";
    private BitmapDescriptor bitmapDescripto;
    private Marker mMarker;
    private BaiduMap.OnMarkerClickListener onMarkerClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view_touch_event);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mTextView = (TextView) findViewById(R.id.textView);

        mBaiduMap = mMapView.getMap();

        bitmapDescripto = BitmapDescriptorFactory.fromResource(R.mipmap.marker);

        //触摸事件
        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                touchData = "BaiduMap.setOnMapTouchListener()\nX:" + motionEvent.getX() + " Y:" + motionEvent.getY()+"\n";
            }
        });


        //百度地图在每一帧绘制时的回调接口，该接口在绘制线程中调用
        mBaiduMap.setOnMapDrawFrameCallback(new BaiduMap.OnMapDrawFrameCallback() {
            @Override
            public void onMapDrawFrame(GL10 gl10, MapStatus mapStatus) {

            }

            @Override
            public void onMapDrawFrame(MapStatus mapStatus) {
                Log.e("TAG", mapStatus.target.toString());
            }
        });

        //点击事件
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mTextView.setText(touchData +
                        "点击地图 " +
                        "经纬度\n" +
                        "lat " + latLng.latitude + " lng:" + latLng.longitude);

                addMarker(latLng);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                mTextView.setText(touchData+
                        "点击地图POI \n" +
                        "经纬度\n" +
                        "lat " + mapPoi.getPosition().latitude + " lng:" + mapPoi.getPosition().longitude +
                        "\nPoi名称:"+mapPoi.getName());

                addMarker(mapPoi.getPosition());
                return false;
            }
        });

        //长按事件
        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mTextView.setText(touchData+
                        "长按地图 \n" +
                        "经纬度\n" +
                        "lat " + latLng.latitude + " lng:" + latLng.longitude);

                addMarker(latLng);
            }
        });

        //双击事件
        mBaiduMap.setOnMapDoubleClickListener(new BaiduMap.OnMapDoubleClickListener() {
            @Override
            public void onMapDoubleClick(LatLng latLng) {
                mTextView.setText(touchData+
                        "双击地图 \n" +
                        "经纬度\n" +
                        "lat " + latLng.latitude + " lng:" + latLng.longitude);
                addMarker(latLng);
            }
        });

        //地图加载成功事件
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Toast.makeText(MapViewTouchEventActivity.this, "Map Loaded", Toast.LENGTH_SHORT).show();
            }
        });


        //地图渲染成功事件
        //MapStatus状态改变
        //addOverlay后会回调
        mBaiduMap.setOnMapRenderCallbadk(new BaiduMap.OnMapRenderCallback() {
            @Override
            public void onMapRenderFinished() {
                Toast.makeText(MapViewTouchEventActivity.this, "Map Render", Toast.LENGTH_SHORT).show();
            }
        });


        //Marker点击事件
        onMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MapViewTouchEventActivity.this, "marker clicked", Toast.LENGTH_SHORT).show();
                mBaiduMap.clear();
                return false;
            }
        };
        mBaiduMap.setOnMarkerClickListener(onMarkerClickListener);

        //Marker拖动事件
        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(MapViewTouchEventActivity.this, "marker drag end", Toast.LENGTH_SHORT).show();
                LatLng latLng = marker.getPosition();
                mTextView.setText(touchData+
                        "拖拽Marker \n" +
                        "经纬度\n" +
                        "lat " + latLng.latitude + " lng:" + latLng.longitude);
            }

            @Override
            public void onMarkerDragStart(Marker marker) {

            }
        });
        

    }

    private void addMarker(LatLng latLng) {
        if(mMarker!=null){
            mMarker.remove();
        }
        MarkerOptions option = new MarkerOptions();
        option.position(latLng).period(1).animateType(MarkerOptions.MarkerAnimateType.grow).icon(bitmapDescripto).draggable(true);
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

    public void removeMarkerClick(View view) {
        mBaiduMap.removeMarkerClickListener(onMarkerClickListener);
    }
}
