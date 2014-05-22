package de.hsbremen.kss.model;

import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.TimeWindow;

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
    FromDepotAction(final Station depot, final TimeWindow timeWindow) {
        super(depot, timeWindow);
    }

}
