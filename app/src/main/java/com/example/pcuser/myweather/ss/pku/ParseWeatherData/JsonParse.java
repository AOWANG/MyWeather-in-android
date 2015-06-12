package com.example.pcuser.myweather.ss.pku.parseWeatherData;

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
    public static CityInfoBean get(String cityName){
        final String address="http://www.pm25.in/api/querys/pm2_5.json?city="+cityName+"&token=5j1znBVAsnSf5xQyNQyq&stations=no";
        String responseStr=null;
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Reader reader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String str = null;
            StringBuffer sb = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str);
            }
            reader.close();
            connection.disconnect();
           responseStr=sb.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return responseStr;
    }
}
