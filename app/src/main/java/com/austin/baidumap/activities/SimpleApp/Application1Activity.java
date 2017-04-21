package com.austin.baidumap.activities.SimpleApp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.TextureMapView;

public class Application1Activity extends AppCompatActivity {
    private TextureMapView mMapView;
    private TextView mBottomLayout;
    private int mDrawingHeight;
    private BaiduMap mBaiduMap;
    private boolean expanded = false;
    private boolean animationProcessing = false;
    private Animation animation;
    private float originY;
    private int mOriginLayoutHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application1);

        mMapView = (TextureMapView) findViewById(R.id.mapView);
        mBottomLayout = (TextView) findViewById(R.id.bottomLayout);

        mBaiduMap = mMapView.getMap();


        mBottomLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(!expanded)
                    expand();
//                    startAnimation();
                }
                return true;
            }
        });

        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
                    if (expanded)
//                        startAnimation();
                    shrink();
                }
            }
        });


    }

    private void startAnimation(){
        if(animationProcessing){
            return;
        }
        final int layoutStartHeight = mBottomLayout.getLayoutParams().height;
        final float startY = mBottomLayout.getY();
        float toY = 0;
        if(expanded){
            toY = mDrawingHeight-mOriginLayoutHeight;
        }else{
            toY = mDrawingHeight/2;
        }
        //expanded:-  not expanded:+
        final float deltaY = startY - toY;

        animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                float fraction = deltaY * interpolatedTime;
                ViewGroup.LayoutParams layoutParams1 = mBottomLayout.getLayoutParams();
                layoutParams1.height = (int) (layoutStartHeight + fraction);
                //mBaiduMap.setPadding(0, 0, 0, (int) (startY + paddingBottom-mOriginLayoutHeight));
            }
        };
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animationProcessing = true;
            }
            @Override
            public void onAnimationEnd(Animation animation) {

                expanded = !expanded;
                animationProcessing = false;
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        animation.setDuration(500);
        if(expanded){
            animation.setInterpolator(new AnticipateInterpolator());
        }else{
            animation.setInterpolator(new OvershootInterpolator());
        }
        mBottomLayout.startAnimation(animation);
    }

    private void shrink() {
        if(animationProcessing){
            return;
        }
        final float deltaY = originY - mDrawingHeight / 2;

        ViewGroup.LayoutParams layoutParams = mBottomLayout.getLayoutParams();
        final int originHeight = layoutParams.height;

        animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                float translateY = deltaY * interpolatedTime;
                ViewGroup.LayoutParams layoutParams1 = mBottomLayout.getLayoutParams();
                layoutParams1.height = (int) (originHeight - translateY);
                mBaiduMap.setPadding(0, 0, 0, (int) (deltaY-translateY));
            }
        };

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animationProcessing = true;

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                expanded = false;
                animationProcessing = false;
                float zoom = mBaiduMap.getMapStatus().zoom;
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(++zoom), 1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setDuration(500);
        animation.setInterpolator(new AnticipateInterpolator());
        mBottomLayout.startAnimation(animation);


    }

    private void expand() {
        if(animationProcessing){
           return;
        }
        float targetY = mDrawingHeight / 2;
        originY = mBottomLayout.getY();
        final float deltaY = originY - targetY;
        ViewGroup.LayoutParams layoutParams = mBottomLayout.getLayoutParams();
        final int originHeight = layoutParams.height;

        animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                int translateY = (int) (deltaY * interpolatedTime);
                ViewGroup.LayoutParams layoutParams1 = mBottomLayout.getLayoutParams();
                layoutParams1.height = originHeight + translateY;
                mBaiduMap.setPadding(0, 0, 0, translateY);
            }
        };

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animationProcessing = true;

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                expanded = true;
                animationProcessing = false;
                float zoom = mBaiduMap.getMapStatus().zoom;
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(--zoom), 1000);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        animation.setDuration(500);
        animation.setInterpolator(new OvershootInterpolator());
        mBottomLayout.startAnimation(animation);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mDrawingHeight = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight();
        mOriginLayoutHeight = mBottomLayout.getLayoutParams().height;
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
