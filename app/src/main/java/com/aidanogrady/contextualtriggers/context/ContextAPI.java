package com.aidanogrady.contextualtriggers.context;


import java.util.Map;

/**
 * Created by thomas on 10/04/17.
 */

public interface ContextAPI  {

    Map<String, Double> getLocation();

    int getSteps();

    String getWeatherMain();

    String getWeatherDescription();

}
