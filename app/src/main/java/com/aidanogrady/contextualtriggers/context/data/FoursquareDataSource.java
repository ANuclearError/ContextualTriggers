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
import java.util.ArrayList;
import java.util.List;


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

    public void sendResult(List<FoursquareVenue> venues) {

        Parcel parcel = Parcel.obtain();
        parcel.writeList(venues);
        parcel.setDataPosition(0);
        FoursquareResult result = FoursquareResult.CREATOR.createFromParcel(parcel);
        parcel.recycle();

        System.out.println("Foursquare result:");
        for (FoursquareVenue venue: venues) {
            System.out.println("\t" + venue);
        }

        Intent intent = new Intent(this, ContextUpdateManager.class);
        intent.putExtra("DataSource", "Foursquare");
        intent.putExtra("nearby", result);
        startService(intent);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Category info can be found here: https://developer.foursquare.com/categorytree
     **/

    private class FoursquareDataRequester implements Runnable {

        String latitude;
        String longitude;
        String client_id = "V5OSSBX1OM1NHPMXR5KOGZLFNX3UXWPHSUNRRVY2WLHBK05J";
        String client_secret = "QP0JSTHIPVDJLT4NP5RKPYI4OGTELJ2SUXSBMX0VLC4PT4NY";
        String version = "20170417";
        String outdoorCategory = "56aa371be4b08b9a8d57355e," +  // bike trail
                "52e81612bcbc57f1066b7a23," +                   // forest
                "4eb1d4d54b900d56c88a45fc," +                   // mountain
                "52e81612bcbc57f1066b7a21," +                   // national park
                "52e81612bcbc57f1066b7a13," +                   // nature preserve
                "4bf58dd8d48988d163941735," +                   // park
                "4bf58dd8d48988d159941735," +                   // trail
                "52e81612bcbc57f1066b7a22";                     // botanical garden

        String radius = "400";

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

                JSONObject jsonObject = new JSONObject(buffer.toString());
                handleResult(jsonObject);

            } catch (Exception e) {
                System.out.println("An exception happened: " + e);
                sendResult(null);
            }
        }

        private void handleResult(JSONObject result){
            try {

                List<FoursquareVenue> venueList = new ArrayList<>();

                JSONArray venueArray = result
                        .getJSONObject("response")
                        .getJSONArray("venues");

                for(int i = 0; i < venueArray.length(); i++) {
                    JSONObject venueObject = venueArray.getJSONObject(i);

                    String name = venueObject.getString("name");

                    int checkIns = venueObject
                            .getJSONObject("stats")
                            .getInt("checkinsCount");

                    String category = venueObject
                            .getJSONArray("categories")
                            .getJSONObject(0)
                            .getString("name");

                    venueList.add(new FoursquareVenue(name,category,checkIns));
                }

                sendResult(venueList);

            } catch (Exception e) {
                System.out.println("Fouresquare: An exception happened: " + e);
                sendResult(new ArrayList<FoursquareVenue>());
            }
        }
    }
}
