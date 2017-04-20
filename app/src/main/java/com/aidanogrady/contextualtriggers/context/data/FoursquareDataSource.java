package com.aidanogrady.contextualtriggers.context.data;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.aidanogrady.contextualtriggers.ContextUpdateManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class FoursquareDataSource extends IntentService {

    public static final String TAG = "foursquareDS";

    HttpURLConnection foursquareConnection = null;
    InputStream stream = null;

    public FoursquareDataSource() {
        super("FoursquareDataSource");
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

                FoursquareDataRequester requester = new FoursquareDataRequester(str_lat, str_lon);
                new Thread(requester).start();

            }
        }
        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }

    public void onSensorChanged(String nearby) {

        if(nearby == null){
            return;
        }

        Intent intent = new Intent(this, ContextUpdateManager.class);
        intent.putExtra("DataSource", "Foursquare");
        intent.putExtra("nearby", nearby);
        startService(intent);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class FoursquareDataRequester implements Runnable {

        String latitude;
        String longitude;
        String client_id = "V5OSSBX1OM1NHPMXR5KOGZLFNX3UXWPHSUNRRVY2WLHBK05J";
        String client_secret = "QP0JSTHIPVDJLT4NP5RKPYI4OGTELJ2SUXSBMX0VLC4PT4NY";
        String version = "20170417";
        String outdoorCategory = "56aa371be4b08b9a8d57355e," +
                "52e81612bcbc57f1066b7a23," +
                "4eb1d4d54b900d56c88a45fc," +
                "52e81612bcbc57f1066b7a21," +
                "52e81612bcbc57f1066b7a13," +
                "4bf58dd8d48988d163941735," +
                "4bf58dd8d48988d159941735," +
                "52e81612bcbc57f1066b7a22";

        String radius = "75";

        FoursquareDataRequester(String lat, String lng){
            latitude = lat;
            longitude = lng;
        }


        @Override
        public void run() {
            try {

                String BASE_URL = "https://api.foursquare.com/v2/venues/search?";
                String PARAMS = String.format(
                        "ll=%s,%s&client_id=%s&client_secret=%s&v=%s&categoryId=%s&radius=%s",
                        latitude,
                        longitude,
                        client_id,
                        client_secret,
                        version,
                        outdoorCategory,
                        radius
                );


                foursquareConnection = (HttpURLConnection) (new URL(BASE_URL + PARAMS)).openConnection();
                foursquareConnection.setRequestMethod("GET");
                foursquareConnection.setDoInput(true);
                foursquareConnection.setDoOutput(true);
                foursquareConnection.connect();


                StringBuilder buffer = new StringBuilder();
                stream = foursquareConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = br.readLine()) != null)
                    buffer.append(line).append("rn");

                stream.close();
                foursquareConnection.disconnect();

                String nearbyLocationsString = buffer.toString();
                System.out.println(nearbyLocationsString);
                onSensorChanged(nearbyLocationsString);

            } catch (Exception e) {
                System.out.println("An exception happened: " + e);
                onSensorChanged(null);
            }
        }
    }
}
