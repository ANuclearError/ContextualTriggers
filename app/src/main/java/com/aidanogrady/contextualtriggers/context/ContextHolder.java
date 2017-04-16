package com.aidanogrady.contextualtriggers.context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ASUS on 15/04/2017.
 */

public class ContextHolder implements ContextAPI {

    private int steps;
    private Map<String, Double> location;
    private String weatherMain;
    private String weatherId;

    public ContextHolder() {
        // set default values
        this.steps = -1;
        this.location = new HashMap<>();
        this.weatherMain = "None";
        this.weatherId = "None";
    }

    @Override
    public Map<String, Double> getLocation() {
        return location;
    }

    @Override
    public int getSteps() {
        return steps;
    }


    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setLocation(double latitude, double longitude) {
        location.put("Latitude", latitude);
        location.put("Longitude", longitude);
    }

    public String getWeatherId(){
        return weatherId;
    }

    public void setWeatherId(String id){
        weatherId = id;
    }

    public Date getCurrentTime() throws ParseException {

        Calendar currentCal = Calendar.getInstance();
        String currentTimeStr = new SimpleDateFormat("HH:mm:ss").format(currentCal.getTime());
        Date currentTime = new SimpleDateFormat("HH:mm:ss").parse(currentTimeStr);
        currentCal.setTime(currentTime);
        currentCal.add(Calendar.DATE, 1);

        return currentCal.getTime();

    }

}
