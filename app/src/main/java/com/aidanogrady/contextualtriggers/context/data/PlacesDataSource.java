package com.aidanogrady.contextualtriggers.context.data;

import android.location.Location;

import com.google.android.gms.location.LocationListener;

import java.util.Observable;


/**
 * The places data source utilises Google's Place API Web Service to obtain interesting locations
 * near the user.
 *
 * @author Aidan O'Grady
 * @since 0.0.1
 */
public class PlacesDataSource extends Observable implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {

    }
}
