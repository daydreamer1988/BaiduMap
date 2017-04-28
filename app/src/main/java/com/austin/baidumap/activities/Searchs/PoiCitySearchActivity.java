package com.austin.baidumap.activities.Searchs;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
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
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.List;

public class PoiCitySearchActivity extends AppCompatActivity implements OnGetPoiSearchResultListener {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private BaiduLocationManager mBaiduLocationManager;
    private String mCity;
    private PoiSearch poiSearch;
    private EditText mEditText;
    private int pageIndex = 0;
    private AlertDialog alertDialog;
    private BitmapDescriptor bitmapDescriptor;
    private boolean isFirstLoc = true;
    private PoiInfo mPoiInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();

        mEditText = (EditText) findViewById(R.id.editText);

        mBaiduLocationManager = BaiduLocationManager.getInstance(this);

        mBaiduLocationManager.setOnReceiveBaiduLocationListener(false, new BaiduLocationManager.OnReceiveBaiduLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                MyLocationData myLocationData = new MyLocationData.Builder()
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build();

                mBaiduMap.setMyLocationData(myLocationData);

                if(isFirstLoc) {
                    isFirstLoc = false;
                    mBaiduMap.setMyLocationEnabled(true);

                    MapStatus mapStatus = new MapStatus.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))
                            .zoom(15)
                            .build();

                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
                }

                mCity = location.getCity();
            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                poiDetialSearch(mPoiInfo);
                return false;
            }
        });
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);
        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.marker);

        //内容改变，pageIndex从0开计
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

    }


    /**
     * 点击开始查询
     * @param view
     */
    public void startSearch(View view) {
        if(mEditText.getText().length()==0){
            Toast.makeText(this, "请输入要查询的keyword", Toast.LENGTH_SHORT).show();
        }else {
            poiSearch(pageIndex);
        }
    }


    private void poiSearch(int pageIndex) {
        PoiCitySearchOption option = new PoiCitySearchOption();
        option.city(mCity).keyword(mEditText.getText().toString()).pageCapacity(20).pageNum(pageIndex);
        poiSearch.searchInCity(option);
    }

    private void poiDetialSearch(PoiInfo poiInfo) {
        //搜索详细信息
        poiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(poiInfo.uid));

    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if(poiResult==null || poiResult.error!= SearchResult.ERRORNO.NO_ERROR){
            Toast.makeText(this, "未查询到结果", Toast.LENGTH_SHORT).show();
        }else{
            showPoiList(poiResult);
        }
    }




    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult == null || poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "未找到详细信息", Toast.LENGTH_SHORT).show();
            return;
        }else{
            PoiDialogUtil.getInstance(PoiCitySearchActivity.this).showPoiDetial(poiDetailResult);
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
    }

    private void showPoiList(PoiResult poiResult) {
        PoiDialogUtil.getInstance(PoiCitySearchActivity.this).showPoiList(poiResult, new PoiDialogUtil.OnPoiItemSelected() {
            @Override
            public void onPoiItemClick(PoiInfo poiInfo) {
                mPoiInfo = poiInfo;
                mBaiduMap.clear();
                MarkerOptions options = new MarkerOptions();
                options.position(poiInfo.location)
                        .animateType(MarkerOptions.MarkerAnimateType.grow)
                        .icon(bitmapDescriptor);
                mBaiduMap.addOverlay(options);
                //移动到坐标点
                MapUtil.animateOutAndIn(mBaiduMap, 2000, poiInfo.location);
            }

            @Override
            public void onLastPageClicked(int totalPageNum, int currentPageNum) {
                poiSearch(--pageIndex);
            }

            @Override
            public void onNextPageClicked(int totalPageNum, int currentPageNum) {
                poiSearch(++pageIndex);
            }
        });
    }


    private class MyAdapter extends BaseAdapter {
        private final List<PoiInfo> allPoi;
        private PoiResult poiResult;

        public MyAdapter(PoiResult poiResult) {
            this.poiResult = poiResult;
            allPoi = poiResult.getAllPoi();
        }

        @Override
        public int getCount() {
            return allPoi.size();
        }

        @Override
        public Object getItem(int position) {
            return allPoi.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = LayoutInflater.from(PoiCitySearchActivity.this).inflate(android.R.layout.simple_list_item_1, null);
            TextView tv = (TextView) itemView.findViewById(android.R.id.text1);
            PoiInfo poiInfo = allPoi.get(position);
            StringBuffer itemInfo = new StringBuffer();
            itemInfo.append("address:"+poiInfo.address);
            itemInfo.append("\ncity:"+poiInfo.city);
            itemInfo.append("\nname:" + poiInfo.name);
            if(poiInfo.location!=null) {
                itemInfo.append("\nlatitude:" + poiInfo.location.latitude);
                itemInfo.append("\nlongitude:" + poiInfo.location.longitude);
            }
            itemInfo.append("\nphonenum:" + poiInfo.phoneNum);
            itemInfo.append("\nuid:" + poiInfo.uid);
            itemInfo.append("\npostcode:" + poiInfo.postCode);
            itemInfo.append("\npoitype:" + poiInfo.type);

            tv.setText(itemInfo);

            return itemView;
        }
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
