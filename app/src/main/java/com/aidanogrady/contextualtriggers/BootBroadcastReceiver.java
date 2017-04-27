package com.aidanogrady.contextualtriggers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * The BootBroadcastReceiver handles starting up the framework's services when the device is booted.
 *
 * @author Aidan O'Grady
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("ContextTriggers", "System booted");

        //Start up the MainActivity
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
