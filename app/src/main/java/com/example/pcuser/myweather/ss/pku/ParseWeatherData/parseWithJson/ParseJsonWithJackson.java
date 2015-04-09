package com.example.pcuser.myweather.ss.pku.parseWeatherData.parseWithJson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by pcuser on 15/3/24.
 */
public class ParseJsonWithJackson {
    public static WeatherBean weatherBean;//= new WeatherBean();

    public static void parseWeatherInformation(String json){
        JsonFactory factory = new JsonFactory();

        try{
            JsonParser jsonParser = factory.createJsonParser(json);
            while (jsonParser.nextToken() != JsonToken.END_OBJECT){
                String fieldname = jsonParser.getCurrentName();
                switch (fieldname){
                    case "city":
                        weatherBean.city = jsonParser.getText();
                        break;
                    case "week":
                        weatherBean.week = jsonParser.getText();
                        break;

                    case "temp1":
                        weatherBean.temp1 = jsonParser.getText();
                        break;
                    case "temp2":
                        weatherBean.temp2 = jsonParser.getText();
                        break;
                    case "temp3":
                        weatherBean.temp3 = jsonParser.getText();
                        break;
                    case "temp4":
                        weatherBean.temp4 = jsonParser.getText();
                        break;
                    case "temp5":
                        weatherBean.temp5 = jsonParser.getText();
                        break;
                    case "temp6":
                        weatherBean.temp6 = jsonParser.getText();
                        break;

                    case "weather1":
                        weatherBean.weather1 = jsonParser.getText();
                        break;
                    case "weather2":
                        weatherBean.weather2 = jsonParser.getText();
                        break;
                    case "weather3":
                        weatherBean.weather3 = jsonParser.getText();
                        break;
                    case "weather4":
                        weatherBean.weather4 = jsonParser.getText();
                        break;
                    case "weather5":
                        weatherBean.weather5 = jsonParser.getText();
                        break;
                    case "weather6":
                        weatherBean.weather6 = jsonParser.getText();
                        break;

                    case "wind1":
                        weatherBean.wind1 = jsonParser.getText();
                        break;
                    case "wind2":
                        weatherBean.wind2 = jsonParser.getText();
                        break;
                    case "wind3":
                        weatherBean.wind3 = jsonParser.getText();
                        break;
                    case "wind4":
                        weatherBean.wind4 = jsonParser.getText();
                        break;
                    case "wind5":
                        weatherBean.wind5 = jsonParser.getText();
                        break;
                    case "wind6":
                        weatherBean.wind6 = jsonParser.getText();
                        break;

                    case "fx1":
                        weatherBean.fx1 = jsonParser.getText();
                        break;
                    case "fx2":
                        weatherBean.fx2 = jsonParser.getText();
                        break;


                    case "fl1":
                        weatherBean.fl1 = jsonParser.getText();
                        break;
                    case "fl2":
                        weatherBean.fl2 = jsonParser.getText();
                        break;
                    case "fl3":
                        weatherBean.fl3 = jsonParser.getText();
                        break;
                    case "fl4":
                        weatherBean.fl4 = jsonParser.getText();
                        break;
                    case "fl5":
                        weatherBean.fl5 = jsonParser.getText();
                        break;
                    case "fl6":
                        weatherBean.fl6 = jsonParser.getText();
                        break;

                    default:
                        break;
                }
                jsonParser.nextToken();
            }


        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

}
