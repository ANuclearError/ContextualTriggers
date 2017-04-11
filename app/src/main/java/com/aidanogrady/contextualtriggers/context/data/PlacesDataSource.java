package com.aidanogrady.contextualtriggers.context.data;

import android.location.Location;

import com.aidanogrady.contextualtriggers.context.ListenerContext;
import com.google.android.gms.location.LocationListener;


/**
 * The places data source utilises Google's Place API Web Service to obtain interesting locations
 * near the user.
 *
 * @author Aidan O'Grady
 * @since 0.1
 */
public class PlacesDataSource extends ListenerContext implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {

    }
}
