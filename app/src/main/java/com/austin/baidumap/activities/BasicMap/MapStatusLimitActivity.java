package com.austin.baidumap.activities.BasicMap;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

public class MapStatusLimitActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private TextView mTextView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_status_limit);

        mTextView4 = (TextView) findViewById(R.id.textView4);

        mMapView = (TextureMapView) findViewById(R.id.mapView);

        mBaiduMap = mMapView.getMap();

        //初始限制
        //southwest: -80.68706311884208, -179.99372650799245
        //northeast: 84.62152201347317, 179.99634956008063
        LatLngBounds mapStatusLimit = mBaiduMap.getMapStatusLimit();

        mTextView4.setText("初始限制:\n"+mapStatusLimit.northeast+"\n"+mapStatusLimit.southwest);
    }

    //Projection获得东北，西南角
    public void setMapStatusLimit(View view) {
        Projection projection = mBaiduMap.getProjection();
        LatLng northEast = projection.fromScreenLocation(new Point(mMapView.getWidth(), 0));
        LatLng southWest = projection.fromScreenLocation(new Point(0, mMapView.getHeight()));
        LatLng northWest = projection.fromScreenLocation(new Point(0, 0));
        LatLng southEast = projection.fromScreenLocation(new Point(mMapView.getWidth(), mMapView.getHeight()));

        String info = "当前屏幕坐标：\n" +
                "西北：\n"+northWest+"\n" +
                "东北：\n"+northEast+"\n" +
                "西南：\n"+southWest+"\n" +
                "东南：\n"+southEast+"\n\n";
        LatLngBounds bounds = new LatLngBounds.Builder().include(northEast).include(southWest).build();
        mBaiduMap.setMapStatusLimits(bounds);
        mTextView4.setText(info+
                "Projection获得的东北角：\n" +
                ""+northEast+"\n" +
                "Projection获得的西南角：\n"+southWest+"\n" +
                "LatLngBounds东北角：\n"+bounds.northeast+"\n" +
                "LatLngBounds西南角：\n"+bounds.southwest);
    }

    //Projection获得西北，东南角
    public void setMapStatusLimit2(View view) {
        Projection projection = mBaiduMap.getProjection();
        LatLng northWest = projection.fromScreenLocation(new Point(0, 0));
        LatLng southEast = projection.fromScreenLocation(new Point(mMapView.getWidth(), mMapView.getHeight()));
        LatLng northEast = projection.fromScreenLocation(new Point(mMapView.getWidth(), 0));
        LatLng southWest = projection.fromScreenLocation(new Point(0, mMapView.getHeight()));
        String info = "当前屏幕坐标：\n" +
                "西北：\n"+northWest+"\n" +
                "东北：\n"+northEast+"\n" +
                "西南：\n"+southWest+"\n" +
                "东南：\n"+southEast+"\n\n";
        LatLngBounds bounds = new LatLngBounds.Builder().include(northWest).include(southEast).build();
        mBaiduMap.setMapStatusLimits(bounds);

        mTextView4.setText(info+
                "Projection获得的西北角：\n" +
                ""+northWest+"\n" +
                "Projection获得的东南角：\n"+southEast+"\n" +
                "LatLngBounds东北角：\n"+bounds.northeast+"\n" +
                "LatLngBounds西南角：\n"+bounds.southwest);
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

}
