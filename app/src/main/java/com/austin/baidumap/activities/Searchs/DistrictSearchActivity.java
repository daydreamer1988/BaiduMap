package com.austin.baidumap.activities.Searchs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.austin.baidumap.BaiduLocationManager;
import com.austin.baidumap.R;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;

import java.util.ArrayList;
import java.util.List;

public class DistrictSearchActivity extends AppCompatActivity implements OnGetPoiSearchResultListener {
    private TextureMapView mMapView;
    private EditText mEditTextCity;
    private EditText mEditTextDistrict;
    private BaiduMap mBaiduMap;
    private BaiduLocationManager mBaiduLocationManager;
    private boolean isFirstLoc = true;
    private DistrictSearch districtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_search);

        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mEditTextCity = (EditText) findViewById(R.id.editTextCity);
        mEditTextDistrict = (EditText) findViewById(R.id.editTextDistrict);

        mBaiduMap = mMapView.getMap();

        mBaiduLocationManager = BaiduLocationManager.getInstance(this);


        mBaiduLocationManager.setOnReceiveBaiduLocationListener(false, new BaiduLocationManager.OnReceiveBaiduLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if(location == null || mMapView==null){
                    return;
                }

                MyLocationData myLocationData = new MyLocationData.Builder()
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build();
                mBaiduMap.setMyLocationData(myLocationData);

                mBaiduMap.setMyLocationEnabled(true);

                if(isFirstLoc){
                    isFirstLoc=false;
                    MapStatus mapStatus = new MapStatus.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))
                            .zoom(15)
                            .build();

                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
                }
            }
        });

        districtSearch = DistrictSearch.newInstance();
        districtSearch.setOnDistrictSearchListener(new OnGetDistricSearchResultListener() {
            @Override
            public void onGetDistrictResult(DistrictResult districtResult) {
                mBaiduMap.clear();
                if (districtResult == null || districtResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(DistrictSearchActivity.this, "未找到结果", Toast.LENGTH_SHORT).show();
                    return;
                }

                LatLngBounds.Builder boundBuilder = new LatLngBounds.Builder();
                List<LatLng> points = new ArrayList<LatLng>();
                PolylineOptions polylineOptions = new PolylineOptions();
                PolygonOptions polygonOptions = new PolygonOptions();

                List<List<LatLng>> polylines = districtResult.getPolylines();

                for (List<LatLng> polyline : polylines) {
                    points.addAll(polyline);
                    for (LatLng latLng : polyline) {
                        boundBuilder.include(latLng);
                    }
                }

                polylineOptions
                        .points(points)
                        .color(0xAA00FF00)
                        .width(10)
                        .dottedLine(true);

                polygonOptions
                        .points(points)
                        .fillColor(0xAAFFFF00)
                        .stroke(new Stroke(5, 0xAA00FF88));

                List<OverlayOptions> optionses = new ArrayList<OverlayOptions>();
                optionses.add(polygonOptions);
                optionses.add(polylineOptions);

                mBaiduMap.addOverlays(optionses);

                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngBounds(boundBuilder.build()));
            }
        });


    }

    public void startDistrictSearch(View view) {
        DistrictSearchOption option = new DistrictSearchOption();
        option.cityName(mEditTextCity.getText().toString())
                .districtName(mEditTextDistrict.getText().toString());
        districtSearch.searchDistrict(option);
    }


    @Override
    public void onGetPoiResult(PoiResult poiResult) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }


    @Override
    protected void onStart() {
        super.onStart();
        mBaiduLocationManager.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBaiduLocationManager.onStop();
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
        mBaiduLocationManager.onDestroy();
        districtSearch.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }


}
