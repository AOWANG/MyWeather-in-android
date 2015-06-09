package com.example.pcuser.myweather.ss.pku.cityList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pcuser.myweather.R;
import com.example.pcuser.myweather.ss.pku.main.MainActivity;
import com.example.pcuser.myweather.ss.pku.util.PinYinUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class CityListActivity extends Activity {
    private static final int MSG_HANDLE_CITYDB_FINISH = 0;
    private static final int MSG_HANDLE_SEARCH = 1;
    private static final int MSG_FAILURE = 10;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_HANDLE_CITYDB_FINISH:
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CityListActivity.this,
                            android.R.layout.simple_list_item_1, cityNameList);
                    cityListView.setAdapter(adapter);
                    cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String cityId = cityDB.getCityIDByIndex(position + 1);
                            Intent intent = new Intent(CityListActivity.this, MainActivity.class);
                            intent.putExtra("cityID", cityId);
                            startActivity(intent);
                        }
                    });

                    break;
                case MSG_HANDLE_SEARCH:

                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(CityListActivity.this,
                            android.R.layout.simple_list_item_1, cityNameListUsedBySearch);
                    cityListView.setAdapter(adapter2);
                    cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String cityName = cityNameListUsedBySearch.get(position);
                            String cityId = cityDB.getCityIDByCityName(cityName);
                            Intent intent = new Intent(CityListActivity.this, MainActivity.class);
                            intent.putExtra("cityID", cityId);
                            startActivity(intent);
                        }
                    });

                case MSG_FAILURE:
                    Log.e("myWeather", "failure");
                    // Toast.makeText(CityListActivity.this, "error", Toast.LENGTH_LONG).show();
                    break;

                default:
                    Log.e("myWeather", "default");
            }
        }
    };

    private ImageView back_btn;
    private ListView cityListView;
    private TextView titleNameText;
    private EditText search_editText;

    private CityDB cityDB;
    private List<CityBean> cityList;
    private List<String> cityNameList;

    private List<String> cityNameListUsedBySearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        cityListView = (ListView) findViewById(R.id.city_list);
//        cityListView.setBackgroundColor(Color.argb(255,244,238,235));
        if (cityDB == null) {
            cityDB = openCityDB();
            initCityList();
        }

        back_btn = (ImageView) findViewById(R.id.title_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        search_editText = (EditText) findViewById(R.id.search_editText);
        cityNameListUsedBySearch = new ArrayList<String>();
        search_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("on:" + s.toString());
                cityNameListUsedBySearch.clear();

                String pinyin = PinYinUtil.converterToSpell(s.toString()).toUpperCase();

                Iterator<CityBean> iterator = cityList.iterator();
                while (iterator.hasNext()) {
                    CityBean item = iterator.next();
                    if (item.getAllPY().contains(pinyin) || item.getAllFirstPY().contains(pinyin)) {
                        cityNameListUsedBySearch.add(item.city);
                    }
                }
                Message message = new Message();
                message.what = MSG_HANDLE_SEARCH;
                message.obj = null;
                handler.sendMessage(message);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        titleNameText = (TextView) findViewById(R.id.title_name);
        Intent intent = this.getIntent();
        String name = intent.getStringExtra("cityName");
        titleNameText.setText("当前区域： " + name);
    }

    private void initCityList() {
        cityList = new ArrayList<CityBean>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareCityList();

            }
        }).start();
    }

    private boolean prepareCityList() {
        cityNameList = new ArrayList<String>();

        cityList = cityDB.getAllCity();
        for (CityBean cityBean : cityList) {
            String cityName = cityBean.getCity();
            cityNameList.add(cityName);
        }

        Message msg = new Message();
        msg.what = MSG_HANDLE_CITYDB_FINISH;
        msg.obj = null;
        handler.sendMessage(msg);
        return true;
    }

    private CityDB openCityDB() {
        String databasePathBase = "/data" +
                Environment.getDataDirectory().getAbsolutePath()
                + File.separator + getPackageName()
                + File.separator + "databases";

        String databasePathCity = databasePathBase + File.separator + CityDB.CITY_DB_NAME;
        File dbPath = new File(databasePathBase);
        File db = new File(databasePathCity);
        if (!dbPath.exists()) {
            dbPath.mkdir();
        }
        if (!db.exists()) {
            try {
                InputStream is = getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new CityDB(this, databasePathCity);
    }
}
