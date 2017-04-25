package com.aidanogrady.contextualtriggers.context.data;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.support.annotation.Nullable;

import com.aidanogrady.contextualtriggers.ContextUpdateManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The OpenDataWeatherSource provides data on weather conditions using the OpenWeatherMap API.
 *
 * @author Calum Alexander-McGarry
 * @author Aidan O'Grady
 */
public class OpenWeatherDataSource extends IntentService {
    public static final String TAG = "weatherDS";

    public OpenWeatherDataSource() {
        super("OpenWeatherDataSource");
    }

    /**
     * Converts the given OpenWeatherMap id into a WeatherResult.
     *
     * @param id  the id to be converted
     * @return  the corresponding weather forecast.
     */
    public static WeatherForecast convertWeatherId(int id) {
        if (id >= 200 && id < 300)
            return WeatherForecast.THUNDERSTORM;
        if (id >= 300 && id < 400)
            return WeatherForecast.DRIZZLE;
        if (id >= 500 && id < 600)
            return WeatherForecast.RAIN;
        if (id >= 600 && id < 700)
            return WeatherForecast.SNOW;
        if (id == 800)
            return WeatherForecast.CLEAR;
        if (id > 800 && id < 900)
            return WeatherForecast.CLOUDY;
        return null;
    }

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

    private void sendResult(WeatherForecast forecast, double temp, double humidity, double wind) {
        Parcel parcel = Parcel.obtain();
        parcel.writeString(forecast.toString());
        parcel.writeDouble(temp);
        parcel.writeDouble(humidity);
        parcel.writeDouble(wind);
        parcel.setDataPosition(0);
        WeatherResult result = WeatherResult.CREATOR.createFromParcel(parcel);
        parcel.recycle();

        Intent intent = new Intent(this, ContextUpdateManager.class);
        intent.putExtra("DataSource", "Weather");
        intent.putExtra(WeatherResult.TAG, result);
        startService(intent);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class WeatherDataRequester implements Runnable {
        /**
         * AppID for accessing weather data. Shouldn't really be in here.
         */
        private static final String APP_ID = "72fea7c50a5622a959485e2731ce1f71";

        private static final String WEATHER_TAG = "weather";

        private static final String ID_TAG = "id";

        private static final String MAIN_TAG = "main";

        private static final String TEMPERATURE_TAG = "temp";

        private static final String HUMIDITY_TAG = "humidity";

        private static final String WIND_TAG = "wind";

        private static final String WIND_SPEED_TAG = "speed";

        /**
         * The mLatitude of where the request is being made for.
         */
        private String mLatitude;

        /**
         * The mLongitude of where the request is being made for.
         */
        private String mLongitude;


        WeatherDataRequester(String lat, String lng){
            mLatitude = lat;
            mLongitude = lng;
        }


        @Override
        public void run() {
            try {
                String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
                String PARAMS = "lat=" + mLatitude + "&lon=" + mLongitude +"&APPID=" + APP_ID;
                URL request = new URL(BASE_URL + PARAMS);

                HttpURLConnection weatherConnection = (HttpURLConnection) request.openConnection();
                weatherConnection.setRequestMethod("GET");
                weatherConnection.setDoInput(true);
                weatherConnection.setDoOutput(true);
                weatherConnection.connect();

                StringBuilder buffer = new StringBuilder();
                InputStream stream = weatherConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = br.readLine()) != null)
                    buffer.append(line).append("rn");

                stream.close();
                weatherConnection.disconnect();

                JSONObject jsonObject = new JSONObject(buffer.toString());
                handleResult(jsonObject);


            } catch (Exception e) {
                System.out.println("An exception happened: " + e);
            }
        }

        private void handleResult(JSONObject result) {
            try {
                JSONArray array = result.getJSONArray(WEATHER_TAG);
                int statusCode = (int) array.getJSONObject(0).get(ID_TAG);
                WeatherForecast forecast = OpenWeatherDataSource.convertWeatherId(statusCode);

                JSONObject main = result.getJSONObject(MAIN_TAG);
                double temperature = (double) main.get(TEMPERATURE_TAG);
                double humidity = (double) main.get(HUMIDITY_TAG);

                JSONObject wind = result.getJSONObject(WIND_TAG);
                double windSpeed = (double) wind.get(WIND_SPEED_TAG);

                if (forecast != null)
                    sendResult(forecast, temperature, humidity, windSpeed);
            } catch (Exception e) {
                System.out.println("An exception happened: " + e);
            }
        }
    }
}
