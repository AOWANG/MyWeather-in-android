package com.example.pcuser.myweather.ss.pku;

import android.app.Application;
import android.graphics.Path;
import android.os.Environment;
import android.util.Log;

import com.example.pcuser.myweather.ss.pku.cityList.CityBean;
import com.example.pcuser.myweather.ss.pku.cityList.CityDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pcuser on 15/3/30.
 */
public class MyApplication extends Application {
    private static  Application mApplication;



    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyApplicaiton","onCreate");
        mApplication = this;


    }
    public static Application getInstance(){
        return  mApplication;
    }


}
