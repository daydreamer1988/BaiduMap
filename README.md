# BaiduMap

- 1.avd不能测试定位功能

-2.Authentication Error errorcode: 200 uid: -1 appid -1 msg: APP不存在，
  
    原因：AK配置问题。

-3.定位退出当前Activity时， 报ServiceConnectionLeaked问题：

    原因：在new LocationClient(Context context); 时，这里的context不能写成this, 应该写为getApplicationContext();

4.网络定位只能成功一次，之后的GPS也能定位成功，但是之后的网络定位都失败。

    原因：AK问题，确保在不同电脑使用的keystore是相同的, 或者在百度平台上再申请个同包名不同keystore的应用即可。

5.Gps定位成功后，无法再返回网络定位（从室外走到室内）。经调试，回调会定时得到GPS最后一次定位的结果，因为从BDLocation获得的时间，和其他数据前后是相同的。

    解决方法：
    
    1. 走到室外，用GPS定位
    
    2. client.stop()后再次client.start().即可再次网络定位。

6. 
LocationMode.Hight_Accuracy与LocationClientOption.LOC_SENSITIVITY_HIGHT对应

LocationMode.Battery_Saving与LocationClientOption.LOC_SENSITIVITY_MIDDLE对应

LocationMode.Device_Sensors与LocationClientOption.LOC_SENSITIVITY_LOW对应


7.setOpenAutoNotifyMode(int minTimeInterval, int minDistance, int locSensitivity)

设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者


/////////////////////////////////

8. 使用MapView切换Activity时，会出现短暂的黑屏现象

解决方法：使用TextureMapView,同时通过TextureMapView 的 setBackgroundColor()方法可以设置切换Activity的背景颜色。

注意：MapView通过以上方法设置背景色不起作用，还是短暂黑屏。


9.  设置自定义的样式，亲测只能使用MapView,而不能使用TextureMapView，不然不起作用

10. 交通图(zoom 7 scale100公里 以上才有效)
  
    城市热力图(zoom 11 scale10公里以上才有效)


11. 可以通过设置MapView的PaddingBottom为负数，来隐藏百度Logo


12. 发现设置padding后，有时不会立即更新，当mapview 状态发生变化（双击，移动等）后，padding生效。

    解决方法：addView() 随意加一个布局，使其调用MapView.onLayout()方法重绘。
    
              推荐使用 mBaiduMap.setPadding(left, top, right, bottom);
    
          
13.InfoWindow 在状态改变的时候显示一层，状态不改变的情况下，感觉是两个图片叠在了一起（两层）

    解决办法：在更新InfoWindow内容前，mBaiduMap.hideInfoWindow(); 并在更新后,mBaiduMap.showInfoWindow(infoWindow);
          
          或在状态更新时，让InfoWindow消失，状态结束后再显示。












