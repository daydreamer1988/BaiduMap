package com.austin.baidumap.activities.Overlays;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

public class DotOverlayActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dot_overlay);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addDot(latLng);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                addDot(mapPoi.getPosition());
                return false;
            }
        });

    }

    private void addDot(LatLng latLng) {

        Bundle bundle = new Bundle();
        bundle.putInt("index", ++index);

        DotOptions options = new DotOptions()
                .center(latLng)
                .color(getResources().getColor(R.color.colorAccent))
                .radius(20)
                .extraInfo(bundle);

        Overlay overlay = mBaiduMap.addOverlay(options);
        Bundle extraInfo = overlay.getExtraInfo();
        int index = extraInfo.getInt("index");
        Toast.makeText(this, "index:"+index, Toast.LENGTH_SHORT).show();

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
    }

    public void clearDot(View view) {
        mBaiduMap.clear();
    }
}
