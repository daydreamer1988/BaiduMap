package com.austin.baidumap.activities.BasicMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.austin.baidumap.R;
import com.baidu.mapapi.map.MapView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * 设置自定义的样式，亲测只能使用MapView,而不能使用TextureMapView
 * 在xml中或动态new出MapView都可以
 */
public class CustomMapActivity extends AppCompatActivity {
    private MapView mMapView;

    // 提供三种样式模板：
    // "custom_config_blue.txt"，
    // "custom_config_dark.txt"，
    // "custom_config_midnightblue.txt"
    private static String PATH = "custom_config_dark.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMapCustomFile(this, PATH);
        //1
//        mMapView = new MapView(this, new BaiduMapOptions());
//        setContentView(mMapView);
        //2
        setContentView(R.layout.activity_custom_map);
        mMapView = (MapView) findViewById(R.id.mapView);

        MapView.setMapCustomEnable(true);
    }

    private void setMapCustomFile(Context context, String PATH) {
        FileOutputStream out = null;
        InputStream inputStream = null;
        String moduleName = null;
        try {
            inputStream = context.getAssets()
                    .open("customConfigdir/" + PATH);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);

            moduleName = context.getFilesDir().getAbsolutePath();
            File f = new File(moduleName + "/" + PATH);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            out = new FileOutputStream(f);
            out.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MapView.setCustomMapStylePath(moduleName + "/" + PATH);
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
