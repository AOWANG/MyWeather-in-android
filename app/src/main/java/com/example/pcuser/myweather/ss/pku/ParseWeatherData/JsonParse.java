package com.example.pcuser.myweather.ss.pku.parseWeatherData;

import android.os.StrictMode;

import com.google.gson.Gson;
import  com.example.pcuser.myweather.ss.pku.bean.CityInfoBean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Administrator on 2015/3/31.
 */

    public class JsonParse {
        public static CityInfoBean getCityInfo(String cityID){
            final String address="http://mobile100.zhangqx.com/WeatherDataCrawler/cityID/"+cityID+".json";
            String responseStr=null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                HttpURLConnection connection = null;

                StringBuffer response = new StringBuffer();

                URL url = new URL(address);

                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                InputStream inputStream = connection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));

                String string = "";
                while ((string = bReader.readLine())!=null) {
                    response.append(string);
                }
                connection.disconnect();
                responseStr=response.toString();
            }catch(Exception e){
                e.printStackTrace();
            }
            Gson gson=new Gson();
            CityInfoBean jsonWeather=gson.fromJson(responseStr,CityInfoBean.class);
            return jsonWeather;
        }
    }

