# BaiduMap

1.avd不能测试定位功能

2.Authentication Error errorcode: 200 uid: -1 appid -1 msg: APP不存在，
  
    原因：AK配置问题。

3.定位退出当前Activity时， 报ServiceConnectionLeaked问题：

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


7.


























