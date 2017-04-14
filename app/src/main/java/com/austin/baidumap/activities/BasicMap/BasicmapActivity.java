package com.austin.baidumap.activities.BasicMap;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.TextureMapView;

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

        mMapView.getMap().setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                Toast.makeText(BasicmapActivity.this, "中间点的坐标：\n" +mapStatus.target.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                addView();
            }
        });

    }

    private void addView() {
        //addView();
        final ImageView imageView1 = new ImageView(this);
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

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.removeView(imageView1);
            }
        });


        //右上角
        final ImageView imageView2 = new ImageView(this);
        imageView2.setImageResource(R.mipmap.ic_launcher);
        MapViewLayoutParams params2 = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .width(80)
                .height(80)
                .align(MapViewLayoutParams.ALIGN_RIGHT, MapViewLayoutParams.ALIGN_TOP)
                .point(new Point(mMapView.getWidth(), 0))
                .build();
        mMapView.addView(imageView2, params2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.removeView(imageView2);
            }
        });


        //左下角
        final ImageView imageView3 = new ImageView(this);
        imageView3.setImageResource(R.mipmap.ic_launcher);
        MapViewLayoutParams params3 = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .width(80)
                .height(80)
                .align(MapViewLayoutParams.ALIGN_LEFT, MapViewLayoutParams.ALIGN_BOTTOM)
                .point(new Point(0, mMapView.getHeight()))
                .build();
        mMapView.addView(imageView3, params3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.removeView(imageView3);
            }
        });

        //右下角
        final ImageView imageView4 = new ImageView(this);
        imageView4.setImageResource(R.mipmap.ic_launcher);
        MapViewLayoutParams params4 = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .width(80)
                .height(80)
                .align(MapViewLayoutParams.ALIGN_RIGHT, MapViewLayoutParams.ALIGN_BOTTOM)
                .point(new Point(mMapView.getWidth(), mMapView.getHeight()))
                .build();
        mMapView.addView(imageView4, params4);
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.removeView(imageView4);
            }
        });

        //top center
        final ImageView imageView5 = new ImageView(this);
        imageView5.setImageResource(R.mipmap.ic_launcher);
        MapViewLayoutParams params5 = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .width(80)
                .height(80)
                .align(MapViewLayoutParams.ALIGN_CENTER_HORIZONTAL, MapViewLayoutParams.ALIGN_TOP)
                .point(new Point(mMapView.getWidth() / 2, 0))
                .build();
        mMapView.addView(imageView5, params5);
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.removeView(imageView5);
            }
        });


        //bottom center
        final ImageView imageView6 = new ImageView(this);
        imageView6.setImageResource(R.mipmap.ic_launcher);
        MapViewLayoutParams params6 = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .width(80)
                .height(80)
                .align(MapViewLayoutParams.ALIGN_CENTER_HORIZONTAL, MapViewLayoutParams.ALIGN_BOTTOM)
                .point(new Point(mMapView.getWidth() / 2, mMapView.getHeight()))
                .build();
        mMapView.addView(imageView6, params6);
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.removeView(imageView6);
            }
        });

        //left center
        final ImageView imageView7 = new ImageView(this);
        imageView7.setImageResource(R.mipmap.ic_launcher);
        MapViewLayoutParams params7 = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .width(80)
                .height(80)
                .align(MapViewLayoutParams.ALIGN_LEFT, MapViewLayoutParams.ALIGN_CENTER_VERTICAL)
                .point(new Point(0, mMapView.getHeight()/2))
                .build();
        mMapView.addView(imageView7, params7);
        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.removeView(imageView7);
            }
        });

        //right center
        final ImageView imageView8 = new ImageView(this);
        imageView8.setImageResource(R.mipmap.ic_launcher);
        MapViewLayoutParams params8 = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .width(80)
                .height(80)
                .align(MapViewLayoutParams.ALIGN_RIGHT, MapViewLayoutParams.ALIGN_CENTER_VERTICAL)
                .point(new Point(mMapView.getWidth(), mMapView.getHeight()/2))
                .build();
        mMapView.addView(imageView8, params8);
        imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.removeView(imageView8);
            }
        });
        //center
        final ImageView imageView9 = new ImageView(this);
        imageView9.setImageResource(R.mipmap.ic_launcher);
        MapViewLayoutParams params9 = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                .width(80)
                .height(80)
                .align(MapViewLayoutParams.ALIGN_CENTER_HORIZONTAL, MapViewLayoutParams.ALIGN_CENTER_VERTICAL)
                .point(new Point(mMapView.getWidth()/2, mMapView.getHeight()/2))
                .build();
        mMapView.addView(imageView9, params9);
        imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //方案1：直接消失
                //mMapView.removeView(imageView9);

                //方案2：动画消失

                Animation animation = new AlphaAnimation(1, 0);
                animation.setDuration(1000);
                animation.setFillAfter(true);
                imageView9.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mMapView.removeView(imageView9);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //这里会多次调用，可以在添加的控件上做文章，判断是否为空，为空才重新添加
        //addView();
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
