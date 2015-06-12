package com.example.pcuser.myweather.ss.pku.main;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import android.os.Binder;

import com.example.pcuser.myweather.ss.pku.bean.CityInfoBean;
import com.example.pcuser.myweather.ss.pku.parseWeatherData.JsonParse;


public class GetWeatherService extends Service {
    public CityInfoBean cityInfoBean=new CityInfoBean();
    public String cityNumber;
    private final IBinder binder=new MyBinder();
    public class MyBinder extends Binder{
        GetWeatherService getService(){
            return GetWeatherService.this;
        }
    }

    public GetWeatherService() {}

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //cityInfoBean= JsonParse.getCityInfo(cityNumber);
        return START_STICKY;
    }
    public CityInfoBean getCityInfoBean(){
        cityInfoBean= JsonParse.getCityInfo(cityNumber);
        return cityInfoBean;
    }
}
