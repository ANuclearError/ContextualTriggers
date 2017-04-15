package com.aidanogrady.contextualtriggers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.aidanogrady.contextualtriggers.context.data.LocationDataSource;
import com.aidanogrady.contextualtriggers.context.data.WeatherDataSource;

/**
 * Created by Kristine on 14/04/2017.
 */

public class ServiceInvoker extends BroadcastReceiver {

    private static final String TAG = "ServiceInvoker";

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("Received Alarm");
        Log.e(TAG, "received alarm");

        // invoke services to get data from sources

        Intent locationIntent = new Intent(context, LocationDataSource.class);
        context.startService(locationIntent);

        Intent weatherIntent = new Intent(context, WeatherDataSource.class);
        context.startService(weatherIntent);
    }
}
