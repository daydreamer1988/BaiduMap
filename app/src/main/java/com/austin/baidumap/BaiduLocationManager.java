package com.austin.baidumap;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

import java.util.List;

/**
 * Created by Austin on 2017/4/9.
 *
 * Create the instance in Application once, and used for all activities.
 *
 */

public class BaiduLocationManager {

    private static final int HANDLER_MESSAGE_RECEIVE_LOCATION = 0x1011;

    private static Context mContext;

    private String TAG = "BaiduLocationManager";

    private static BaiduLocationManager mInstance;

    private LocationClient mClient;

    private BDLocationListener mLocationListener;

    private OnReceiveBaiduLocationListener mActivityCallback;

    private static final Object objLock = new Object();

    private Vibrator mVibrator;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private LocationClientOption option;

    private boolean logLocationMsg = true;
    private BDNotifyListener localNotifyListener;


    /**
     * location callback interface
     */
    public interface OnReceiveBaiduLocationListener{
        void onReceiveLocation(BDLocation location);
    }


    /**
     * notify callback interface
     */
    public interface OnNotifyListener{
        void onNotify(BDLocation bdLocation, float distance);
    }


    /**
     * private constructor
     * @param applicationContext
     */
    private BaiduLocationManager(Context applicationContext){
        initLocationClient(applicationContext);
    }


    /**
     * singleton getInstance
     * @param context
     * @return
     */
    public static BaiduLocationManager getInstance(Context context){
        mContext = context;
        if (mInstance == null) {
            synchronized (objLock) {
                if (mInstance == null)
                mInstance = new BaiduLocationManager(context.getApplicationContext());
            }
        }
        return mInstance;

    }


    /**
     * initial the LocationClientOption
     * getLocationOption() to get the default option
     * @param applicationContext
     */
    private void initLocationClient(Context applicationContext){
        mClient = new LocationClient(applicationContext);
        option = new LocationClientOption();
        //默认只定位一次
        //时间不能小于1000ms 否则定时失败
        option.setScanSpan(3000);

        //默认不打开
        //开启GPS可能需要一些时间，这期间返回的经纬度，时间，等可能为null
        option.setOpenGps(true);

        //高精度定位模式：这种定位模式下，会同时使用网络定位和GPS定位，优先返回最高精度的定位结果；
        //低功耗定位模式：这种定位模式下，不会使用GPS，只会使用网络定位（Wi-Fi和基站定位）
        //仅用设备定位模式：这种定位模式下，不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位
        //默认为高精度定位模式。
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        //设置坐标类型，默认GCJ02
        option.setCoorType(BDLocation.BDLOCATION_GCJ02_TO_BD09LL);

        //默认不需要，不需要时location.getAddrStr()返回null
        option.setIsNeedAddress(true);

        //默认不需要，不需要时location.getLocationDescribe()返回null
        option.setIsNeedLocationDescribe(true);//语义化信息

        //GPS定位结果中默认返回,即使设置为false gps结果中，location.getAltitude()还是会返回海拔
        option.setIsNeedAltitude(false);

        //默认不需要，不需要时location.getPoiList()返回为null
        option.setIsNeedLocationPoiList(true);

        option.setProdName(mContext.getPackageName());

        mClient.setLocOption(option);

        mLocationListener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(final BDLocation location) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mActivityCallback != null) {
                            mActivityCallback.onReceiveLocation(location);
                        }
                        if(logLocationMsg) {
                            showDefaultInfo(location, true);
                        }
                    }
                });
            }
            @Override
            public void onConnectHotSpotMessage(String s, int i) {
            }

        };



        mClient.registerLocationListener(mLocationListener);

    }


    /**
     * get the default LocationClientOption and custom it.
     */
    public LocationClientOption getLocationOption(){
        return option;
    }

    public void setOnReceiveBaiduLocationListener(boolean logLocationMsg, OnReceiveBaiduLocationListener listener) {
        this.logLocationMsg = logLocationMsg;
        mActivityCallback = listener;
    }


    /**
     * 位置提醒最多提醒3次，3次过后将不再提醒
     * 可通过函数SetNotifyLocation()来实现再次提醒
     * @param lat
     * @param lng
     * @param radius
     * @param coorType
     * @param needVibrate
     * @param onNotifyListener
     */
    public void registNotifyListener(final double lat, final double lng, final float radius, final String coorType, final boolean needVibrate, final OnNotifyListener onNotifyListener) {
        if(mContext!=null && mClient!=null && localNotifyListener ==null) {
            mVibrator = (Vibrator) mContext.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            localNotifyListener = new BDNotifyListener() {
                @Override
                public void onNotify(BDLocation bdLocation, float v) {
                    super.onNotify(bdLocation, v);
                    if(needVibrate){
                        mVibrator.vibrate(1000);
                    }
                    onNotifyListener.onNotify(bdLocation, v);
                }
            };
            localNotifyListener.SetNotifyLocation(lat, lng, radius, coorType);
            mClient.registerNotify(localNotifyListener);
        }
    }

    public void unregistNotifyListener() {
        if(mClient!=null && localNotifyListener !=null) {
            mClient.removeNotifyEvent(localNotifyListener);
            localNotifyListener = null;
        }
    }


    /**
     * 1. stop location service
     * 2. remove nofity event.
     */
    public void onStop() {
        synchronized (objLock) {
            if(mClient != null && mClient.isStarted()){
                mClient.stop();
            }
        }
    }

    /**
     * start location
     */
    public void onStart() {
        synchronized (objLock) {
            if(mClient != null && !mClient.isStarted()){
                mClient.start();
            }
        }
    }

    /**
     * when the activity destroyed, call this to release some resouces.
     */
    public void onDestroy() {
        if (mClient != null) {
            mClient.stop();
            //can not unRegisterLocationListener, cause we initialize the LocationClient in the Application
            //mClient.unRegisterLocationListener(mLocationListener);
        }
    }

    public String showDefaultInfo(BDLocation location, boolean showLog){
        if(location==null) return "";

        //获取定位结果
        StringBuffer sb = new StringBuffer(256);

        sb.append("time : ");
        sb.append(location.getTime());    //获取定位时间

        sb.append("\nerror code : ");
        sb.append(location.getLocType());    //获取类型类型

        sb.append("\nlatitude : ");
        sb.append(location.getLatitude());    //获取纬度信息

        sb.append("\nlontitude : ");
        sb.append(location.getLongitude());    //获取经度信息

        sb.append("\nradius : ");
        sb.append(location.getRadius());    //获取定位精准度

        if (location.getLocType() == BDLocation.TypeGpsLocation){

            // GPS定位结果
            sb.append("\nspeed : ");
            sb.append(location.getSpeed());    // 单位：公里每小时

            sb.append("\nsatellite : ");
            sb.append(location.getSatelliteNumber());    //获取卫星数

            sb.append("\nheight : ");
            sb.append(location.getAltitude());    //获取海拔高度信息，单位米

            sb.append("\ndirection : ");
            sb.append(location.getDirection());    //获取方向信息，单位度

            sb.append("\naddr : ");
            sb.append(location.getAddrStr());    //获取地址信息

            sb.append("\ndescribe : ");
            sb.append("gps定位成功");

        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){

            // 网络定位结果
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());    //获取地址信息

            sb.append("\ndirection : ");
            sb.append(location.getDirection());

            sb.append("\noperationers : ");
            sb.append(location.getOperators());    //获取运营商信息

            sb.append("\ndescribe : ");
            sb.append("网络定位成功");

        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

            // 离线定位结果
            sb.append("\ndescribe : ");
            sb.append("离线定位成功，离线定位结果也是有效的");

        } else if (location.getLocType() == BDLocation.TypeServerError) {

            sb.append("\ndescribe : ");
            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

            sb.append("\ndescribe : ");
            sb.append("网络不同导致定位失败，请检查网络是否通畅");

        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

            sb.append("\ndescribe : ");
            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

        }

        sb.append("\nlocationdescribe : ");
        sb.append(location.getLocationDescribe());    //位置语义化信息


        List<Poi> list = location.getPoiList();    // POI数据
        if (list != null) {
            sb.append("\npoilist size = : ");
            sb.append(list.size());
            for (Poi p : list) {
                if(p==null) return "";
                sb.append("\npoi= : ");
                sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
            }
        }

        if(showLog) {
            Log.e(TAG, sb.toString());
        }
        return sb.toString();
    }

}
