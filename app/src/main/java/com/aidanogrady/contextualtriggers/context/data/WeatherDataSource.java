package com.aidanogrady.contextualtriggers.context.data;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.aidanogrady.contextualtriggers.ContextUpdateManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherDataSource extends Service{

    HttpURLConnection weatherConnection = null;
    InputStream stream = null;

    public void requestWeatherData() {

        AsyncWeatherRequest request = new AsyncWeatherRequest();
        request.execute();
    }

    public void onSensorChanged(String main, String description) {

        Intent intent = new Intent(this, ContextUpdateManager.class);
        intent.putExtra("DataSource", "Weather");
        intent.putExtra("Main", main);
        intent.putExtra("Description", description);
        startService(intent);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class AsyncWeatherRequest extends AsyncTask<Object, Object, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            try {

                String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
                String PARAMS = "lat=55.8642&lon=4.2518&APPID=72fea7c50a5622a959485e2731ce1f71";

                weatherConnection = (HttpURLConnection) (new URL(BASE_URL + PARAMS)).openConnection();
                weatherConnection.setRequestMethod("GET");
                weatherConnection.setDoInput(true);
                weatherConnection.setDoOutput(true);
                weatherConnection.connect();


                StringBuilder buffer = new StringBuilder();
                stream = weatherConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = br.readLine()) != null)
                    buffer.append(line).append("rn");

                stream.close();
                weatherConnection.disconnect();

                JSONObject jsonObject = new JSONObject(buffer.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                String weather_main = (String) jsonArray.getJSONObject(0).get("main");
                String weather_desc = (String) jsonArray.getJSONObject(0).get("description");

                System.out.println(weather_main);
                System.out.println(weather_desc);

                onSensorChanged(weather_main, weather_desc);

            } catch (Exception e) {
                System.out.println("An exception happened: " + e);
                onSensorChanged(null, null);
            }
            return null;
        }
    }
}
