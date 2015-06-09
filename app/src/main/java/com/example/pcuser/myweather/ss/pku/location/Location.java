package com.example.pcuser.myweather.ss.pku.location;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by lyp on 2015/5/17.
 */
public class Location {
    private static LocationClient mLocationClient = null;
    private static BDLocationListener locListener = null;

    public Location() {
    }

    public static void init(Context ctx) {
        mLocationClient = new LocationClient(ctx);
        locListener = new MyLocationListener();
        mLocationClient.registerLocationListener(locListener);
    }

    public static void init(Context ctx, BDLocationListener listener) {
        mLocationClient = new LocationClient(ctx);
        locListener = listener;
        mLocationClient.registerLocationListener(locListener);
    }

    public static void startLocating() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);    //模式选择，高精度、省电、仅设备
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    public static void stopLocating() {
        mLocationClient.stop();
    }
}
