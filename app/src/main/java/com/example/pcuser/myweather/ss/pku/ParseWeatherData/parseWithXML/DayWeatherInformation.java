package com.example.pcuser.myweather.ss.pku.parseWeatherData.parseWithXML;

/**
 * Created by pcuser on 15/3/23.
 */
public class DayWeatherInformation {
    public String date;
    public String high;
    public String low;
    public String dayType;
    public String dayFengxiang;
    public String dayFengli;
    public String nightType;
    public String nightFengxiang;
    public String nightFengli;

    public String getDate() {
        return date;
    }

    public String getLow() {

        return low.substring(low.indexOf(" ") + 1);
    }

    public String getHigh() {
        return high.substring(high.indexOf(" ") + 1);
    }

    public String getDayFengli() {
        return dayFengli.substring(0, dayFengli.length() - 1);
    }

    public String getNightFengli() {
        return nightFengli.substring(0, nightFengli.length() - 1);
    }
}
