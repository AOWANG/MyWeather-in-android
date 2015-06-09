package com.example.pcuser.myweather.ss.pku.util;

import com.example.pcuser.myweather.R;
import com.example.pcuser.myweather.ss.pku.MyApplication;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by pcuser on 15/3/23.
 */
public class DateMethodUtil {
    public static String getTodayWeekNumber() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = MyApplication.getInstance().getString(R.string.sunday);
        } else if ("2".equals(mWay)) {
            mWay = MyApplication.getInstance().getString(R.string.monday);
        } else if ("3".equals(mWay)) {
            mWay = MyApplication.getInstance().getString(R.string.tuesday);
        } else if ("4".equals(mWay)) {
            mWay = MyApplication.getInstance().getString(R.string.wednesday);
        } else if ("5".equals(mWay)) {
            mWay = MyApplication.getInstance().getString(R.string.thursday);
        } else if ("6".equals(mWay)) {
            mWay = MyApplication.getInstance().getString(R.string.friday);
        } else if ("7".equals(mWay)) {
            mWay = MyApplication.getInstance().getString(R.string.saturday);
        }

        return MyApplication.getInstance().getString(R.string.today_week_number) + mWay;
    }


}
