package com.example.pcuser.myweather.ss.pku.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import com.example.pcuser.myweather.R;
import com.example.pcuser.myweather.ss.pku.MyApplication;

import android.content.Intent;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


import java.util.ArrayList;

public class MainCityActivity extends Activity implements View.OnClickListener {

    private ArrayList<String> mMainCityNames;
    private ArrayList<String> mMainCityNumbers;
    private ImageView mAddCity;
    private ImageView mMinusCity;
    ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_city_list);
        mAddCity=(ImageView)findViewById(R.id.addCity);
        mMinusCity=(ImageView)findViewById(R.id.minusCity);
        mAddCity.setOnClickListener(this);
        mMinusCity.setOnClickListener(this);
        mListView=(ListView)findViewById(R.id.mainCity_list);
        mMainCityNames=MyApplication.getMainCityName();
        mMainCityNumbers=MyApplication.getMainCityNUmber();
        if(mMainCityNames!=null){
            ArrayAdapter<String>adapter=new ArrayAdapter<String>(MainCityActivity.this,android.R.layout.simple_list_item_1,mMainCityNames);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {//若果某个主城市被点击，跳回到MainActivity更新数据
                    Intent intent = new Intent(MainCityActivity.this,MainActivity.class);
                    intent.putExtra("cityName",mMainCityNames.get(i));
                    intent.putExtra("cityNumber",mMainCityNumbers.get(i));
                    startActivity(intent);
                    //finish();
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_city, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addCity:
                Intent i=new Intent(MainCityActivity.this,CityListActivity.class);
                startActivity(i);
                break;
            case R.id.minusCity:
                break;
            default:break;
        }
    }
}