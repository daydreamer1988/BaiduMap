package com.austin.baidumap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.austin.baidumap.activities.LocationActivity;

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
            }

            startActivity(new Intent(MainActivity.this, clz));
        }
    }
}















