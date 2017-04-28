package com.austin.baidumap.activities.Searchs;

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

public class SearchListActivity extends AppCompatActivity {
    private ListView mListView;
    private ActivityInfo[] mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        mListView = (ListView) findViewById(R.id.listView);

        mDatas = new ActivityInfo[]{
            new ActivityInfo("地理编码",
                    "GeoCoder.newInstance()\n" +
                    "coder.setOnGetGeoCodeResultListener()\n" +
                    "GeoCodeOption.city().address()\n" +
                    "   coder.geocode(GeoCodeOption)\n"+
                    "ReverseGeoCodeOption.location()\n" +
                    "   coder.reverseGeoCode(ReverseGeoCodeOption)\n" +
                    "coder.destroy()",
                    GeocodeActivity.class),
            new ActivityInfo("Poi City检索",
                    "PoiSearch.newInstance()\n" +
                    "poiSearch.setOnGetPoiSearchResultListener()\n" +
                    "   onGetPoiResult()\n" +
                    "   onGetPoiDetailResult()\n"+
                    "PoiCitySearchOption.city().keyword()\n" +
                    "   .pageCapacity().pageNum(pageIndex)\n" +
                    "poiSearch.searchInCity(PoiCitySearchOption)\n" +
                    "poiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(poiInfo.uid))",
                    PoiCitySearchActivity.class),

            new ActivityInfo("Poi Nearby检索",
                    "PoiNearbySearchOption\n" +
                    "   .keyword().location(latLng).radius()\n" +
                    "   .pageCapacity(20).pageNum(pageIndex).sortType()\n" +
                    "poiSearch.searchNearby(option);\n\n" +
                    "PoiSortType.distance_from_near_to_far\n"+
                    "PoiSortType.comprehensive\n\n" +
                    "poiSearch.searchNearby(PoiNearbySearchOption)",
                    PoiNearbySearchActivity.class),

            new ActivityInfo("Poi Bound检索",
                    "貌似百度地图有BUG\n" +
                    "PoiBoundSearchOption.keyword().bound(LatLngBounds)\n" +
                    ".pageCapacity().pageNum(pageIndex);\n" +
                    "poiSearch.searchInBound(option);",
                    PoiBoundSearchActivity.class),

            new ActivityInfo("District 检索",
                    "DistrictSearch.newInstance()\n" +
                    "districtSearch.setOnDistrictSearchListener()\n" +
                    "districtSearchOption.cityName().districtName();\n" +
                    "districtSearch.searchDistrict(option);",
                    DistrictSearchActivity.class),

            new ActivityInfo("Suggestion 检索",
                    "SuggestionSearch.newInstance()\n" +
                    "suggestionSearch.setOnGetSuggestionResultListener()\n" +
                    "suggestionSearchOption.city(mCity).keyword()\n" +
                    "suggestionSearch.requestSuggestion(suggestionSearchOption);",
                    SuggestionSearchActivity.class),

        };

        mListView.setAdapter(new SearchListAdapter());

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(SearchListActivity.this, mDatas[position].clz));
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


    private class SearchListAdapter extends BaseAdapter {
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
            View view = LayoutInflater.from(SearchListActivity.this).inflate(R.layout.info_item, null);

            TextView title = (TextView) view.findViewById(R.id.title);
            TextView desc = (TextView) view.findViewById(R.id.desc);

            title.setText(mDatas[position].title);
            desc.setText(mDatas[position].desc);

            return view;
        }
    }
}
