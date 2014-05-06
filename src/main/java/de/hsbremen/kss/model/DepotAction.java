package de.hsbremen.kss.model;

import de.hsbremen.kss.configuration.Station;

/**
 * 
 * @author henrik
 * 
 */
abstract class DepotAction implements Action {

    /** the depot where the action is performed */
    private final Station depot;

    /**
     * ctor.
     * 
     * @param depot
     *            the depot where the action is performed
     */
    DepotAction(final Station depot) {
        this.depot = depot;
    }

    @Override
    public final Station getStation() {
        return this.depot;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " : " + this.depot;
    }
}
