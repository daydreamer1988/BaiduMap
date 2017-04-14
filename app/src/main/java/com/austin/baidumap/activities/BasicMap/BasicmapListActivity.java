package com.austin.baidumap.activities.BasicMap;

import android.content.Intent;
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
                startActivity(new Intent(BasicmapListActivity.this, clz));
            }
        });
    }

    public ActivityInfo[] DATAS = {
            new ActivityInfo("地图加载前的背景颜色",
                    "MapView.setBackgroundColor()",
                    MapViewBackgroundActivity.class),
            new ActivityInfo("获取地图级别对应比例尺大小",
                    "MapView.getMapLevel()",
                    GetMapLevelActivity.class),
            new ActivityInfo("获得初始经纬度",
                    "BaiduMap.getMapStatus().target",
                    GetInitialLatLntActivity.class),
            new ActivityInfo("向MapView中添加/删除View",
                    "MapView.addView()\n" +
                    "MapView.removeView()",
                    BasicmapActivity.class),
            new ActivityInfo("获取MapView比例尺宽高,显示隐藏,设置位置",
                    "MapView.getScaleControlViewWidth()\n" +
                    "MapView.getScaleControlViewHeight()\n" +
                    "MapView.showScaleControl()\n" +
                    "MapView.setScaleControlPosition(Point)",
                    ScaleControlActivity.class),
            new ActivityInfo("获取/设置百度Logo的位置",
                    "MapView.getLogoPosition()\n" +
                    "MapView.setLogoPosition(LogoPosition)",
                    LogoPositionActivity.class),
            new ActivityInfo("设置ZoomControl的位置",
                    "MapView.setZoomControlsPosition(Point)",
                    ZoomControlActivity.class),
            new ActivityInfo("设置地图的Padding",
                    "MapView.setPadding(left, top, right, bottom)",
                    PaddingActivity.class),
            new ActivityInfo("自定义地图样式",
                    "MapView.setCustomMapStylePath(FilePath)\n"+
                    "MapView.setMapCustomEnable()",
                    CustomMapActivity.class),
            new ActivityInfo("设置mapType,及热力图，交通图",
                    "mBaiduMap.setMapType()\n" +
                    "   BaiduMap.MAP_TYPE_NORMAL\n" +
                    "   BaiduMap.MAP_TYPE_SATELLITE\n" +
                    "BaiduMap.setTrafficEnabled()\n" +
                    "BaiduMap.setBaiduHeatMapEnabled()",
                    MapTypeActivity.class),

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
            return inflate;
        }
    }


}
