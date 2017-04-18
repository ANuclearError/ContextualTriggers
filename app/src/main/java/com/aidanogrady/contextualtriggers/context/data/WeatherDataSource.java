package com.aidanogrady.contextualtriggers.context.data;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
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


public class WeatherDataSource extends IntentService {

    public static final String TAG = "weatherDS";
    HttpURLConnection weatherConnection = null;
    InputStream stream = null;

    public WeatherDataSource() {
        super("WeatherDataSource");
    }

//    @Override
//    public void onCreate() {
//        super.onCreate();
//       // FoursquareDataRequester requester = new FoursquareDataRequester();
//       // new Thread(requester).start();
//        android.os.Debug.waitForDebugger();
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Bundle bundle;

        if (intent != null) {
            bundle = intent.getExtras();
        } else {
            bundle = null;
        }

        if (bundle != null) {

            if (intent.hasExtra("Latitude") && intent.hasExtra("Longitude")) {

                double latitude = intent.getDoubleExtra("Latitude", 0.0);
                double longitude = intent.getDoubleExtra("Longitude", 0.0);

                String str_lat = Double.toString(latitude);
                String str_lon = Double.toString(longitude);

                WeatherDataRequester requester = new WeatherDataRequester(str_lat, str_lon);
                new Thread(requester).start();

            }
        }
        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }

    public void onSensorChanged(String id) {

        Intent intent = new Intent(this, ContextUpdateManager.class);
        intent.putExtra("DataSource", "Weather");
        intent.putExtra("id", id);
        startService(intent);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class WeatherDataRequester implements Runnable {

        String latitude;
        String longitude;
        String app_id = "72fea7c50a5622a959485e2731ce1f71";

        WeatherDataRequester(String lat, String lng){
            latitude = lat;
            longitude = lng;
        }


        @Override
        public void run() {
            try {

                String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
                String PARAMS = "lat=" + latitude + "&lon=" + longitude +"&APPID=" + app_id;


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
                Integer weather_id_int = (Integer) jsonArray.getJSONObject(0).get("id");
                String weather_id = weather_id_int.toString();

                System.out.println(weather_id);

                onSensorChanged(weather_id);

            } catch (Exception e) {
                System.out.println("An exception happened: " + e);
                onSensorChanged(null);
            }
        }
    }
}
