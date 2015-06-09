package com.example.pcuser.myweather.ss.pku.cityList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pcuser on 15/3/30.
 */
public class CityDB {
    public static final String CITY_DB_NAME = "city.db";
    private static final String CITY_TABLE_NAME = "city";

    private SQLiteDatabase db;

    public CityDB(Context context, String path) {
        db = context.openOrCreateDatabase(CITY_DB_NAME, Context.MODE_PRIVATE, null);

    }

    public List<CityBean> getAllCity() {
        List<CityBean> list = new ArrayList<>();
        Cursor c = db.rawQuery("Select *from " + CITY_TABLE_NAME, null);
        while (c.moveToNext()) {
            String province = c.getString(c.getColumnIndex("province"));
            String city = c.getString(c.getColumnIndex("city"));
            String number = c.getString(c.getColumnIndex("number"));
            String allPY = c.getString(c.getColumnIndex("allpy"));
            String allFirstPY = c.getString(c.getColumnIndex("allfirstpy"));
            String firstPY = c.getString(c.getColumnIndex("firstpy"));
            CityBean item = new CityBean(province, city, number, firstPY, allPY, allFirstPY);
            list.add(item);
        }

        return list;
    }

    public String getCityNameByIndex(int index) {
        Cursor c = db.rawQuery("select city from " + CITY_TABLE_NAME + " where _id =  " + index, null);
        c.moveToFirst();
        String city = c.getString(c.getColumnIndex("city"));
        return city;
    }

    public String getCityIDByIndex(int index) {
        Cursor c = db.rawQuery("select * from " + CITY_TABLE_NAME + " where _id =  " + index, null);
        c.moveToFirst();
        String id = c.getString(c.getColumnIndex("number"));
        return id;
    }

    public String getCityIDByCityName(String name) {
        Cursor c = db.rawQuery("select * from " + CITY_TABLE_NAME + " where city = " + "\"" + name + "\"", null);
        c.moveToFirst();
        String id = c.getString(c.getColumnIndex("number"));
        return id;
    }

    public String getCityIDByCityAllPY(String allPY) {
        Cursor c = db.rawQuery("select * from " + CITY_TABLE_NAME + " where allpy = " + "\"" + allPY + "\"", null);
        c.moveToFirst();
        String id = c.getString(c.getColumnIndex("number"));
        return id;
    }

    public String getCityAllPY(int index) {
        Cursor c = db.rawQuery("select * from" + CITY_DB_NAME + " where _id = " + index, null);
        c.moveToFirst();
        String allPY = c.getString(c.getColumnIndex("allpy"));
        return allPY;
    }

    public String getCityAllFirstPY(int index) {
        Cursor c = db.rawQuery("select * from" + CITY_DB_NAME + " where _id = " + index, null);
        c.moveToFirst();
        String allFirstPY = c.getString(c.getColumnIndex("allFirstPY"));
        return allFirstPY;
    }

    public Cursor getCursorByIndex(int index) {
        Cursor c = db.rawQuery("select city from " + CITY_TABLE_NAME + " where _id =  " + index, null);
        c.moveToFirst();
        return c;
    }
}
