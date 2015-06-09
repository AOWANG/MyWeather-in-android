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

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.example.pcuser.myweather.R;
import com.example.pcuser.myweather.ss.pku.cityList.CityListActivity;
import com.example.pcuser.myweather.ss.pku.location.LocalCity;
import com.example.pcuser.myweather.ss.pku.location.Location;
import com.example.pcuser.myweather.ss.pku.parseWeatherData.parseWithXML.DayWeatherInformation;
import com.example.pcuser.myweather.ss.pku.parseWeatherData.parseWithXML.ParseXMLWithPull;
import com.example.pcuser.myweather.ss.pku.parseWeatherData.parseWithXML.TodayDetailInformation;
import com.example.pcuser.myweather.ss.pku.util.DateMethodUtil;
import com.example.pcuser.myweather.ss.pku.util.NetUtil;
import com.example.pcuser.myweather.ss.pku.util.PinYinUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    private static final int MSG_UPDATE_WEATHER_WITH_XML = 1;
    private static final int MSG_FAILURE = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progressBar.setVisibility(View.INVISIBLE);
            title_update_button.setVisibility(View.VISIBLE);
            switch (msg.what) {
                case MSG_UPDATE_WEATHER_WITH_XML:
                    Log.e("myWeather", "update UI weather interface with xml");

                    if (ParseXMLWithPull.parseWeatherInformation(msg.obj.toString())) {
                        UpdateWeatherUIByXMLInformation();
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.no_weather_info), Toast.LENGTH_SHORT).show();
                    }

                    break;
                case MSG_FAILURE:
                    Log.e("myWeather", "failure");
                    Toast.makeText(MainActivity.this, getString(R.string.no_network), Toast.LENGTH_LONG).show();
                    break;

                default:
                    Log.e("myWeather", "default");
            }
        }
    };

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
        Log.d("myWeather", "onCreate");

        getControls();
        getScreenMetrics();

        updateUIByLocalData();


        if (NetUtil.getNetConnectionState(MainActivity.this) == NetUtil.NET_CONNECTION_NONE) {
            Toast.makeText(MainActivity.this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = this.getIntent();
        String cityID = intent.getStringExtra("cityID");
        //更新数据
        if (cityID != null) {
            SharedPreferences settings = (SharedPreferences) getSharedPreferences("localWeatherInformation", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("cityID", cityID);
            editor.commit();

            DownloadWeatherInformationByXML downloadWeatherInformationByXML = new DownloadWeatherInformationByXML(cityID);

            progressBar.setVisibility(View.VISIBLE);
            title_update_button.setVisibility(View.INVISIBLE);
            downloadWeatherInformationByXML.start();
        } else {
            DownloadWeatherInformationByXML downloadWeatherInformationByXML = new DownloadWeatherInformationByXML();
            progressBar.setVisibility(View.VISIBLE);
            title_update_button.setVisibility(View.INVISIBLE);
            downloadWeatherInformationByXML.start();
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
                Intent intent = new Intent(MainActivity.this, CityListActivity.class);
                //  Intent intent=new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel://10086"));
                intent.putExtra("cityName", city_name.getText().toString());
                startActivity(intent);
            }
        });

        title_update_button = (ImageView) findViewById(R.id.title_update_btn);
        title_update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //download information thread
                DownloadWeatherInformationByXML downloadData = new DownloadWeatherInformationByXML();
                progressBar.setVisibility(View.VISIBLE);
                title_update_button.setVisibility(View.INVISIBLE);
                downloadData.start();
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

        editor.commit();
    }

    private void updateUIByLocalData() {
        SharedPreferences sharedPreferences = (SharedPreferences) getSharedPreferences("localWeatherInformation", MODE_PRIVATE);
        String city = sharedPreferences.getString("city", null);
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

            drawPicture(pm25, today_weather1, true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("mainActivity", "onDestroy()");
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

    private TodayDetailInformation todayDetailInformation;
    private DayWeatherInformation todayWeatherInformation;

    private void UpdateWeatherUIByXMLInformation() {

        todayDetailInformation = new TodayDetailInformation();
        todayWeatherInformation = new DayWeatherInformation();

        todayDetailInformation = ParseXMLWithPull.todayDetail;
        if (todayDetailInformation.pm25 == null) {
            todayDetailInformation.pm25 = "100";
            todayDetailInformation.quality = getString(R.string.no_data);
        }
        city_name.setText(todayDetailInformation.city);
        currentWendu.setText(getString(R.string.today_temperature) + todayDetailInformation.wendu + "℃");
        title_city_name.setText(todayDetailInformation.city + getString(R.string.title_city_name));
        issue_time.setText(todayDetailInformation.updateTime + getString(R.string.issue_time));
        humidity.setText(getString(R.string.humidity) + todayDetailInformation.shidu);
        pm25_value.setText(todayDetailInformation.pm25);
        air_quality.setText(todayDetailInformation.quality);

        todayWeatherInformation = ParseXMLWithPull.weekWeatherInformation.get(0);

        today_week_number.setText( DateMethodUtil.getTodayWeekNumber());

        today_temperature.setText(todayWeatherInformation.getLow() + "~" + todayWeatherInformation.getHigh());
        today_weather.setText(todayWeatherInformation.nightType);
        today_wind_power.setText(todayWeatherInformation.getNightFengli());


        drawPicture(todayDetailInformation.pm25.trim(), todayWeatherInformation.nightType, false);
    }

    private void drawPicture(String pm25, String weatherType, boolean isFromLocal) {

        int pmValue = Integer.parseInt(pm25);
        String pmImgStr = "0_50";
        if (pmValue > 50 && pmValue < 201) {
            int startV = (pmValue - 1) / 50 * 50 + 1;
            int endV = ((pmValue - 1) / 50 + 1) * 50;
            pmImgStr = Integer.toString(startV) + "_" + endV;
        } else if (pmValue >= 201 && pmValue < 301) {
            pmImgStr = "201_300";
        } else if (pmValue >= 301) {
            pmImgStr = "greater_300";
        }

        String typeImg = "biz_plugin_weather_" + PinYinUtil.converterToSpell(weatherType);
        Class aClass = R.drawable.class;
        int typeId = -1;
        int pmImgId = -1;
        try {
            //一般尽量采用这种形式
            Field pmField = aClass.getField("biz_plugin_weather_" + pmImgStr);
            Object pmImgO = pmField.get(new Integer(0));
            pmImgId = (int) pmImgO;

            Field field = aClass.getField(typeImg);
            Object value = field.get(new Integer(0));
            typeId = (int) value;

            if (!isFromLocal)
                Toast.makeText(MainActivity.this, getString(R.string.update_succeed), Toast.LENGTH_SHORT).show();
        } catch (Exception e) { //e.printStackTrace();
            if (-1 == typeId)
                typeId = R.drawable.biz_plugin_weather_qing;
            if (-1 == pmImgId)
                pmImgId = R.drawable.biz_plugin_weather_0_50;
            if (!isFromLocal)
                Toast.makeText(MainActivity.this, getString(R.string.no_picture), Toast.LENGTH_SHORT).show();
        } finally {
            Drawable drawable = getResources().getDrawable(typeId);
            weather_pic.setImageDrawable(drawable);
            drawable = getResources().getDrawable(pmImgId);
            pm25_pic.setImageDrawable(drawable);

        }
    }

    private class DownloadWeatherInformationByXML extends Thread {
        URL xmlUrl;

        DownloadWeatherInformationByXML() {
            try {
                String preUrl = "http://wthrcdn.etouch.cn/WeatherApi?citykey=";
                SharedPreferences sharedPreferences = (SharedPreferences) getSharedPreferences("localWeatherInformation", MODE_PRIVATE);
                String cityID = sharedPreferences.getString("cityID", "101010200");
                this.xmlUrl = new URL(preUrl + cityID);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        DownloadWeatherInformationByXML(String Url) {
            try {
                String preUrl = "http://wthrcdn.etouch.cn/WeatherApi?citykey=";
                this.xmlUrl = new URL(preUrl + Url);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }

        @Override
        public void run() {
            super.run();
            //user HttpURLConnection   get XML information
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) this.xmlUrl.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);
                InputStream inputStream = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder response = new StringBuilder();
                String str = "";
                while ((str = br.readLine()) != null) {
                    response.append(str);
                }
//                Log.e("myWeather",response.toString());
                Message msg = new Message();
                if (response != null) {
                    msg.what = MSG_UPDATE_WEATHER_WITH_XML;
                    msg.obj = response.toString();
                    handler.sendMessage(msg);
                } else {
                    msg.what = MSG_FAILURE;
                    msg.obj = null;
                    handler.sendMessage(msg);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

        }
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
            DownloadWeatherInformationByXML downloadData = new DownloadWeatherInformationByXML(cityID);
            downloadData.start();
        }
    }
}

