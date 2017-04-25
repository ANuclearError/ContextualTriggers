package com.aidanogrady.contextualtriggers.context.data;

import android.Manifest;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Pair;

import com.aidanogrady.contextualtriggers.ContextUpdateManager;
import com.aidanogrady.contextualtriggers.R;
import com.permissioneverywhere.PermissionEverywhere;
import com.permissioneverywhere.PermissionResponse;
import com.permissioneverywhere.PermissionResultCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * The CalendarDataSource provides information on the user's calendar. Triggers will be able to
 * decide on how to use this calendar information.
 *
 * @author Aidan O'Grady
 */
public class CalendarDataSource extends IntentService implements PermissionResultCallback {
    public static final String TAG = "calendar";

    public static final String[] INSTANCES_PROJECTION = new String[] {
            CalendarContract.Instances.TITLE,
            CalendarContract.Instances.EVENT_LOCATION,
            CalendarContract.Instances.BEGIN
    };

    private static final int INSTANCE_TITLE_INDEX = 0;

    private static final int INSTANCE_EVENT_LOCATION_INDEX = 1;

    private static final int INSTANCE_BEGIN_INDEX = 2;


    public CalendarDataSource() {
        super("CalendarDataSource");
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        PermissionEverywhere.getPermission(getApplicationContext(),
                new String[] {
                        Manifest.permission.READ_CALENDAR
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

        Pair<Long, Long> period = getTodayMillis();

        ArrayList<CalendarEvent> results = null;

        boolean read = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED;

        if (read) {
            Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
            ContentUris.appendId(builder, period.first);
            ContentUris.appendId(builder, period.second);

            Cursor cursor = cr.query(builder.build(), INSTANCES_PROJECTION, null, null, null);
            if (cursor != null) {
                results = new ArrayList<>();
                while (cursor.moveToNext()) {
                    String title = cursor.getString(INSTANCE_TITLE_INDEX);
                    String location = cursor.getString(INSTANCE_EVENT_LOCATION_INDEX);
                    long begin = cursor.getLong(INSTANCE_BEGIN_INDEX);

                    Parcel parcel = Parcel.obtain();
                    parcel.writeString(title);
                    parcel.writeString(location);
                    parcel.writeLong(begin);
                    parcel.setDataPosition(0);
                    CalendarEvent event = CalendarEvent.CREATOR.createFromParcel(parcel);
                    results.add(event);
                    parcel.recycle();
                }
                cursor.close();
            }
        }

        intent = new Intent(this, ContextUpdateManager.class);
        intent.putExtra("DataSource", "Calendar");
        intent.putParcelableArrayListExtra(CalendarEvent.TAG, results);
        startService(intent);
    }

    /**
     * Creates a pair of milliseconds from now to to end of the day.
     *
     * @return Pair from now to 23:59:59.999 tonight.
     */
    private Pair<Long, Long> getTodayMillis() {
        long now = System.currentTimeMillis();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 23);
        today.set(Calendar.MINUTE, 59);
        today.set(Calendar.SECOND, 59);
        today.set(Calendar.MILLISECOND, 999);
        return new Pair<>(now, today.getTimeInMillis());
    }

    @Override
    public void onComplete(PermissionResponse permissionResponse) {
        boolean read = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED;
        if (!read) {
            this.stopSelf();
        }
    }
}
