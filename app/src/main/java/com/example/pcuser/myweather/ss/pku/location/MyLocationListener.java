package com.example.pcuser.myweather.ss.pku.location;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

/**
 * Created by lyp on 2015/5/15.
 * 实现实时定位回调监听
 */
public class MyLocationListener implements BDLocationListener {

    @Override
    public void onReceiveLocation(BDLocation location) {
        //Receive Location
        StringBuffer sb = new StringBuffer(256);
        sb.append("city code: "); //返回的citycode只有三位数，是百度内部的城市编码code信息
        sb.append(location.getCityCode());
        sb.append("\ncity name: ");
        sb.append(location.getCity());
        sb.append("\nprovince: ");
        sb.append(location.getProvince());
        sb.append("\ndistrict: ");
        sb.append(location.getDistrict());
        //sb.append("\nresult code : ");
        //sb.append(location.getLocType());
        if (location.getLocType() == BDLocation.TypeGpsLocation) {
            sb.append("\ndirection : ");
            sb.append("\naddress : ");
            sb.append(location.getAddrStr());
            sb.append(location.getDirection());
        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
            sb.append("\naddress : ");
            sb.append(location.getAddrStr());
        }
        Log.i("Location ", sb.toString());
        Log.d("location", "locate finish ");
    }
}
