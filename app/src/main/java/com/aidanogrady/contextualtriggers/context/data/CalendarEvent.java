package com.aidanogrady.contextualtriggers.context.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A basic object for storing information on a calendar event in the user's calendar.
 *
 * @author Aidan O'Grady
 */
public class CalendarEvent implements Parcelable {
    public static final String TAG =
            "com.aidanogrady.contextualtriggers.TodaysEvents";

    /**
     * The title of this event.
     */
    private String mTitle;

    /**
     * The location of the event.
     */
    private String mLocation;

    /**
     * The end time of the event.
     */
    private long mStartTime;

    public static final Creator<CalendarEvent> CREATOR = new Creator<CalendarEvent>() {
        @Override
        public CalendarEvent createFromParcel(Parcel in) {
            return new CalendarEvent(in);
        }

        @Override
        public CalendarEvent[] newArray(int size) {
            return new CalendarEvent[size];
        }
    };

    private CalendarEvent(Parcel in) {
        mTitle = in.readString();
        mLocation = in.readString();
        mStartTime = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mLocation);
        parcel.writeLong(mStartTime);
    }
}
