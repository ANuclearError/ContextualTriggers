package com.aidanogrady.contextualtriggers.context;

import java.util.Observable;

/**
 * Created by thomas on 10/04/17.
 */

public interface ContextAPI {

    public boolean registerObserver();

    public boolean unregisterObserver();

    public void notifyObservers();

    public ContextAPI getContext();

}
