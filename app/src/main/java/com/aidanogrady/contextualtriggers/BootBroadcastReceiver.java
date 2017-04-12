package com.aidanogrady.contextualtriggers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aidanogrady.contextualtriggers.context.data.LocationDataSource;

/**
 * The BootBroadcastReceiver handles starting up the framework's services when the device is booted.
 *
 * @author Aidan O'Grady
 * @since 0.1
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("System booted");

        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
