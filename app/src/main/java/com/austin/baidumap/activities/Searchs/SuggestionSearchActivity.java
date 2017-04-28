package com.austin.baidumap.activities.Searchs;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.austin.baidumap.BaiduLocationManager;
import com.austin.baidumap.R;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import java.util.ArrayList;
import java.util.List;

import static com.austin.baidumap.R.id.listView;

public class SuggestionSearchActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private EditText mEditText;
    private BaiduMap mBaiduMap;
    private BaiduLocationManager mBaiduLocationManager;
    private boolean isFirstLoc = true;
    private SuggestionSearch suggestionSearch;
    private SuggestionSearchOption suggestionSearchOption;
    private String mCity;

    //关于显示建议列表
    List<SuggestionResult.SuggestionInfo> allSuggestions = new ArrayList<>();
    private ListView popupWindowListView;
    private PopupWindow popupWindow;
    private SuggestionAdapter suggestionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_search);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mEditText = (EditText) findViewById(R.id.editText);
        mBaiduMap = mMapView.getMap();

        mBaiduLocationManager = BaiduLocationManager.getInstance(this);

        mBaiduLocationManager.setOnReceiveBaiduLocationListener(false, new BaiduLocationManager.OnReceiveBaiduLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {

                if(location == null || mMapView == null) return;

                mCity = location.getCity();
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

        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                showSearchWidget();
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                suggestionSearchOption
                        .city(mCity)
                        .keyword(s.toString());
                suggestionSearch.requestSuggestion(suggestionSearchOption);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        suggestionSearch = SuggestionSearch.newInstance();
        suggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                List<SuggestionResult.SuggestionInfo> allSuggestions = suggestionResult.getAllSuggestions();
                SuggestionSearchActivity.this.allSuggestions = allSuggestions;
                if (suggestionResult == null ||allSuggestions==null|| suggestionResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    return;
                }else{
                    if(popupWindow.isShowing()){
                        suggestionAdapter.notifyDataSetChanged();
                    }else {
                        PopupWindowCompat.showAsDropDown(popupWindow, mEditText, 0, 10, Gravity.CENTER);
                    }
                }
            }
        });
        suggestionSearchOption = new SuggestionSearchOption();

        initInfoWindow();
    }

    private void initInfoWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_layout, null);
        popupWindowListView = (ListView) view.findViewById(listView);
        popupWindow = new PopupWindow(view);
        //// TODO: 2017/4/28 计算显示宽度
        popupWindow.setWidth(1020);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xffffffff));
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        suggestionAdapter = new SuggestionAdapter();
        popupWindowListView.setAdapter(suggestionAdapter);
    }


    private int getPaddingTop() {
        int paddingTop = (int) (Resources.getSystem().getDisplayMetrics().density * 50);
        mBaiduMap.setPadding(0, paddingTop, 0, 0);
        return paddingTop;
    }

    private void showSearchWidget() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);
        TranslateAnimation animation = new TranslateAnimation(0, 0, -getPaddingTop(), 0);
        animation.setDuration(500);
        animation.setFillAfter(true);

        AnimationSet set = new AnimationSet(this, null);
        set.addAnimation(alphaAnimation);
        set.addAnimation(animation);
        mEditText.startAnimation(set);
        mEditText.setVisibility(View.VISIBLE);
    }

    private class SuggestionAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return allSuggestions.size();
        }

        @Override
        public Object getItem(int position) {
            return allSuggestions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(SuggestionSearchActivity.this);
            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setPadding(20, 40, 0, 40);
            SuggestionResult.SuggestionInfo suggestionInfo = allSuggestions.get(position);
            tv.setText(suggestionInfo.key);
            return tv;
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
        suggestionSearch.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();

    }




}
