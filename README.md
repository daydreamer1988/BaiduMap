# BaiduMap

1.avd不能测试定位功能

2.Authentication Error errorcode: 200 uid: -1 appid -1 msg: APP不存在，
  
    原因：AK配置问题。
    
3. 定位退出当前Activity时， 报ServiceConnectionLeaked问题：

    原因：在new LocationClient(Context context); 时，这里的context不能写成this, 应该写为getApplicationContext();
