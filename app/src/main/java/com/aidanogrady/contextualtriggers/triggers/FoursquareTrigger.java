package com.aidanogrady.contextualtriggers.triggers;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.aidanogrady.contextualtriggers.R;
import com.aidanogrady.contextualtriggers.context.ContextAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public FoursquareTrigger(String name, Context context, ContextAPI holder) {
        super(name, context, holder);
        mContext = context;
        mContextHolder = holder;
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

        String nearby = mContextHolder.getNearbyFoursquareData();

        if(nearby != null){
            if(handleNearbyData(nearby)){
                notifyUser();
            }
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

                JSONObject parkObject = jsonArray.getJSONObject(0);
                String name = parkObject.getString("name");
                mNotificationTitle = "Great location nearby!";
                mNotificationMessage = String.format("You're near a park! Perfect for a run!",name);
                return true;
            }

            return false;

        } catch (JSONException e) {
            return false;
        }


    }
}
