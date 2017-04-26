package com.aidanogrady.contextualtriggers.context;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kristine on 25/04/2017.
 */

public class Geofences implements Parcelable {

    private long id;
    private String name;
    private double latitude;
    private double longitude;

    public Geofences(String name, double latitude, double longitude) {
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

        public Geofences createFromParcel(Parcel in) {return new Geofences(in); }

        public Geofences[] newArray(int size) { return new Geofences[size]; }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    private Geofences(Parcel in) {
        id = Integer.MAX_VALUE;
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }
}
