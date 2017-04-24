package com.austin.baidumap.activities.SimpleApp;

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

public class SimpleApplicationListActivity extends AppCompatActivity {
    private ListView mListView;
    private ActivityInfo[] mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_application_list);

        mListView = (ListView) findViewById(R.id.listView);

        mDatas = new ActivityInfo[]{
                new ActivityInfo("动态展开布局",
                        "地图MapStatus.target使终保持在地图中心",
                        Application1Activity.class),

                new ActivityInfo("移动Zoom效果",
                        "设置地图ZoomOut ZoomIn动画",
                        Application2Activity.class),

        };

        mListView.setAdapter(new MyBaseAdapter());

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivityInfo mData = mDatas[position];
                startActivity(new Intent(SimpleApplicationListActivity.this, mData.clz));
            }
        });

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

    private class MyBaseAdapter extends BaseAdapter {
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
            ActivityInfo item = (ActivityInfo) getItem(position);

            View inflate = LayoutInflater.from(SimpleApplicationListActivity.this).inflate(R.layout.info_item, null);

            TextView title = (TextView) inflate.findViewById(R.id.title);
            TextView desc = (TextView) inflate.findViewById(R.id.desc);

            title.setText(item.title);
            desc.setText(item.desc);

            return inflate;
        }
    }
}
