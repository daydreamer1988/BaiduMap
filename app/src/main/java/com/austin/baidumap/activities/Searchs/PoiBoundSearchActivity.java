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
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

public class PoiBoundSearchActivity extends AppCompatActivity implements OnGetPoiSearchResultListener {
    private TextureMapView mMapView;
    private EditText mEditText;
    private BaiduMap mBaiduMap;
    private BaiduLocationManager mBaiduLocationManager;
    private boolean isFirstLoc = true;
    private PoiSearch poiSearch;
    private int pageIndex = 0;
    private BitmapDescriptor bitmapDescriptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_bound_search);
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

                mBaiduMap.setMyLocationData(myLocationData);

                mBaiduMap.setMyLocationEnabled(true);

                if(isFirstLoc){
                    isFirstLoc = false;
                    MapStatus mapStatus = new MapStatus.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))
                            .zoom(15)
                            .build();

                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
                }
            }
        });

        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);

        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.marker);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pageIndex = 0;
            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle bundle = marker.getExtraInfo();
                if(bundle!=null){
                    String uid = bundle.getString("uid");
                    poiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(uid));
                }
                return false;
            }
        });

    }

    public void startBoundSearch(View view) {
        String keyword = mEditText.getText().toString();

        if(keyword==null || keyword.length()==0){
            Toast.makeText(this, "请输入keyword", Toast.LENGTH_SHORT).show();
            return;
        }else{

            PoiBoundSearchOption option = new PoiBoundSearchOption();
            option.keyword(keyword)
                    .bound(mBaiduMap.getMapStatus().bound)
                    .pageCapacity(20)
                    .pageNum(pageIndex);
            poiSearch.searchInBound(option);
        }
    }


    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        PoiDialogUtil.getInstance(this).showPoiList(poiResult, new PoiDialogUtil.OnPoiItemSelected() {
            @Override
            public void onPoiItemClick(PoiInfo poiInfo) {
                mBaiduMap.clear();
                Bundle bundle = new Bundle();
                bundle.putString("uid", poiInfo.uid);

                MarkerOptions options = new MarkerOptions();
                options.extraInfo(bundle)
                        .animateType(MarkerOptions.MarkerAnimateType.grow)
                        .position(poiInfo.location)
                        .icon(bitmapDescriptor);

                mBaiduMap.addOverlay(options);

                MapUtil.animateOutAndIn(mBaiduMap, 2000, poiInfo.location);

            }

            @Override
            public void onLastPageClicked(int totalPageNum, int currentPageNum) {
                pageIndex--;
                startBoundSearch(null);
            }

            @Override
            public void onNextPageClicked(int totalPageNum, int currentPageNum) {
                pageIndex++;
                startBoundSearch(null);

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
