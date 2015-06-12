package com.example.pcuser.myweather.ss.pku.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.pcuser.myweather.ss.pku.MyApplication;
import com.example.pcuser.myweather.ss.pku.bean.TrendView;
import com.example.pcuser.myweather.ss.pku.parseWeatherData.JsonParse;
import com.example.pcuser.myweather.ss.pku.util.NetUtil;
import com.example.pcuser.myweather.ss.pku.util.PinYinUtil;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.example.pcuser.myweather.R;
import com.example.pcuser.myweather.ss.pku.location.LocalCity;
import com.example.pcuser.myweather.ss.pku.location.Location;
import com.example.pcuser.myweather.ss.pku.bean.CityInfoBean;


public class MainActivity extends Activity {
    private static final int MSG_UPDATE_WEATHER_WITH_XML = 1;
    private static final int MSG_FAILURE = 0;

    GetWeatherService serviceBinder;
    Intent i;
    boolean lack_weatherinfo=false;//获得cityInfoBean数据是否完整的标记
    private String mCityNumber;//current city ID
    private CityInfoBean mCityInfoBean;
    private TrendView sevenWeatherView;//七天折线图

    private TextView city_name;
    private TextView currentWendu;
    private TextView title_city_name;
    private TextView issue_time;
    private TextView humidity;
    private TextView pm25_value;
    private TextView air_quality;

    private TextView today_week_number;
    private TextView today_temperature;
    private TextView today_weather;
    private TextView today_wind_power;

    private ImageView title_city_manager;

    private ImageView title_update_button;

    private ImageView pm25_pic;
    private ImageView weather_pic;

    private ProgressBar progressBar;

    //定位图标
    private ImageView location;

    private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(
                ComponentName className, IBinder service) {
            //—-called when the connection is made—-
            serviceBinder = ((GetWeatherService.MyBinder)service).getService();
            serviceBinder.cityNumber= mCityNumber;
            startService(i);
        }
        public void onServiceDisconnected(ComponentName className) {
            //---called when the service disconnects---
            serviceBinder = null;
        }
    };

    //绑定service，并获取城市天气数据
    public CityInfoBean getWeatherFromService() {
        i = new Intent(MainActivity.this, GetWeatherService.class);
        bindService(i, connection, Context.BIND_AUTO_CREATE);
        return JsonParse.getCityInfo(mCityNumber);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
        Log.d("myWeather", "onCreate");

        getControls();//给控件绑定句柄
        getScreenMetrics();
        updateUIByLocalData();//更新上次更新的城市信息并显示


        if (NetUtil.getNetConnectionState(MainActivity.this) == NetUtil.NET_CONNECTION_NONE) {
            Toast.makeText(MainActivity.this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
            return;
        }
        // 绑定services
        i = new Intent(MainActivity.this, GetWeatherService.class);
        bindService(i, connection, Context.BIND_AUTO_CREATE);
        //从主城市Activity 或者 城市列表的Activity 传回城市名和ID
        Intent intent = this.getIntent();
        String cityName=intent.getStringExtra("cityName");
        String cityNumber = intent.getStringExtra("cityNumber");
        mCityNumber=cityNumber;
        //更新数据
        if (cityNumber!= null) {
            SharedPreferences settings = (SharedPreferences) getSharedPreferences("localWeatherInformation", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("cityNumber", cityNumber);
            editor.commit();
            mCityNumber=cityNumber;
            //从service获得天气数据存入mCityInfoBean中
            mCityInfoBean= JsonParse.getCityInfo(mCityNumber);
            if(mCityInfoBean==null) return;
            progressBar.setVisibility(View.VISIBLE);
            title_update_button.setVisibility(View.INVISIBLE);

            MyApplication.addMainCity(cityName,cityNumber);

            //更新所有控件的内容
            updateAllViews();

        } else {
            progressBar.setVisibility(View.VISIBLE);
            title_update_button.setVisibility(View.INVISIBLE);
        }


    }

    private void getScreenMetrics() {
        // 获取屏幕密度
        DisplayMetrics dm;
        dm = getResources().getDisplayMetrics();

        float density = dm.density;
        int densityDPI = dm.densityDpi;
        Log.e("DisplayMetrics", "density=" + density + "; densityDPI=" + densityDPI);

        int widthPixels = dm.widthPixels;
        int heightPixels = dm.heightPixels;
        Log.e("resolution", "widthPixels=" + widthPixels + "; heightPixels=" + heightPixels);

        int screenWidth = (int) (widthPixels * density + 0.5f);
        int screenHeight = (int) (heightPixels * density + 0.5f);
        Log.e("DisplayMetrics", "screenWidth=" + screenWidth + "; screenHeight=" + screenHeight);

        float ydpi = dm.ydpi;
        float xdpi = dm.xdpi;
        Log.e("mainActivity", "100dp 长 " + 100 * density / densityDPI + "英寸");
    }

    private void getControls() {
        //give control to various
        city_name = (TextView) findViewById(R.id.city_name);
        currentWendu = (TextView) findViewById(R.id.currentWendu);
        title_city_name = (TextView) findViewById(R.id.title_city_name);
        issue_time = (TextView) findViewById(R.id.issue_time);
        humidity = (TextView) findViewById(R.id.humidity);
        pm25_value = (TextView) findViewById(R.id.pm2_5_value);
        air_quality = (TextView) findViewById(R.id.air_quality);

        today_week_number = (TextView) findViewById(R.id.today_week_number);
        today_temperature = (TextView) findViewById(R.id.today_temperature);
        today_weather = (TextView) findViewById(R.id.today_weather);
        today_wind_power = (TextView) findViewById(R.id.today_wind_power);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        title_city_manager = (ImageView) findViewById(R.id.title_city_manager);
        title_city_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainCityActivity.class);
                //  Intent intent=new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel://10086"));
                intent.putExtra("cityName", city_name.getText().toString());
                startActivity(intent);
            }
        });

        title_update_button = (ImageView) findViewById(R.id.title_update_btn);
        title_update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCityInfoBean=JsonParse.getCityInfo(mCityNumber);
            }
        });

        pm25_pic = (ImageView) findViewById(R.id.pm2_5_pic);
        weather_pic = (ImageView) findViewById(R.id.weather_pic);

        /*lyp: start 定位*/
        location = (ImageView) findViewById(R.id.title_location);
        Location.init(this, new MyLocationListener());
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location.startLocating();
            }
        });
        /*lyp: end of 定位*/

        //七天数据图
        int screenWidth  = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        sevenWeatherView = (TrendView) findViewById(R.id.trendView);
        sevenWeatherView.setWidthHeight(screenWidth, screenHeight);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("mainActivity", "onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v("mainActivity", "onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("mainActivity", "onResume()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("mainActivity", "onStop()");
        //停止定位
        Location.stopLocating();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("mainActivity", "onPause()");

        SharedPreferences settings = (SharedPreferences) getSharedPreferences("localWeatherInformation", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString("city", city_name.getText().toString());
        editor.putString("cityID",mCityNumber);
        editor.putString("title_city_name", title_city_name.getText().toString());
        editor.putString("updateTime", issue_time.getText().toString());
        editor.putString("wendu", currentWendu.getText().toString());
        editor.putString("shidu", humidity.getText().toString());
        editor.putString("pm25", pm25_value.getText().toString());
        editor.putString("quality", air_quality.getText().toString());

        editor.putString("today_date", today_week_number.getText().toString());
        editor.putString("today_temperature", today_temperature.getText().toString());
        editor.putString("today_weather", today_weather.getText().toString());
        editor.putString("today_wind_power", today_wind_power.getText().toString());

        //保存七天数据，在数据不缺失情况
        if(lack_weatherinfo==false){
            String topTempString="";
            String lowTempString="";
            String dayWeatherString="";
            String nightWeatherString="";
            if(mCityInfoBean!=null){
                CityInfoBean.SevenWeather.Weather []sevenWeathers =mCityInfoBean.sevenWeather.weathers;
                for(int i=0;i<7;++i){
                    topTempString+=sevenWeathers[i].wendu1+",";
                    lowTempString+=sevenWeathers[i].wendu2+",";
                    dayWeatherString+=sevenWeathers[i].tianqi1.substring(0,2)+",";
                    nightWeatherString+=sevenWeathers[i].tianqi2.substring(0,2)+",";
                }
            }

            editor.putString("seven_topTemp",topTempString);
            editor.putString("seven_lowTemp",lowTempString);
            editor.putString("seven_dayWeather",dayWeatherString);
            editor.putString("seven_nightWeather",nightWeatherString);
        }

        editor.commit();
        saveMainCities();


    }

    private void updateUIByLocalData() {
        SharedPreferences sharedPreferences = (SharedPreferences) getSharedPreferences("localWeatherInformation", MODE_PRIVATE);
        String city = sharedPreferences.getString("city", null);
        String cityID=sharedPreferences.getString("cityID",null);
        String title_city_name1 = sharedPreferences.getString("title_city_name", null);
        String updateTime = sharedPreferences.getString("updateTime", null);
        String wendu = sharedPreferences.getString("wendu", null);
        String shidu = sharedPreferences.getString("shidu", null);
        String pm25 = sharedPreferences.getString("pm25", null);
        String quality = sharedPreferences.getString("quality", null);

        String today_date = sharedPreferences.getString("today_date", null);
        String today_temperature1 = sharedPreferences.getString("today_temperature", null);
        String today_weather1 = sharedPreferences.getString("today_weather", null);
        String today_wind_power1 = sharedPreferences.getString("today_wind_power", null);

        if (city != null) {
            title_city_name.setText(title_city_name1);
            city_name.setText(city);
            issue_time.setText(updateTime);
            currentWendu.setText(wendu);
            humidity.setText(shidu);
            pm25_value.setText(pm25);
            air_quality.setText(quality);
            today_week_number.setText(today_date);
            today_temperature.setText(today_temperature1);
            today_weather.setText(today_weather1);
            today_wind_power.setText(today_wind_power1);
        }
        mCityNumber=cityID;
        updateSevenDataByLocal();
//

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("mainActivity", "onDestroy()");
        //存入设置城市
        MyApplication myApplication=new MyApplication();
        myApplication.saveMainCities();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v("mainActivity", "onSaveInstanceState(outState)");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.v("mainActivity", "onBackPressed()");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v("mainActivity", "onRestoreInstanceState");

    }


    //把主城市名和ID存入本地
    public void  saveMainCities(){
        //把城市名和ID分别串起来存成一个String，城市之间用“，”隔开。
        List<String>mainCityNames =MyApplication.getMainCityName();
        List<String>mainCityNumbers =MyApplication.getMainCityNUmber();
        if(mainCityNames==null||mainCityNumbers==null) return;
        SharedPreferences sharedPreferences=getSharedPreferences("mainCityNameANDNumber",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        String mainCityNameString="";
        for(String item:mainCityNames){

            mainCityNameString+=",";
        }
        String mainCityNumberString="";
        for(String item:mainCityNumbers){
            mainCityNumberString+=",";
        }
        editor.putString("mainCityNames",mainCityNameString);
        editor.putString("mainCityNumbers",mainCityNameString);
        editor.commit();
    }


    /**
     * Created by lyp on 2015/5/15.
     * 实现定位回调监听
     */
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            String cityID = LocalCity.getCityIdByName(LocalCity.getLocalCityName(location));
            Log.d("location", "locate finish ");
            Log.d("City ID", cityID);
            mCityNumber=cityID;
            getWeatherFromService();
            updateAllViews();
        }
    }



    //给折线图更新数据展示
    public void updateSevenWeatherView(){
        CityInfoBean.SevenWeather.Weather []sevenWeathers =mCityInfoBean.sevenWeather.weathers;
        List<Integer> topTempList=new ArrayList<>();
        List<Integer> lowTempList=new ArrayList<Integer>();
        List<String>dayWeatherList=new ArrayList<String>();
        List<String>nightWeatherList=new ArrayList<String>();
        lack_weatherinfo=false;
        for(int i=0;i<7;++i){
            if(sevenWeathers[i].wendu1==null||sevenWeathers[i].wendu2==null||sevenWeathers[i].tianqi1==null||sevenWeathers[i].tianqi2==null){
                updateSevenDataByLocal();
                lack_weatherinfo=true;
                return;
            }
            topTempList.add(Integer.valueOf(sevenWeathers[i].wendu1));
            lowTempList.add(Integer.valueOf(sevenWeathers[i].wendu2));
            dayWeatherList.add(sevenWeathers[i].tianqi1.substring(0,2));
            nightWeatherList.add(sevenWeathers[i].tianqi2.substring(0,2));
        }
        sevenWeatherView.setTemperature(topTempList,lowTempList);
        sevenWeatherView.setBitmap(dayWeatherList, nightWeatherList);
    }
    private void updateSevenDataByLocal(){
        //从本地读出七天天气信息
        SharedPreferences sharedPreferences= (SharedPreferences) getSharedPreferences("localWeatherInformation", MODE_PRIVATE);
        String topTempString=sharedPreferences.getString("seven_topTemp",null);
        String lowTempString=sharedPreferences.getString("seven_lowTemp",null);
        String dayWeatherString=sharedPreferences.getString("seven_dayWeather",null);
        String nightWeatherString=sharedPreferences.getString("seven_nightWeather",null);
        if(topTempString==null||topTempString=="") return ;
        List<String> topTempListString=new ArrayList<String>(Arrays.asList(topTempString.split(",")));
        List<String> lowTempListString=new ArrayList<String >(Arrays.asList(lowTempString.split(",")));
        List<String>dayWeatherList=new ArrayList<String>(Arrays.asList(dayWeatherString.split(",")));
        List<String>nightWeatherList=new ArrayList<String>(Arrays.asList(nightWeatherString.split(",")));
        List<Integer> topTempList=new ArrayList<Integer>() ;
        List<Integer> lowTempList=new ArrayList<Integer>() ;
        for(int i=0;i<7;++i){
            topTempList.add(Integer.valueOf(topTempListString.get(i)));
            lowTempList.add(Integer.valueOf(lowTempListString.get(i)));
        }
        sevenWeatherView.setTemperature(topTempList,lowTempList);
        sevenWeatherView.setBitmap(dayWeatherList, nightWeatherList);
    }

    //更新所有控件内容
    public void updateAllViews(){
        if(mCityInfoBean==null) return;
        title_city_name.setText(mCityInfoBean.cityInfo.cityName);
        city_name.setText(mCityInfoBean.cityInfo.cityName);
        issue_time.setText(mCityInfoBean.currentWeather.upTime);
        currentWendu.setText(mCityInfoBean.currentWeather.wd);
        humidity.setText(mCityInfoBean.currentWeather.sd);
        pm25_value.setText(mCityInfoBean.pm25.aqi);
        air_quality.setText(mCityInfoBean.pm25.quality);
        //today_week_number.setText(mCityInfoBean.currentWeather.);
        today_temperature.setText(mCityInfoBean.currentWeather.temp);
        today_weather.setText(mCityInfoBean.sevenWeather.weathers[0].tianqi1);
        today_wind_power.setText(mCityInfoBean.sevenWeather.weathers[0].fengli1);
        updateSevenWeatherView();//更新折线图控件
    }

}

