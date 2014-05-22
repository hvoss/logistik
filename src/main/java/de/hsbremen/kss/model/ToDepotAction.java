package de.hsbremen.kss.model;

import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.TimeWindow;

/**
 * The Class ToDepotAction.
 */
public class ToDepotAction extends DepotAction {

    /**
     * Instantiates a new to depot action.
     * 
     * @param depot
     *            the depot
     */
    ToDepotAction(final Station depot, final TimeWindow timeWindow) {
        super(depot, timeWindow);
    }

}
