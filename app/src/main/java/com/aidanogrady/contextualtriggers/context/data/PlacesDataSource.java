package com.aidanogrady.contextualtriggers.context.data;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;


/**
 * The places data source utilises Google's Place API Web Service to obtain interesting locations
 * near the user.
 *
 * @author Aidan O'Grady
 */
public class PlacesDataSource extends IntentService {

    public PlacesDataSource(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
