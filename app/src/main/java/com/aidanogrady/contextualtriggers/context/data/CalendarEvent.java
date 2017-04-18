package com.aidanogrady.contextualtriggers.context.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * TODO: Class desc
 *
 * @author Aidan O'Grady
 * @since TODO: Version
 */
public class CalendarEvent implements Parcelable {
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

    public CalendarEvent(Parcel in) {
        mLocation = in.readString();
        mStartTime = in.readLong();
    }

    public CalendarEvent(S)

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mLocation);
        parcel.writeLong(mStartTime);
    }
}
