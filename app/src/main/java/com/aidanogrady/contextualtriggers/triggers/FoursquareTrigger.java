package com.aidanogrady.contextualtriggers.triggers;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.Pair;

import com.aidanogrady.contextualtriggers.R;
import com.aidanogrady.contextualtriggers.context.ContextAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Calum on 15/04/2017.
 */

public class FoursquareTrigger extends SimpleTrigger {
    /**
     * Constructs a new SimpleTrigger with the given name.
     *
     * @param name    the name of the service for this trigger.
     * @param context
     */

    private Context mContext;
    private ContextAPI mContextHolder;
    private String mNotificationTitle;
    private String mNotificationMessage;

    private List<Pair<String,String>> mRecentLocations;
    private double mRecentVisitThreshold = 90;

    public FoursquareTrigger(String name, Context context, ContextAPI holder) {
        super(name, context, holder);
        mContext = context;
        mContextHolder = holder;
        mRecentLocations = new ArrayList<>();
    }

    @Override
    public void notifyUser() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.basic_notification_icon)
                        .setContentTitle(mNotificationTitle)
                        .setContentText(mNotificationMessage);
        int mNotificationId = 006;
        NotificationManager mNotifyMgr =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @Override
    public void notifyIfTriggered() {

            if(isTriggered()) {
                notifyUser();
            }
    }

    @Override
    public Boolean isTriggered() {
        String nearby = mContextHolder.getNearbyFoursquareData();

        if(nearby != null){
            return handleNearbyData(nearby);
        }

        return false;
    }


    private boolean handleNearbyData(String nearby) {

        try {

            JSONObject nearbyJson = new JSONObject(nearby).getJSONObject("response");
            JSONArray jsonArray = nearbyJson.getJSONArray("venues");

            if(jsonArray.length() != 0){

                updateTally(jsonArray);

                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject parkObject = jsonArray.getJSONObject(i);
                    String name = parkObject.getString("name");
                    JSONObject statsObject = parkObject.getJSONObject("stats");
                    int checkIns = statsObject.getInt("checkinsCount");

                    if(checkIns >= 100) {
                        mNotificationTitle = "Great location nearby!";
                        mNotificationMessage = String.format("You are near %s! Perfect for a run!", name);
                        return true;
                    }
                }

                return false;
            }

            return false;

        } catch (JSONException e) {
            return false;
        }
    }

    private void updateTally(JSONArray venues){

        try {
            String category = venues.getJSONObject(0)
                    .getJSONArray("categories")
                    .getJSONObject(0)
                    .getString("name");

            String venueName = venues.getJSONObject(0)
                    .getString("name");

            SharedPreferences commonLocations = mContext.getSharedPreferences("locationPrefs", 0);
            int categoryTally = commonLocations.getInt(category, -1);

            SharedPreferences.Editor editor = commonLocations.edit();
            if(categoryTally >= 0) {
                if(!visitedRecently(venueName)) {
                    editor.putInt(category, categoryTally + 1);
                }
            }else{
                editor.putInt(category, 1);
            }

            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private boolean visitedRecently(String name) {

        for (Pair location : mRecentLocations) {

            if (location.first.equals(name)) {
                try {
                    String dateString = (String) location.second;
                    DateFormat format = DateFormat.getDateTimeInstance();
                    Date previous_time = format.parse(dateString);
                    System.out.println(previous_time);

                    Date current_time = new Date();
                    System.out.println(current_time);

                    long difference = current_time.getTime() - previous_time.getTime();
                    long hoursDifference = difference / (60 * 1000);
                    System.out.println("DIFFERENCE: " + hoursDifference);

                    if(hoursDifference > mRecentVisitThreshold){
                        mRecentLocations.remove(location);
                        mRecentLocations.add(new Pair<>(name,format.format(current_time)));
                        return true;
                    }else{
                        return false;
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        DateFormat format = DateFormat.getDateTimeInstance();
        Date now = new Date();
        String current_time = format.format(now);

        mRecentLocations.add(new Pair<>(name, current_time));
        return false;
    }
}
