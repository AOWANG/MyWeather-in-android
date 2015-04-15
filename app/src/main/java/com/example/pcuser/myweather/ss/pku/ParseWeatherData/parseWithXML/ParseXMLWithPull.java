package com.example.pcuser.myweather.ss.pku.parseWeatherData.parseWithXML;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by pcuser on 15/3/23.
 */
public class ParseXMLWithPull {
    private static final int DAYS_OF_WEATHER_INFORMATION = 5;
    public static TodayDetailInformation todayDetail = new TodayDetailInformation();

    public static ArrayList<DayWeatherInformation> weekWeatherInformation = new ArrayList<DayWeatherInformation>(DAYS_OF_WEATHER_INFORMATION);

    public static Boolean parseWeatherInformation(String xmlData) {
        weekWeatherInformation.clear();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    System.out.println("Start document");
                } else if(eventType == XmlPullParser.START_TAG) {
//                    System.out.println("Start tag "+xpp.getName());
                    if (xpp.getName().equals("city")){
                        eventType = xpp.next();
                        if (eventType == XmlPullParser.TEXT){
                            todayDetail.city = xpp.getText();
                        }
                    }else if (xpp.getName().equals("updatetime")){
                        eventType = xpp.next();
                        if (eventType == XmlPullParser.TEXT){
                            todayDetail.updateTime = xpp.getText();
                        }
                    }else if (xpp.getName().equals("wendu")){
                        eventType = xpp.next();
                        if (eventType == XmlPullParser.TEXT){
                            todayDetail.wendu = xpp.getText();
                        }
                    }else if (xpp.getName().equals("shidu")){
                        eventType = xpp.next();
                        if (eventType == XmlPullParser.TEXT){
                            todayDetail.shidu = xpp.getText();
                        }
                    }else if (xpp.getName().equals("pm25")){
                        eventType = xpp.next();
                        if (eventType == XmlPullParser.TEXT){
                            todayDetail.pm25 = xpp.getText();
                        }
                    }else if (xpp.getName().equals("quality")){
                        eventType = xpp.next();
                        if (eventType == XmlPullParser.TEXT){
                            todayDetail.quality = xpp.getText();
                        }
                    }else if (xpp.getName().equals("forecast")){
                        for (int i = 0; i < DAYS_OF_WEATHER_INFORMATION; i++){
                            DayWeatherInformation dayWeatherInformation = new DayWeatherInformation();
                            eventType = xpp.next();
                            eventType = xpp.next();

                            if (xpp.getName().equals("date")){
                                eventType = xpp.next();
                                dayWeatherInformation.date = xpp.getText();
                            }
                            eventType = xpp.next();
                            eventType = xpp.next();
                            if(xpp.getName().equals("high")){
                                eventType = xpp.next();
                                dayWeatherInformation.high = xpp.getText();
                            }
                            eventType = xpp.next();
                            eventType = xpp.next();
                            if (xpp.getName().equals("low")){
                                eventType = xpp.next();
                                dayWeatherInformation.low = xpp.getText();
                            }
                            eventType = xpp.next();
                            eventType = xpp.next();
                            eventType = xpp.next();
                            if (xpp.getName().equals("type")){
                                eventType = xpp.next();
                                dayWeatherInformation.dayType = xpp.getText();
                            }
                            eventType = xpp.next();
                            eventType = xpp.next();
                            if (xpp.getName().equals("fengxiang")){
                                eventType = xpp.next();
                                dayWeatherInformation.dayFengxiang = xpp.getText();
                            }
                            eventType = xpp.next();
                            eventType = xpp.next();
                            if (xpp.getName().equals("fengli")){
                                eventType = xpp.next();
                                dayWeatherInformation.dayFengli = xpp.getText();
                            }
                            eventType = xpp.next();
                            eventType = xpp.next();
                            eventType = xpp.next();
                            eventType = xpp.next();

                            if (xpp.getName().equals("type")){
                                eventType = xpp.next();
                                dayWeatherInformation.nightType = xpp.getText();
                            }
                            eventType = xpp.next();
                            eventType = xpp.next();
                            if (xpp.getName().equals("fengxiang")){
                                eventType = xpp.next();
                                dayWeatherInformation.nightFengxiang = xpp.getText();
                            }
                            eventType = xpp.next();
                            eventType = xpp.next();
                            if (xpp.getName().equals("fengli")){
                                eventType = xpp.next();
                                dayWeatherInformation.nightFengli = xpp.getText();
                            }
                            weekWeatherInformation.add(dayWeatherInformation);
                            eventType = xpp.next();
                            eventType = xpp.next();
                            eventType = xpp.next();
                        }
                    }else if(xpp.getName().equals("error")){
                        //error
                        return false;

                    }
                } else if(eventType == XmlPullParser.END_TAG) {
//                    System.out.println("End tag "+xpp.getName());
                } else if(eventType == XmlPullParser.TEXT) {
//                    System.out.println("Text "+xpp.getText());
                }
                eventType = xpp.next();
            }
            System.out.println("End document");
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return true;
    }

    private static void parseSevenDaysWeatherInformation(){

    }

}
