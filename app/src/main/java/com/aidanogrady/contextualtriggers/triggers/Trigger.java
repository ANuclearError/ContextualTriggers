package com.aidanogrady.contextualtriggers.triggers;

/**
 * The basic trigger interface. The purpose of a trigger is to prompt the user to perform some
 * behaviour. A trigger will determine the best time to notify the user based on some contextual
 * information it has access to.
 *
 * @author Aidan O'Grady
 */
interface Trigger{

    int getComplexity();

    String getNotificationTitle();

    String getNotificationMessage();

    Boolean isTriggered();
}
