package com.austin.baidumap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.austin.baidumap.activities.BasicMap.BasicmapListActivity;
import com.austin.baidumap.activities.ClusterActivity;
import com.austin.baidumap.activities.LocationActivity;
import com.austin.baidumap.activities.Overlays.OverlayListActivity;
import com.austin.baidumap.activities.Searchs.SearchListActivity;
import com.austin.baidumap.activities.SimpleApp.SimpleApplicationListActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listView);

        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,getData()));

        mListView.setOnItemClickListener(new ItemClickListener());
    }

    private List<String> getData() {
        List<String> data = new ArrayList<>();
        data.add("基础定位");
        data.add("基础地图");
        data.add("点聚合");
        data.add("简单应用");
        data.add("Overlay:视图之外Overlay消失");
        data.add("检索");
        return data;
    }


    private class ItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Class<?> clz = null;

            switch (position) {
                case 0:
                    clz = LocationActivity.class;
                    break;
                case 1:
                    clz = BasicmapListActivity.class;
                    break;
                case 2:
                    clz = ClusterActivity.class;
                    break;
                case 3:
                    clz = SimpleApplicationListActivity.class;
                    break;
                case 4:
                    clz = OverlayListActivity.class;
                    break;

                case 5:
                    clz = SearchListActivity.class;
                    break;
                default:
                    break;
            }

            startActivity(new Intent(MainActivity.this, clz));
        }
    }
}















