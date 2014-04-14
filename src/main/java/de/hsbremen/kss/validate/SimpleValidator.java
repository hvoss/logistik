package de.hsbremen.kss.validate;

import java.util.Set;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.model.Plan;

/**
 * A simple validator.
 * 
 * checks only the source stations.
 * 
 * @author henrik
 * 
 */
public final class SimpleValidator implements Validator {

    @Override
    public boolean validate(final Configuration configuration, final Plan plan) {
        return allStationsReached(configuration, plan);
    }

    /**
     * checks if all source stations reached.
     * 
     * @param configuration
     *            the given configuration
     * @param plan
     *            the plan of the construction algorithm
     * @return true: all source stations reached; false: otherwise
     */
    protected boolean allStationsReached(final Configuration configuration, final Plan plan) {
        final Set<Station> allSourceStations = Order.getAllSourceStations(configuration.getOrders());
        return plan.getStations().containsAll(allSourceStations);
    }
}
