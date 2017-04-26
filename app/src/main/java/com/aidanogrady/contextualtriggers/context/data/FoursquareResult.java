package com.aidanogrady.contextualtriggers.context.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Calum on 26/04/2017.
 */

public class FoursquareResult implements Parcelable{

        private List<FoursquareVenue> mNearbyVenues;

        public static final Creator<FoursquareResult> CREATOR = new Creator<FoursquareResult>() {
            @Override
            public FoursquareResult createFromParcel(Parcel in) {
                return new FoursquareResult(in);
            }

            @Override
            public FoursquareResult[] newArray(int size) {
                return new FoursquareResult[size];
            }
        };

        private FoursquareResult(Parcel in) {
            mNearbyVenues = new ArrayList<>();
            in.readList(mNearbyVenues,null);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(mNearbyVenues);
        }

        public List<FoursquareVenue> getNearbyVenues() {
            return mNearbyVenues;
        }

}
