package de.hsbremen.kss.model;

import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.TimeWindow;

/**
 * 
 * @author henrik
 * 
 */
abstract class DepotAction implements Action {

    /** the depot where the action is performed */
    private final Station depot;

    private final TimeWindow timewindow;

    /**
     * ctor.
     * 
     * @param depot
     *            the depot where the action is performed
     */
    DepotAction(final Station depot, final TimeWindow timewindow) {
        this.depot = depot;
        this.timewindow = timewindow;
    }

    @Override
    public double duration() {
        return 0;
    }

    @Override
    public final Station getStation() {
        return this.depot;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " : " + this.depot;
    }

    @Override
    public TimeWindow timewindow() {
        return this.timewindow;
    }
}
