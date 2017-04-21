package com.austin.baidumap.activities.BasicMap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.austin.baidumap.R;
import com.austin.baidumap.utils.MapUtil;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

import java.util.List;

public class GetMarkerInBoundsActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_marker_in_bounds);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addMarker(latLng);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                addMarker(mapPoi.getPosition());

                return false;
            }
        });

    }

    private void addMarker(LatLng latLng) {
        Bundle extrainfo = new Bundle();
        index++;
        extrainfo.putString("key", ""+index);
        OverlayOptions options = new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_place))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker))
                .position(latLng)
                .period(1)
                .animateType(MarkerOptions.MarkerAnimateType.grow)
                .extraInfo(extrainfo);

        mBaiduMap.addOverlay(options);
    }

    public void getMarkersInBounds(View view) {
        LatLngBounds bounds = MapUtil.getScreenBounds(mMapView);
        List<Marker> markersInBounds = mBaiduMap.getMarkersInBounds(bounds);

        int size = markersInBounds == null ? 0 : markersInBounds.size();
        Toast.makeText(this, "当前屏幕有Marker"+size+"个", Toast.LENGTH_SHORT).show();
    }
}
