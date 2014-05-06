package de.hsbremen.kss.model;

import de.hsbremen.kss.configuration.Station;

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
}
