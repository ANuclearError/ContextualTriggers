package com.aidanogrady.contextualtriggers.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ASUS on 15/04/2017.
 */

public class ContextHolder implements ContextAPI {

    private int steps;
    private Map<String, Double> location;
    private String weatherMain;
    private String weatherDescription;

    public ContextHolder() {
        // set default values
        this.steps = -1;
        this.location = new HashMap<>();
        this.weatherMain = "None";
        this.weatherDescription = "None";
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

    public String getWeatherMain(){
        return weatherMain;
    }

    public String getWeatherDescription(){
        return weatherDescription;
    }

    public void setWeatherDescription(String description){
        weatherDescription = description;
    }

    public void setWeatherMain(String main){
        weatherMain = main;
    }

}
