package com.example.pcuser.myweather.ss.pku.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class CityInfoBean implements Serializable {
	
	public CityInfoBean(){
		cityInfo = new CityInfo();
		currentWeather = new CurrentWeather();
		sevenWeather = new SevenWeather();
		pm25 = new PM25();
		locPhoto = new LocPhoto();
	}
	
	
	public CityInfo cityInfo;
	public class CityInfo{
		public String cityID;
		public String cityPinyin;
		public String cityName;
		public String cityBelong;
		public String cityCountryEN;
		public String cityCountryCH;
		public String cityLon;
		public String cityLat;
	}
	
	public CurrentWeather currentWeather;
	public class CurrentWeather{
		public String upDate;
		public String temp;
		public String tempF;
		public String wd;
		public String sd;
		public String upTime;
		public String sm;
	}
	
	public SevenWeather sevenWeather;
	public class SevenWeather{
		public String updateTime;
		
		public Weather[] weathers;
		public SevenWeather(){
			weathers = new Weather[7];
			for (int i = 0; i < weathers.length; i++) {
				weathers[i] = new Weather();
			}
		}
		
		public class Weather{
			public String tianqi1;
			public String tianqi2;
			public String wendu1;
			public String wendu2;
			public String fengxiang1;
			public String fengxinag2;
			public String fengli1;
			public String fengli2;
			public String sunUpDown;
		}
	}
	
	public PM25 pm25;
	public class PM25{
		public String aqi;
		public String area;
		public String pm2_5;
		public String pm2_5_24h;
		public String quality;
		public String primary_pullutant;
		public String time_point;
		public String co;
		public String co_24h;
		public String no2;
		public String no2_24h;
		public String o3;
		public String o3_24h;
		public String pm10;
		public String pm10_24h;
		public String so2;
		public String so2_24h;
	}
	
	public LocPhoto locPhoto;
	public class LocPhoto{
		public int count;
		public ArrayList<String> photoURLs;
		public LocPhoto(){
			photoURLs = new ArrayList<>();
		}

	}
	
}
