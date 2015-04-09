package com.example.pcuser.myweather.ss.pku.cityList;

/**
 * Created by pcuser on 15/3/30.
 */
public class CityBean {
    public String province;
    public String city;
    private String number;
    private String firstPY;
    private String allPY;
    private String allFirstPY;

    public CityBean(String province,String city,String number,String firstPY,String allPY,String allFirstPY){
        this.province = province;
        this.city = city;
        this.number = number;
        this.firstPY = firstPY;
        this.allPY = allPY;
        this.allFirstPY = allFirstPY;
    }

    public String getCity(){
        return this.city;
    }

    public String getProvince(){
           return this.province;
    }

    public String getAllPY() {
        return this.allPY;
    }

    public String getAllFirstPY(){
        return  this.allFirstPY;
    }



}
