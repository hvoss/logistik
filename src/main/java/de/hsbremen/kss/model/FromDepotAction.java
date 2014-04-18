package de.hsbremen.kss.model;

import de.hsbremen.kss.configuration.Station;

/**
 * 
 * @author henrik
 * 
 */
public class FromDepotAction extends DepotAction {

    /**
     * ctor.
     * 
     * @param depot
     *            the depot where the action is performed
     */
    FromDepotAction(final Station depot) {
        super(depot);
    }

}
