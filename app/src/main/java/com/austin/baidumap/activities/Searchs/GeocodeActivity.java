package com.austin.baidumap.activities.Searchs;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.austin.baidumap.BaiduLocationManager;
import com.austin.baidumap.R;
import com.austin.baidumap.utils.MapUtil;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class GeocodeActivity extends AppCompatActivity implements OnGetGeoCoderResultListener {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private BaiduLocationManager mLocationManager;
    private boolean isFirstLoc = true;
    private String mCity;
    private TextView mTextView;
    private StringBuffer mStringBuffer = new StringBuffer();
    private GeoCoder coder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocode);

        mMapView = (TextureMapView) findViewById(R.id.mapView);

        mTextView = (TextView) findViewById(R.id.textView);

        mBaiduMap = mMapView.getMap();

        mLocationManager = BaiduLocationManager.getInstance(this);

        mLocationManager.setOnReceiveBaiduLocationListener(false, new BaiduLocationManager.OnReceiveBaiduLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                MyLocationData myLocationData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build();

                mBaiduMap.setMyLocationData(myLocationData);
                mBaiduMap.setMyLocationEnabled(true);

                LatLng target = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus mapStatus = new MapStatus.Builder()
                        .target(target)
                        .zoom(15)
                        .build();

                if(isFirstLoc){
//                    isFirstLoc = false;
                    MapUtil.animateOutAndIn(mBaiduMap, 2000, target);
                    mCity = location.getCity();
                    //先获取城市
                    mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            mStringBuffer.setLength(0);
                            mStringBuffer.append("点击经纬度：\n"+latLng);
                            reverseGeoCode(latLng);
                        }

                        @Override
                        public boolean onMapPoiClick(MapPoi mapPoi) {
                            mStringBuffer.setLength(0);
                            mStringBuffer.append("点击POI经纬度：\n"+mapPoi.getPosition());
                            mStringBuffer.append("\n地址：" + mapPoi.getName());
                            geoCode(mapPoi.getName());
                            return false;
                        }
                    });
                }

            }
        });

        coder = GeoCoder.newInstance();
        coder.setOnGetGeoCodeResultListener(this);

    }

    private void reverseGeoCode(LatLng latLng) {
        ReverseGeoCodeOption option = new ReverseGeoCodeOption();
        option.location(latLng);
        coder.reverseGeoCode(option);
    }

    private void geoCode(String name) {
        GeoCodeOption option = new GeoCodeOption();
        option.city(mCity).address(name);
        coder.geocode(option);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "no result", Toast.LENGTH_SHORT).show();
            return;
        }

        LatLng location = geoCodeResult.getLocation();
        String address = geoCodeResult.getAddress();


        mStringBuffer.append(
                "\n地理编码坐标：\n" + location +
                "\n地址：" + address);

        AlertDialog dialog = new AlertDialog.Builder(GeocodeActivity.this)
                .setMessage(mStringBuffer)
                .show();

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if(reverseGeoCodeResult==null || reverseGeoCodeResult.error!= SearchResult.ERRORNO.NO_ERROR){
            Toast.makeText(this, "no result", Toast.LENGTH_SHORT).show();
            return;
        }

        LatLng location = reverseGeoCodeResult.getLocation();
        String businessCircle = reverseGeoCodeResult.getBusinessCircle();
        String sematicDescription = reverseGeoCodeResult.getSematicDescription();
        String address = reverseGeoCodeResult.getAddress();

        ReverseGeoCodeResult.AddressComponent addressDetail = reverseGeoCodeResult.getAddressDetail();
        String countryName = addressDetail.countryName;
        int countryCode = addressDetail.countryCode;
        String province = addressDetail.province;
        String city = addressDetail.city;
        String district = addressDetail.district;
        String street = addressDetail.street;
        String streetNumber = addressDetail.streetNumber;
        mStringBuffer.append(
                "\n反地理经纬度：\n"+location +
                "\n商圈："+businessCircle +
                "\n描述："+sematicDescription+
                "\n地址："+address+
                "\nDetail:" +
                "\ncountryName:"+countryName+
                "\ncountryCode:"+countryCode+
                "\nprovince:"+province+
                "\ncity:"+city+
                "\ndistrict:"+district+
                "\nstreet:"+street+
                "\nstreetNumber:"+streetNumber);

        AlertDialog dialog = new AlertDialog.Builder(GeocodeActivity.this)
                .setMessage(mStringBuffer)
                .setCancelable(true)
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationManager.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationManager.onStop();
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
        mLocationManager.onDestroy();


        //////////////////////////
        coder.destroy();

        //////////////////////////
    }

    public void clearCircle(View view) {
        mBaiduMap.clear();
    }


}
