package com.example.pcuser.myweather.ss.pku.main;

import android.os.Bundle;

import com.example.pcuser.myweather.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.pcuser.myweather.ss.pku.cityList.CityBean;
import com.example.pcuser.myweather.ss.pku.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2015/3/29.
 */
public class CityListActivity extends Activity implements  View.OnClickListener{
    private ImageView mBackBtn;
    private ListView mListView;
    private EditText mEditText;
    private List<CityBean> mAllCity;
    private static final int MSG_HANDLE_SEARCH=2;
    private static final int MSG_HANDLE_LOADALLCITY=1;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_city_list);

        prepareList();
        mEditText=(EditText)findViewById(R.id.search_edit);
        mEditText.addTextChangedListener(mTextWatcher);
        mBackBtn=(ImageView)findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);


        Log.d("SelectCity", "onStart");
    }
    TextWatcher mTextWatcher=new TextWatcher() {
        private int editStart;
        private int editEnd;
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            String tempString=charSequence.toString().toUpperCase();
            ArrayList<Map<String,String>> cityInfoBySearch = new ArrayList<Map<String,String>>();
            for(CityBean city:mAllCity){
                Map<String,String> city_info=new HashMap<String, String>();
                if(city.getNumber().startsWith(tempString)||city.getCity().startsWith(tempString)||
                        city.getAllFirstPY().startsWith(tempString)||city.getAllPY().startsWith(tempString)){
                    city_info.put("city_name",city.getCity());
                    city_info.put("city_number",city.getNumber());
                    cityInfoBySearch.add(city_info);
                }
            }
            Message message=new Message();
            message.what=MSG_HANDLE_SEARCH;
            message.obj=cityInfoBySearch;
            mHandler.sendMessage(message);

        }

        @Override
        public void afterTextChanged(Editable editable) {
            editStart=mEditText.getSelectionStart();
            editEnd=mEditText.getSelectionEnd();
            if(editable.length()>20){
                editable.delete(editStart-1,editEnd);
                int tempSelection=editStart;
                mEditText.setText(editable);
                mEditText.setSelection(tempSelection);
            }
            if(editable==null){
                getAllCity();
            }

        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("SelectCity", "onResume");
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
            default:break;
        }
    }

    public boolean getAllCity(){

        ArrayList<Map<String,String>> cityInfo = new ArrayList<Map<String,String>>();
        for(CityBean city:mAllCity){
            Map<String,String> city_info=new HashMap<String, String>();
            city_info.put("city_name",city.getCity());
            city_info.put("city_number",city.getNumber());
            cityInfo.add(city_info);
        }
        Message message=new Message();
        message.what=MSG_HANDLE_LOADALLCITY;
        message.obj=cityInfo;
        mHandler.sendMessage(message);
        return true;
    }


    private android.os.Handler mHandler=new android.os.Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_HANDLE_SEARCH:
                case MSG_HANDLE_LOADALLCITY:
                    final List<Map<String,String>> cityInfo=(List<Map<String,String>>)msg.obj;
                    SimpleAdapter simpleAdapter=new SimpleAdapter(CityListActivity.this,cityInfo,
                            R.layout.city,new String[]{"city_name"},new int[]{R.id.item_city_name});
                    mListView.setAdapter(simpleAdapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(CityListActivity.this,MainActivity.class);
                            intent.putExtra("cityName",cityInfo.get(i).get("city_name"));
                            intent.putExtra("cityNumber", cityInfo.get(i).get("city_number"));
                            startActivity(intent);
                            //finish();
                        }
                    });break;
                default:
                    break;
            }
        }
    };

    private boolean prepareList(){//在程序启动时在Application中加载完 城市数据库

        mAllCity=MyApplication.getmCityList();
        mListView=(ListView)findViewById(R.id.all_city);
        getAllCity();
        return true;
    }
}
