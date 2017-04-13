package com.austin.baidumap.activities;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

public class BasicmapActivity extends AppCompatActivity {
    private String TAG = "BasicmapActivity";

    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basicmap);
        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();


        //获得初始经纬度
        LatLng target = mBaiduMap.getMapStatus().target;
//        Log.e(TAG, target.toString());
//        int mapLevel = mMapView.getMapLevel();
//        //E/BasicmapActivity: initial map level:5000
//        Log.e(TAG, "initial map level:" + mapLevel);

        //设置地图加载前的背景颜色
        mMapView.setBackgroundColor(Color.parseColor("#00afec"));

    }


    /*
    MapViewLayoutParams params2 = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .width(80)
                .height(80)
                //ElayoutMode.absoluteMode下必须设置
                //注意point的点是以图片的左下角为标准的。所以下面设置中心点的Y值是加上图片高度的一半
//                .point(new Point(mMapView.getWidth()/2, mMapView.getHeight()/2))
                .point(new Point(0, 0))
//                .position(target) //ElayoutMode.mapMode下必须设置， 如果是图片的话，滑动地图图片移动不是特别的顺畅
                .build();

        mMapView.addView(imageView, params);
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //addView();
        ImageView imageView1 = new ImageView(this);
        imageView1.setImageResource(R.mipmap.ic_launcher);

        //左上角
        MapViewLayoutParams params = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .width(80)
                .height(80)
                .align(MapViewLayoutParams.ALIGN_LEFT, MapViewLayoutParams.ALIGN_TOP)
                .point(new Point(0, 0))
                .build();
        mMapView.addView(imageView1, params);


        //右上角
        ImageView imageView2 = new ImageView(this);
        imageView2.setImageResource(R.mipmap.ic_launcher);
        MapViewLayoutParams params2 = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .width(80)
                .height(80)
                .align(MapViewLayoutParams.ALIGN_RIGHT, MapViewLayoutParams.ALIGN_TOP)
                .point(new Point(mMapView.getWidth(), 0))
                .build();
        mMapView.addView(imageView2, params2);



        //左下角
        ImageView imageView3 = new ImageView(this);
        imageView3.setImageResource(R.mipmap.ic_launcher);
        MapViewLayoutParams params3 = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .width(80)
                .height(80)
                .align(MapViewLayoutParams.ALIGN_LEFT, MapViewLayoutParams.ALIGN_BOTTOM)
                .point(new Point(0, mMapView.getHeight()))
                .build();
        mMapView.addView(imageView3, params3);


        //右下角
        ImageView imageView4 = new ImageView(this);
        imageView4.setImageResource(R.mipmap.ic_launcher);
        MapViewLayoutParams params4 = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .width(80)
                .height(80)
                .align(MapViewLayoutParams.ALIGN_RIGHT, MapViewLayoutParams.ALIGN_BOTTOM)
                .point(new Point(mMapView.getWidth(), mMapView.getHeight()))
                .build();
        mMapView.addView(imageView4, params4);


        //top center
        ImageView imageView5 = new ImageView(this);
        imageView5.setImageResource(R.mipmap.ic_launcher);
        MapViewLayoutParams params5 = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .width(80)
                .height(80)
                .align(MapViewLayoutParams.ALIGN_CENTER_HORIZONTAL, MapViewLayoutParams.ALIGN_TOP)
                .point(new Point(mMapView.getWidth() / 2, 0))
                .build();
        mMapView.addView(imageView5, params5);



        //bottom center
        ImageView imageView6 = new ImageView(this);
        imageView6.setImageResource(R.mipmap.ic_launcher);
        MapViewLayoutParams params6 = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .width(80)
                .height(80)
                .align(MapViewLayoutParams.ALIGN_CENTER_HORIZONTAL, MapViewLayoutParams.ALIGN_BOTTOM)
                .point(new Point(mMapView.getWidth() / 2, mMapView.getHeight()))
                .build();
        mMapView.addView(imageView6, params6);


        //left center
        ImageView imageView7 = new ImageView(this);
        imageView7.setImageResource(R.mipmap.ic_launcher);
        MapViewLayoutParams params7 = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .width(80)
                .height(80)
                .align(MapViewLayoutParams.ALIGN_LEFT, MapViewLayoutParams.ALIGN_CENTER_VERTICAL)
                .point(new Point(0, mMapView.getHeight()/2))
                .build();
        mMapView.addView(imageView7, params7);


        //right center
        ImageView imageView8 = new ImageView(this);
        imageView8.setImageResource(R.mipmap.ic_launcher);
        MapViewLayoutParams params8 = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .width(80)
                .height(80)
                .align(MapViewLayoutParams.ALIGN_RIGHT, MapViewLayoutParams.ALIGN_CENTER_VERTICAL)
                .point(new Point(mMapView.getWidth(), mMapView.getHeight()/2))
                .build();
        mMapView.addView(imageView8, params8);

        //center
        ImageView imageView9 = new ImageView(this);
        imageView9.setImageResource(R.mipmap.ic_launcher);
        MapViewLayoutParams params9 = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .width(80)
                .height(80)
                .align(MapViewLayoutParams.ALIGN_CENTER_HORIZONTAL, MapViewLayoutParams.ALIGN_CENTER_VERTICAL)
                .point(new Point(mMapView.getWidth()/2, mMapView.getHeight()/2))
                .build();
        mMapView.addView(imageView9, params9);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mMapView!=null){
            mMapView.onDestroy();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mMapView!=null){
            mMapView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }
}
