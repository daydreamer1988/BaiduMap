package com.austin.baidumap.activities.BasicMap;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

public class ProjectionActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private Marker mMarker;
    private TextView mTextView3;
    private float x;
    private float y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projection);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mTextView3 = (TextView) findViewById(R.id.textView3);
        mBaiduMap = mMapView.getMap();

        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                x = motionEvent.getX();
                y = motionEvent.getY();
            }
        });

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addMarker(latLng);
                getProjection(latLng);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                addMarker(mapPoi.getPosition());
                getProjection(mapPoi.getPosition());
                return false;
            }
        });
    }

    private void getProjection(LatLng latLng) {
        Point point = mBaiduMap.getProjection().toScreenLocation(latLng);
        LatLng latLng1 = mBaiduMap.getProjection().fromScreenLocation(point);
        String info =
                "<b>原始XY：OnMapTouchListener</b><br>"+new Point((int)x, (int)y).toString()+
                "<br><b>原始LatLng:setOnMapClickListener</b><br>"+latLng.toString()+
                "<br><b>ProjectionXY:mProjection().toScreenLocation(latLng)</b><br>"+point.toString()+
                "<br><b>ProjectionLatLng：mProjection().fromScreenLocation(point)</b><br>"+latLng1.toString();
        mTextView3.setText(Html.fromHtml(info));


        int lineHeight = mTextView3.getLineHeight();
        int lineCount = mTextView3.getLineCount();

        final int dalta = lineCount * lineHeight;

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                float height = dalta * interpolatedTime;
                        ViewGroup.LayoutParams layoutParams = mTextView3.getLayoutParams();
                        layoutParams.height = (int) height;
                mBaiduMap.setPadding(0, (int) height, 0, 0);
            }
        };
        animation.setDuration(500);
        animation.setInterpolator(new OvershootInterpolator());
        mTextView3.startAnimation(animation);

    }

    private void addMarker(LatLng latLng) {
        if(mMarker!=null){
            mMarker.remove();
        }
        OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker))
                .animateType(MarkerOptions.MarkerAnimateType.grow);
        mMarker = (Marker) mBaiduMap.addOverlay(option);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

}
