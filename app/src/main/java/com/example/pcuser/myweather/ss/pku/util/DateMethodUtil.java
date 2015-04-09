package com.example.pcuser.myweather.ss.pku.util;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by pcuser on 15/3/23.
 */
public class DateMethodUtil {
        public static String getTodayWeekNumber(){
            final Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
            if("1".equals(mWay)){
                mWay ="天";
            }else if("2".equals(mWay)){
                mWay ="一";
            }else if("3".equals(mWay)){
                mWay ="二";
            }else if("4".equals(mWay)){
                mWay ="三";
            }else if("5".equals(mWay)){
                mWay ="四";
            }else if("6".equals(mWay)){
                mWay ="五";
            }else if("7".equals(mWay)){
                mWay ="六";
            }
            return "星期" + mWay;
        }



}
