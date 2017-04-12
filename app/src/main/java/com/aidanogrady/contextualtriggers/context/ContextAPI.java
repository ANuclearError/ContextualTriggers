package com.aidanogrady.contextualtriggers.context;


/**
 * Created by thomas on 10/04/17.
 */

public interface ContextAPI  {

    boolean registerObserver();

    boolean unregisterObserver();

    void notifyObservers();

    ContextAPI getContext();
}
