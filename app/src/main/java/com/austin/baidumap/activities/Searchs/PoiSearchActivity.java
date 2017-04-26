package com.austin.baidumap.activities.Searchs;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.austin.baidumap.BaiduLocationManager;
import com.austin.baidumap.R;
import com.austin.baidumap.utils.MapUtil;
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
import com.baidu.mapapi.search.core.CityInfo;
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

public class PoiSearchActivity extends AppCompatActivity implements OnGetPoiSearchResultListener {
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
    private PoiInfo poiInfo;

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
                poiDetialSearch(poiInfo);
                return false;
            }
        });
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);
        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.marker);

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
            showPoiDetial(poiDetailResult);
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
    }

    private void showPoiList(PoiResult poiResult) {
        final List<PoiInfo> allPoi = poiResult.getAllPoi();
        int currentPageNum = poiResult.getCurrentPageNum();
        int currentPageCapacity = poiResult.getCurrentPageCapacity();
        List<CityInfo> suggestCityList = poiResult.getSuggestCityList();
        final int totalPageNum = poiResult.getTotalPageNum();
        int totalPoiNum = poiResult.getTotalPoiNum();

        alertDialog = new AlertDialog.Builder(PoiSearchActivity.this)
                .setAdapter(new MyAdapter(poiResult), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            return;
                        }
                        poiInfo = allPoi.get(which-1);
                        //添加Marker
                        mBaiduMap.clear();
                        MarkerOptions options = new MarkerOptions();
                        options.position(poiInfo.location)
                                .animateType(MarkerOptions.MarkerAnimateType.grow)
                                .icon(bitmapDescriptor);
                        mBaiduMap.addOverlay(options);
                        //移动到坐标点
                        MapUtil.animateOutAndIn(mBaiduMap, 2000, poiInfo.location);
                    }
                })
                .setNegativeButton("上一页", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(pageIndex==0){
                            Toast.makeText(PoiSearchActivity.this, "没有上一页了", Toast.LENGTH_SHORT).show();
                        }else{
                            poiSearch(--pageIndex);
                            alertDialog.dismiss();
                        }
                    }
                })

                .setPositiveButton("下一页", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(pageIndex>=totalPageNum){
                            Toast.makeText(PoiSearchActivity.this, "没有下一页了", Toast.LENGTH_SHORT).show();
                        }else{
                            poiSearch(++pageIndex);
                            alertDialog.dismiss();
                        }
                    }
                })
                .create();

        ListView listView = alertDialog.getListView();

        //LayoutAnimation
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.2f, 1, 0.2f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(400);

        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(200);

        AnimationSet set = new AnimationSet(this, null);
        set.addAnimation(scaleAnimation);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        listView.setLayoutAnimation(controller);

        //Divider
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.BLUE, Color.GREEN, Color.YELLOW}));
        listView.setDividerHeight(10);

        //HeaderView
        TextView headerView = new TextView(PoiSearchActivity.this);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        headerView.setGravity(Gravity.CENTER);
        headerView.setText("当前页(从0开始)：" + currentPageNum + "\n" +
                "总页数：" + totalPageNum + "\n" +
                "currentPageCapacity:" + currentPageCapacity + "\n"+
                "totalPoiNum:" + totalPoiNum);

        listView.addHeaderView(headerView);

        //Padding
        listView.setPadding(30, 0, 30, 0);

        alertDialog.show();
    }

    private void showPoiDetial(PoiDetailResult poiDetailResult) {
        LatLng location = poiDetailResult.getLocation();
        String address = poiDetailResult.address;
        int checkinNum = poiDetailResult.checkinNum;
        int commentNum = poiDetailResult.commentNum;
        String detailUrl = poiDetailResult.detailUrl;
        double environmentRating = poiDetailResult.environmentRating;
        double facilityRating = poiDetailResult.facilityRating;
        int favoriteNum = poiDetailResult.favoriteNum;
        int grouponNum = poiDetailResult.getGrouponNum();
        double hygieneRating = poiDetailResult.getHygieneRating();
        int imageNum = poiDetailResult.getImageNum();
        String name = poiDetailResult.getName();
        double overallRating = poiDetailResult.getOverallRating();
        double price = poiDetailResult.getPrice();
        double serviceRating = poiDetailResult.getServiceRating();
        String shopHours = poiDetailResult.getShopHours();
        double tasteRating = poiDetailResult.getTasteRating();
        double technologyRating = poiDetailResult.getTechnologyRating();
        String telephone = poiDetailResult.getTelephone();
        String uid = poiDetailResult.getUid();

        String message = "location:"+location+
                "\naddress:"+address+
                "\ncheckinNum:"+checkinNum+
                "\ncommentNum:"+commentNum+
                "\ndetailUrl:"+detailUrl+
                "\nenvironmentRating:"+environmentRating+
                "\nfacilityRating:"+facilityRating+
                "\nfavoriteNum:"+favoriteNum+
                "\ngrouponNum:"+grouponNum+
                "\nhygieneRating:"+hygieneRating+
                "\nimageNum:"+imageNum+
                "\nname:"+name+
                "\noverallRating:"+overallRating+
                "\nprice:"+price+
                "\nserviceRating:"+serviceRating+
                "\nshopHours:"+shopHours+
                "\ntasteRating:"+tasteRating+
                "\ntechnologyRating:"+technologyRating+
                "\ntelephone:"+telephone+
                "\nuid:"+uid;
        AlertDialog alertDialog = new AlertDialog.Builder(PoiSearchActivity.this)
                .setCancelable(true)
                .setMessage(message)
                .show();
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

            View itemView = LayoutInflater.from(PoiSearchActivity.this).inflate(android.R.layout.simple_list_item_1, null);
            TextView tv = (TextView) itemView.findViewById(android.R.id.text1);
            PoiInfo poiInfo = allPoi.get(position);
            StringBuffer itemInfo = new StringBuffer();
            itemInfo.append("address:"+poiInfo.address);
            itemInfo.append("\ncity:"+poiInfo.city);
            itemInfo.append("\nname:" + poiInfo.name);
            itemInfo.append("\nlatitude:"+poiInfo.location.latitude);
            itemInfo.append("\nlongitude:"+poiInfo.location.longitude);
            itemInfo.append("\nphonenum:" + poiInfo.phoneNum);
            itemInfo.append("\nuid:" + poiInfo.uid);
            itemInfo.append("\npostcode:" + poiInfo.postCode);
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
