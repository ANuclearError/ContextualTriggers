package com.aidanogrady.contextualtriggers.context.data;

import android.Manifest;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.aidanogrady.contextualtriggers.ContextUpdateManager;
import com.aidanogrady.contextualtriggers.R;
import com.permissioneverywhere.PermissionEverywhere;
import com.permissioneverywhere.PermissionResponse;
import com.permissioneverywhere.PermissionResultCallback;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * The CalendarDataSource provides information on the user's calendar. Triggers will be able to
 * decide on how to use this calendar information.
 *
 * @author Aidan O'Grady
 */
public class CalendarDataSource extends IntentService implements PermissionResultCallback {
    public static final String TAG = "calendar";

    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Events.EVENT_LOCATION,
            CalendarContract.Events.DTSTART
    };

    private static final int EVENT_LOCATION_INDEX = 0;

    private static final int EVENT_DTSTART_INDEX = 1;


    public CalendarDataSource(String name) {
        super(name);
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        PermissionEverywhere.getPermission(getApplicationContext(),
                new String[] {
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR
                },
                1,
                "Contextual Triggers",
                "This service needs calendar read and write permissions",
                R.mipmap.ic_launcher)
                .enqueue(this);

        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;

        long epochMillis = System.currentTimeMillis();
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(epochMillis);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar tomorrow = today;
        tomorrow.add(Calendar.DATE, 1);

        String selection = "((" +
                CalendarContract.Events.DTSTART + " >= ?) AND (" +
                CalendarContract.Events.DTEND + "<= ?" +
                "))";
        String[] selectionArgs = {
                Long.toString(today.getTimeInMillis()),
                Long.toString(tomorrow.getTimeInMillis()),
        };

        boolean read = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED;
        boolean write = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED;

        ArrayList<CalendarEvent> results = new ArrayList<>();
        if (read && write) {
            Cursor cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

            if (cur != null) {
                while (cur.moveToNext()) {
                    String location;
                    long dtstart = 0;

                    location = cur.getString(EVENT_LOCATION_INDEX);
                    dtstart = cur.getLong(EVENT_DTSTART_INDEX);
                    Parcel parcel = Parcel.obtain();
                    parcel.writeString(location);
                    parcel.writeLong(dtstart);
                    results.add(CalendarEvent.CREATOR.createFromParcel(parcel));
                    parcel.recycle();
                }

                cur.close();
            }
        }

        intent = new Intent(this, ContextUpdateManager.class);
        intent.putExtra("DataSource", "Calendar");
        intent.putParcelableArrayListExtra(CalendarEvent.TAG, results);
        startService(intent);

    }

    @Override
    public void onComplete(PermissionResponse permissionResponse) {
        boolean read = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED;
        boolean write = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED;

        if (!read || !write) {
            this.stopSelf();
        }
    }
}
