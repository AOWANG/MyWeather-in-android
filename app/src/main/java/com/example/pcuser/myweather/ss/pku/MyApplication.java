package com.example.pcuser.myweather.ss.pku;

import android.app.Application;
import android.util.Log;

/**
 * Created by pcuser on 15/3/30.
 */
public class MyApplication extends Application {
    private static Application mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyApplicaiton", "onCreate");
        mApplication = this;
    }

    public static Application getInstance() {
        return mApplication;
    }


}
