package com.austin.baidumap.activities.BasicMap;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.austin.baidumap.R;

public class BasicmapListActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basicmap_list);
        listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(new BasicMapAdapter());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class<?> clz = DATAS[position].targetClass;
                if(clz!=null)
                startActivity(new Intent(BasicmapListActivity.this, clz));
            }
        });
    }

    public ActivityInfo[] DATAS = {
            //--------------------------------------------------------
            new ActivityInfo("===MapView===", "|__________", null),

            new ActivityInfo("地图加载前的背景颜色",
                    "mMapView.setBackgroundColor()",
                    MapViewBackgroundActivity.class),
            new ActivityInfo("获取地图级别对应比例尺大小",
                    "mMapView.getMapLevel()",
                    GetMapLevelActivity.class),
            new ActivityInfo("向MapView中添加/删除View",
                    "mMapView.addView()\n" +
                    "mMapView.removeView()",
                    BasicmapActivity.class),
            new ActivityInfo("获取MapView比例尺宽高,显示隐藏,设置位置",
                    "mMapView.getScaleControlViewWidth()\n" +
                    "mMapView.getScaleControlViewHeight()\n" +
                    "mMapView.showScaleControl()\n" +
                    "mMapView.setScaleControlPosition(Point)",
                    ScaleControlActivity.class),
            new ActivityInfo("获取/设置百度Logo的位置",
                    "mMapView.getLogoPosition()\n" +
                    "mMapView.setLogoPosition(LogoPosition)",
                    LogoPositionActivity.class),
            new ActivityInfo("设置ZoomControl的位置",
                    "mMapView.setZoomControlsPosition(Point)",
                    ZoomControlActivity.class),
            new ActivityInfo("设置地图的Padding",
                    "mMapView.setPadding(left, top, right, bottom)\n" +
                    "mBaiduMap.setViewPadding(left, top, right, bottom)",
                    PaddingActivity.class),
            new ActivityInfo("自定义地图样式",
                    "MapView.setCustomMapStylePath(FilePath)\n"+
                    "MapView.setMapCustomEnable()",
                    CustomMapActivity.class),

            new ActivityInfo("取消百度Logo，比例尺 ",
                    "mMapView.removeViewAt(1)\n"+
                    "mMapView.removeViewAt(2)",
                    RemoveBaiduLogoActivity.class),
            //--------------------------------------------------------

            new ActivityInfo("===BaiduMap===", "|__________", null),
            new ActivityInfo("设置mapType,及热力图，交通图",
                    "mBaiduMap.getMapType()\n"+
                    "mBaiduMap.setMapType()\n" +
                    "   BaiduMap.MAP_TYPE_NORMAL\n" +
                    "   BaiduMap.MAP_TYPE_SATELLITE\n" +
                    "   BaiduMap.MAP_TYPE_NONE\n" +
                    "mBaiduMap.setTrafficEnabled()\n" +
                    "mBaiduMap.setBaiduHeatMapEnabled()",
                    MapTypeActivity.class),
            new ActivityInfo("获得初始经纬度",
                    "mBaiduMap.getMapStatus().target",
                    GetInitialLatLntActivity.class),

            new ActivityInfo("MapStatusLimit",
                    "mBaiduMap.getMapStatusLimit()\n" +
                    "mBaiduMap.setMapStatusLimits(LatLngBounds)\n" +
                    "在OnMapLoadedCallback.onMapLoaded() 之后设置才生效",
                    MapStatusLimitActivity.class),
            new ActivityInfo("地图操作监听",
                    "mBaiduMap.setOnMapTouchListener()\n" +
                    "mBaiduMap.setOnMapClickListener()\n" +
                    "mBaiduMap.setOnMapLongClickListener()\n" +
                    "mBaiduMap.setOnMapDoubleClickListener()\n" +
                    "mBaiduMap.setOnMapLoadedCallback()\n" +
                    "mBaiduMap.setOnMapRenderCallbadk()\n" +
                    "mBaiduMap.setOnMapDrawFrameCallback()\n"+
                    "mBaiduMap.setOnMarkerClickListener()\n" +
                    "mBaiduMap.setOnMarkerDragListener()\n" +
                    "mBaiduMap.addOverlay(OverLayOption)\n" +
                    "option.position(LatLng).animateType().icon().draggable()\n" +
                    "mBaiduMap.clear();//清空地图所有的Overlay覆盖物及InfoWindow",
                    MapViewTouchEventActivity.class),

            new ActivityInfo("获取区域内所有的Marker",
                    "List<Marker>:mBaiduMap.getMarkersInBounds(LatLngBounds)\n"+
                    "OverlayOptions 设置period（多少帧刷新一次图片）\n" +
                            "可以解决动画卡顿问题，默认值20，要求大于0",
                    GetMarkerInBoundsActivity.class),

            new ActivityInfo("获取、设置指南针位置，及设置指南针图片，与是否显示",
                    "mBaiduMap.getCompassPosition():Point\n" +
                    "mBaiduMap.setCompassPosition(Point)\n" +
                    "mBaiduMap.setCompassIcon(Bitmap)\n" +
                    "UiSettings.setCompassEnabled()",
                    CompassActivity.class),

            new ActivityInfo("获取、设置ZoomLevel",
                    "mBaiduMap.getMaxZoomLevel()\n" +
                    "mBaiduMap.getMinZoomLevel()\n" +
                    "mBaiduMap.setMaxAndMinZoomLevel(13,11)",
                    ZoomLevelActivity.class),
            new ActivityInfo("BaiduHeadMap(是否打开了百度热力图层)",
                    "mBaiduMap.isBaiduHeatMapEnabled()\n" +
                            "mBaiduMap.isSupportBaiduHeatMap()\n" +
                            "",
                    BaiduHeatMapActivity.class),
            new ActivityInfo("是否显示底图默认Poi",
                    "mBaiduMap.showMapPoi()",
                    IsShowPoiActivity.class),
            new ActivityInfo("Projection(OnMapLoadedCallback.onMapLoaded()之后才正常)",
                    "mProjection().toScreenLocation(latLng)\n" +
                    "mProjection().fromScreenLocation(point)\n\n" +
                            "mBaiduMap.setPadding(left,top,right,bottom)deprecated方法\n" +
                            "注意不是mBaiduMap.setViewPadding()方法\n" +
                            "该方法的优点是，适配了中心点不受覆盖物的影响，始终在中心点",
                    ProjectionActivity.class),

            //--------------------------------------------------------

            new ActivityInfo("===UiSettings===",
                    "|__________",null),

            new ActivityInfo("UiSettings,各种手势开关，及是否显示Compass",
                    "uiSettings.setZoomGesturesEnabled()\n" +
                    "uiSettings.setScrollGesturesEnabled()\n" +
                    "uiSettings.setRotateGesturesEnabled()\n" +
                    "uiSettings.setOverlookingGesturesEnabled()\n" +
                    "uiSettings.setCompassEnabled()\n" +
                    "uiSettings.setAllGesturesEnabled()\n" +
                    "uiSettings.isZoomGesturesEnabled()\n" +
                    "uiSettings.isScrollGesturesEnabled()\n" +
                    "uiSettings.isRotateGesturesEnabled()\n" +
                    "uiSettings.isOverlookingGesturesEnabled()\n" +
                    "uiSettings.isCompassEnabled()",
                    UiSettingsActivity.class),


            //--------------------------------------------------------

            new ActivityInfo("===InfoWindow===",
                    "|__________",null),

            new ActivityInfo("InfoWindow显示及隐藏（一）",
                    "mBaiduMap.showInfoWindow(InfoWindow)\n" +
                    "mBaiduMap.hideInfoWindow()\n" +
                    "new InfoWindow(View, LatLng, yOffset)",
                    InfoWindowActivity.class),

            new ActivityInfo("InfoWindow显示及隐藏（二）",
                    "mBaiduMap.showInfoWindow(InfoWindow)\n" +
                            "mBaiduMap.hideInfoWindow()\n" +
                            "new InfoWindow(View, LatLng, yOffset)",
                    InfoWindowActivity2.class),

            new ActivityInfo("InfoWindow显示及隐藏（三推荐）",
                    "mBaiduMap.showInfoWindow(InfoWindow)\n" +
                    "mBaiduMap.hideInfoWindow()\n" +
                    "new InfoWindow(View,LatLng,yOffset,OnInfoWindowClickListener)",
                    InfoWindowActivity3.class),

            //--------------------------------------------------------
            new ActivityInfo("===MapStatus, MapStatus.Builder, MapStatusUpdate, MapStatusUpdateFactory===",
                    "|__________",
                    null),
            new ActivityInfo("代码缩放，旋转，俯视",
                    "MapStatus.Builder().zoom/rotate/overlook(float).build()\n" +
                    "MapStatusUpdateFactory.newMapStatus(mapStatus)\n" +
                    "mBaiduMap.animateMapStatus(mapStatusUpdate, time)",
                    MapControlActivity.class),

            //--------------------------------------------------------

            new ActivityInfo("===定位===",
                    "|__________",
                    null),

            new ActivityInfo("定位我的位置",
                    "解决了一开始定位北京的问题\n" +
                    "解决了网速慢的情况下一直显示空白页的问题",
                    MyLocationSimpleActivity.class),

            //--------------------------------------------------------

            new ActivityInfo("===MyLocationConfiguration===",
                    "|__________",
                    null),

            new ActivityInfo("设置三种定位模式，自定义图标，方向传感器",
                    "LocationMode.FOLLOWING\n" +
                    "LocationMode.NORMAL\n" +
                    "LocationMode.COMPASS\n" +
                    "mBaiduMap.setMyLocationConfiguration(MyLocationConfiguration)\n" +
                    "SensorUtil.registerSensor(context, OnSensorChangedListener)\n" +
                    "SensorUtil.unRegisterSensor()\n" +
                    "mBaiduMap.getLocationConfiguration()\n" +
                    "mBaiduMap.setMyLocationConfiguration()\n" +
                    "只有先允许定位图层后设置定位图层配置信息才会生效\n" +
                    "mBaiduMap.setMyLocationData\n" +
                    "只有先允许定位图层后设置数据才会生效\n" +
                            "mBaiduMap.getLocationData()在未设置前返回为NULL",
                    MyLocationConfigurationActivity.class),

    };


    class ActivityInfo{
        private String title;
        private Class targetClass;
        private String desc;
        public ActivityInfo(String title, String desc, Class targetClass){
            this.title = title;
            this.targetClass = targetClass;
            this.desc = desc;
        }
    }

    private class BasicMapAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return DATAS.length;
        }

        @Override
        public Object getItem(int position) {
            return DATAS[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = LayoutInflater.from(BasicmapListActivity.this).inflate(R.layout.info_item, null);
            TextView title = (TextView) inflate.findViewById(R.id.title);
            TextView desc = (TextView) inflate.findViewById(R.id.desc);
            title.setText(DATAS[position].title);
            desc.setText(DATAS[position].desc);
            if(DATAS[position].targetClass==null){
                title.setTextColor(Color.RED);
                desc.setTextColor(Color.RED);
            }
            return inflate;
        }
    }


}
