package com.aidanogrady.contextualtriggers.context.data;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Calum on 12/04/2017.
 */

public class WeatherDataSource extends AsyncTask{

    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast?";
    private static String PARAMS = "q=Glasgow,UK&APPID=72fea7c50a5622a959485e2731ce1f71";

    HttpURLConnection weatherConnection = null;
    InputStream stream = null;

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            weatherConnection = (HttpURLConnection) (new URL(BASE_URL + PARAMS)).openConnection();
            weatherConnection.setRequestMethod("GET");
            weatherConnection.setDoInput(true);
            weatherConnection.setDoOutput(true);
            weatherConnection.connect();


            StringBuilder buffer = new StringBuilder();
            stream = weatherConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ( (line = br.readLine()) != null )
                buffer.append(line).append("rn");

            stream.close();
            weatherConnection.disconnect();

            JSONObject jsonObject = new JSONObject(buffer.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            String dateval = (String) jsonArray.getJSONObject(0).get("dt_txt");
            JSONArray weatherArray = jsonArray.getJSONObject(0).getJSONArray("weather");
            String weather_main = (String) weatherArray.getJSONObject(0).get("main");
            String weather_desc = (String) weatherArray.getJSONObject(0).get("description");

            System.out.println(dateval);
            System.out.println(weather_main);
            System.out.println(weather_desc);

            return null;

        }catch(Exception e){
            System.out.println("An exception happened: " + e);
            return null;
        }

    }


}
