package com.aidanogrady.contextualtriggers.context.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Calum on 26/04/2017.
 */

public class FoursquareVenue implements Serializable{

    private int mCheckIns;
    private String mVenueName;
    private String mVenueCategory;
    private String mLatLng;

    public FoursquareVenue(String name, String category, int checkins, String latlng){

        mCheckIns = checkins;
        mVenueName = name;
        mVenueCategory = category;
        mLatLng = latlng;
    }

    public int getCheckins(){
        return mCheckIns;
    }

    public String getVenueName(){
        return mVenueName;
    }

    public String getVenueCategory(){
        return mVenueCategory;
    }

    public String getLatLng(){
        return mLatLng;
    }

    @Override
    public String toString() {
        return "FoursquareVenue{" +
                "mCheckIns=" + mCheckIns +
                ", mVenueName='" + mVenueName + '\'' +
                ", mVenueCategory='" + mVenueCategory + '\'' +
                ", mLatLng='" + mLatLng + '\'' +
                '}';
    }
}
