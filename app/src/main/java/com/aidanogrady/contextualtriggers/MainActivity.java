package com.aidanogrady.contextualtriggers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/**
 * MainActivity starts up the application. If previously stopped in boot and the user
 * has pressed to start the application then it is used to restart the application.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("ContextTriggers", "Application started");
        //Start the ContentUpdateManager service
        Intent intent = new Intent(this, ContextUpdateManager.class);
        startService(intent);

        finish();
    }
}
