package de.hsbremen.kss.model;

import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.TimeWindow;

/**
 * represents an action performed within a tour.
 * 
 * @author henrik
 * 
 */
public interface Action {

    /**
     * returns the station where the action is performed
     * 
     * @return station where the action is performed
     */
    Station getStation();

    /**
     * returns the duration of a action.
     * 
     * @return duration of a action
     */
    double duration();

    TimeWindow timewindow();
}
