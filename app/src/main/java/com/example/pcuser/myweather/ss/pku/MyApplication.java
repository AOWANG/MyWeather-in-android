package com.example.pcuser.myweather.ss.pku;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import android.os.Environment;
import com.example.pcuser.myweather.ss.pku.cityList.CityBean;
import com.example.pcuser.myweather.ss.pku.cityList.CityDB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pcuser on 15/3/30.
 */
public class MyApplication extends Application {
    private CityDB mCityDB;
    private  static List<CityBean> mCityList;
    private static final String TAG="MyAPP";
    private static Application mApllication;

    private static ArrayList<String> mMainCityNames;
    private static ArrayList<String> mMainCityNumbers;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyApplication->oncreate");
        mApllication=this;

        mCityDB=openCityDB();
        initCityList();//从数据库加载城市列表

        loadMainCities();//从本地加载主城市列表

    }



    @Override
    public void onTerminate() {
        super.onTerminate();
        saveMainCities();//存储列表主城市
         }

    public static Application getInstance(){
        return mApllication;
    }
    private CityDB openCityDB(){
        String DatabasePathBase="/data"+ Environment.getDataDirectory().getAbsolutePath()
                + File.separator+getPackageName()+File.separator+"databases";
        String DatabasePathCity=DatabasePathBase+File.separator+CityDB.CITY_DB_NAME;
        File dbPath=new File(DatabasePathBase);
        File db=new File(DatabasePathCity);
        Log.d(TAG, DatabasePathBase);
        if(!dbPath.exists())
        {
            dbPath.mkdir();
        }
        if(!db.exists()){
            Log.i(TAG,"db is not exists");
            try {
                InputStream is=getAssets().open("city.db");
                FileOutputStream fos=new FileOutputStream(db);
                int len=-1;
                byte[] buffer=new byte[1024];
                while ((len=is.read(buffer))!=-1){
                    fos.write(buffer,0,len);
                    fos.flush();
                }
                fos.close();
                is.close();
            }catch (IOException e){
                e.printStackTrace();
                System.exit(0);
            }

        }
        return new CityDB(this,DatabasePathCity);
    }
    private void initCityList(){
        mCityList=new ArrayList<CityBean>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareCityList();
            }
        }).start();
    }
    private boolean prepareCityList(){
        mCityList=mCityDB.getAllCity();
        for(CityBean city:mCityList){
            String cityName=city.getCity();
            //Log.d(TAG,cityName);
        }
        return true;
    }

    public static List<CityBean> getmCityList() {
        return mCityList;
    }

    public static void addMainCity(String cityName,String cityID){
        //判断传入的城市是否是新的主城市，如果是存入本地主城市文件
        if (mMainCityNames==null){
            mMainCityNames=new ArrayList<String>();
            mMainCityNames.add(cityName);
            mMainCityNumbers=new ArrayList<String>();
            mMainCityNumbers.add(cityID);
        }else if(mMainCityNumbers.contains(cityID)==false){
            mMainCityNames.add(cityName);
            mMainCityNumbers.add(cityID);
        }
    }
    public static void removeMainCity(String cityID){
        int index=mMainCityNumbers.indexOf(cityID);
        mMainCityNames.remove(index);
        mMainCityNumbers.remove(index);

    }
    public static ArrayList getMainCityName(){
        return mMainCityNames;
    }
    public static ArrayList getMainCityNUmber(){
        return mMainCityNumbers;
    }
    //从本地文件读取之前存的添加的城市名和城市代码
    public void loadMainCities(){
        SharedPreferences sharedPreferences=getSharedPreferences("mainCityNameANDNumber",MODE_PRIVATE);
        String cityNames=sharedPreferences.getString("mainCityNames",null);
        String cityNumbers=sharedPreferences.getString("mainCityNumbers",null);
        if(cityNames==null){
            mMainCityNames=null;
            mMainCityNumbers=null;
            return;
        }
        mMainCityNames=new ArrayList<String> (Arrays.asList(cityNames.split(",")));
        mMainCityNumbers=new ArrayList<String> (Arrays.asList(cityNumbers.split(",")));
        return;
    }
    //把主城市名和ID存入本地
    public void  saveMainCities(){
        //把城市名和ID分别串起来存成一个String，城市之间用“，”隔开。
        if(mMainCityNames==null||mMainCityNumbers==null) return;
        SharedPreferences sharedPreferences=getSharedPreferences("mainCityNameANDNumber",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        String mainCityNames="";
        for(String item:mMainCityNames){
            mainCityNames+=",";
        }
        String mainCityNumbers="";
        for(String item:mMainCityNumbers){
            mainCityNumbers+=",";
        }
        editor.putString("mainCityNames",mainCityNames);
        editor.putString("mainCityNumbers",mainCityNames);
        editor.commit();
    }

}
