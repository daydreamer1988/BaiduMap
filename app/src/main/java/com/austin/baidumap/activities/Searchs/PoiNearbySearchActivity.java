package com.austin.baidumap.activities.Searchs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.austin.baidumap.BaiduLocationManager;
import com.austin.baidumap.R;
import com.austin.baidumap.utils.MapUtil;
import com.austin.baidumap.utils.PoiDialogUtil;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;

public class PoiNearbySearchActivity extends AppCompatActivity implements OnGetPoiSearchResultListener {
    private TextureMapView mMapView;
    private EditText mEditText;

    private BaiduMap mBaiduMap;
    private BaiduLocationManager mBaiduLocationManager;
    private boolean isFirstLoc = true;
    private PoiSearch poiSearch;
    private LatLng latLng;
    private int pageIndex = 0;
    private BitmapDescriptor bitmapDescriptor;
    private MarkerOptions mMarkerOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_nearby_search);
        mMapView = (TextureMapView) findViewById(R.id.mapView);

        mEditText = (EditText) findViewById(R.id.editText);

        mBaiduMap = mMapView.getMap();
        mBaiduLocationManager = BaiduLocationManager.getInstance(this);

        mBaiduLocationManager.setOnReceiveBaiduLocationListener(false, new BaiduLocationManager.OnReceiveBaiduLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {

                MyLocationData myLocationData = new MyLocationData.Builder()
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build();

                mBaiduMap.setMyLocationEnabled(true);
                mBaiduMap.setMyLocationData(myLocationData);

                MyLocationConfiguration configuration = new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.NORMAL,
                        false,
                        null
                );
                mBaiduMap.setMyLocationConfiguration(configuration);

                if(isFirstLoc){
                    isFirstLoc = false;
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    MapStatus mapStatus = new MapStatus.Builder()
                            .target(latLng)
                            .zoom(15)
                            .build();
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));

                }
            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle extraInfo = marker.getExtraInfo();
                String uid = extraInfo.getString("uid");
                poiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(uid));
                return false;
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pageIndex=0;
            }
        });

        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);

        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.marker);
        //设置默认MarkerOption
        mMarkerOption = new MarkerOptions();
        mMarkerOption.icon(bitmapDescriptor).animateType(MarkerOptions.MarkerAnimateType.grow);
    }

    public void startNearbySearch(View view) {
        String keyword = mEditText.getText().toString();

        if (keyword==null || keyword.length()==0) {
            Toast.makeText(this, "请输入KEYWORD", Toast.LENGTH_SHORT).show();
        }else{
            PoiNearbySearchOption option = new PoiNearbySearchOption();
            option.keyword(keyword)
                    .location(latLng)
                    .pageCapacity(20)
                    .pageNum(pageIndex)
                    .sortType(PoiSortType.distance_from_near_to_far)
                    .radius(1000);
            poiSearch.searchNearby(option);
        }
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        mBaiduMap.clear();
        PoiDialogUtil.getInstance(this).showPoiList(poiResult, new PoiDialogUtil.OnPoiItemSelected() {
            @Override
            public void onPoiItemClick(PoiInfo poiInfo) {
                mBaiduMap.clear();
                Bundle bundle = new Bundle();
                bundle.putString("uid", poiInfo.uid);
                mMarkerOption.position(poiInfo.location).extraInfo(bundle);
                mBaiduMap.addOverlay(mMarkerOption);
                MapUtil.animateOutAndIn(mBaiduMap, 2000, poiInfo.location);
            }

            @Override
            public void onLastPageClicked(int totalPageNum, int currentPageNum) {
                pageIndex--;
                startNearbySearch(null);

            }

            @Override
            public void onNextPageClicked(int totalPageNum, int currentPageNum) {
                pageIndex++;
                startNearbySearch(null);
            }
        });
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        PoiDialogUtil.getInstance(this).showPoiDetial(poiDetailResult);
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
        poiSearch.destroy();
        bitmapDescriptor.recycle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }


}
