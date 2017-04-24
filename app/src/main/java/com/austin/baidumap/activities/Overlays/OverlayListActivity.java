package com.austin.baidumap.activities.Overlays;

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

public class OverlayListActivity extends AppCompatActivity {
    private ListView mListView;
    private ActivityInfo[] mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay_list);
        mListView = (ListView) findViewById(R.id.listView);

        mDatas = new ActivityInfo[]{
                new ActivityInfo("点",
                        "mBaiduMap.addOverlay(DotOptions)\n" +
                                "dotOptions.center(latLng).color().radius(20).extraInfo(bundle)",
                        DotOverlayActivity.class),


                new ActivityInfo("线",
                        "mBaiduMap.addOverlay(PolylineOptions)\n" +
                                "options.width(10).color().points(mPoints)\n" +
                                "如果点多于2个，中间点的转折处不平滑，只有起点，终点的CAP是圆的\n" +
                                "解决方法：在转折处添加个圆点，但是内角还是不平滑",
                        PolyLineOverlayActivity.class),

                new ActivityInfo("弧",
                        "mBaiduMap.addOverlay(ArcOptions)\n" +
                                "arcOptions.color().extraInfo(bundle).points(point1, point2, point3).width(10);",
                        ArcOverlayActivity.class),

                new ActivityInfo("圆",
                        "mBaiduMap.addOverlay(CircleOptions)\n" +
                                "circleOptions.center(latLng).radius(500)//米" +
                                ".fillColor(color).stroke(new Stroke(width//pixel, strokeColor));",
                        CircleOverlayActivity.class),
                new ActivityInfo("Text",
                        "mBaiduMap.addOverlay(TextOptions)\n" +
                                "options.fontColor().fontSize().bgColor().align().position(latLng).rotate(30).typeface();\n" +
                                "align(h,v):TextOptions有常量，设置停靠点\n" +
                                "rotate(degree):逆时针旋转",
                        TextOverlayActivity.class),

        };

        mListView.setAdapter(new OverlayListAdapter());

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(OverlayListActivity.this, mDatas[position].clz));
            }
        });


    }

    private class OverlayListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDatas.length;
        }

        @Override
        public Object getItem(int position) {
            return mDatas[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(OverlayListActivity.this).inflate(R.layout.info_item, null);
            TextView title = (TextView) view.findViewById(R.id.title);
            TextView desc = (TextView) view.findViewById(R.id.desc);
            title.setText(mDatas[position].title);
            desc.setText(mDatas[position].desc);
            return view;
        }
    }

    static class ActivityInfo{
        private final String title;
        private final String desc;
        private final Class clz;
        public ActivityInfo(String title, String desc, Class clz){
            this.title = title;
            this.desc = desc;
            this.clz = clz;
        }
    }
}
