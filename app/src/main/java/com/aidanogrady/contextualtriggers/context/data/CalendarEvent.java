package com.aidanogrady.contextualtriggers.context.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A basic object for storing information on a calendar event in the user's calendar.
 *
 * @author Aidan O'Grady
 */
public class CalendarEvent implements Parcelable {
    public static final String TAG = "com.aidanogrady.contextualtriggers.TodaysEvents";

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
            String title = in.readString();
            String location = in.readString();
            long startTime = in.readLong();
            return new CalendarEvent(title, location, startTime);
        }

        @Override
        public CalendarEvent[] newArray(int size) {
            return new CalendarEvent[size];
        }
    };

    private CalendarEvent(String title, String location, long startTime) {
        mTitle = title;
        mLocation = location;
        mStartTime = startTime;
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

    /**
     * Returns the title of the event.
     *
     * @return event title
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Returns the location of the event.
     *
     * @return event location
     */
    public String getLocation() {
        return mLocation;
    }

    /**
     * Returns the starting time of the event.
     *
     * @return event start time
     */
    public long getStartTime() {
        return mStartTime;
    }
}
