package com.aidanogrady.contextualtriggers.context.data;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.aidanogrady.contextualtriggers.ContextUpdateManager;

/**
 * Created by ASUS on 14/04/2017.
 */

public class StepCounter extends Service implements SensorEventListener {
    private static final String TAG = "StepCounter";

    /**
     * The number of steps to be walked for the counter to send an intent.
     */
    private static final int THRESHOLD = 50;

    /**
     * The total number of steps walked since booting.
     */
    private int mTotalStepsSinceBoot;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.e(TAG, "In onStartCommand");
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
            Log.e(TAG, "successfully registered");
        } else {
           // Log.e(TAG, "no step counter in the device");
            Intent i = new Intent(this, ContextUpdateManager.class);
            intent.putExtra("DataSource", "Steps");
            intent.putExtra("Count", -1);
            startService(intent);
            onDestroy(); // stop service
        }

        return START_STICKY;
    }

    @Nullable @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int steps = (int) event.values[0];

        int diff = steps - mTotalStepsSinceBoot;
        if (diff > THRESHOLD) {
            mTotalStepsSinceBoot = steps;
            Intent intent = new Intent(this, ContextUpdateManager.class);
            intent.putExtra("DataSource", "Steps");
            intent.putExtra("Count", diff);
            startService(intent);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
