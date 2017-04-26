package com.aidanogrady.contextualtriggers.context;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kristine on 25/04/2017.
 */

public class Geofence implements Parcelable {

    private long id;
    private String name;
    private double latitude;
    private double longitude;

    public Geofence(String name, double latitude, double longitude) {
        this.id = Integer.MAX_VALUE;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Geofence createFromParcel(Parcel in) {return new Geofence(in); }

        public Geofence[] newArray(int size) { return new Geofence[size]; }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    private Geofence(Parcel in) {
        id = Integer.MAX_VALUE;
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }
}
