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

    public FoursquareVenue(String name, String category, int checkins){

        mCheckIns = checkins;
        mVenueName = name;
        mVenueCategory = category;
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

    @Override
    public String toString() {
        return "FoursquareVenue{" +
                "mCheckIns=" + mCheckIns +
                ", mVenueName='" + mVenueName + '\'' +
                ", mVenueCategory='" + mVenueCategory + '\'' +
                '}';
    }
}
