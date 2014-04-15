package de.hsbremen.kss.validate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;

/**
 * A simple validator.
 * 
 * checks only the source stations.
 * 
 * @author henrik
 * 
 */
public final class SimpleValidator implements Validator {

    /** logging interface */
    private static final Logger LOG = LoggerFactory.getLogger(SimpleValidator.class);

    @Override
    public boolean validate(final Configuration configuration, final Plan plan) {
        final Set<Order> orders = new HashSet<>(configuration.getOrders());

        for (final Tour tour : plan.getTours()) {
            final List<Station> stations = tour.getStations();
            for (final Order order : tour.getOrders()) {
                final Station source = order.getSource();
                final Station destination = order.getDestination();

                final int sourceIndex = stations.indexOf(source);

                if (sourceIndex > -1) {
                    if (destination != null) {
                        final int destinationIndex = stations.lastIndexOf(destination);

                        if (destinationIndex >= sourceIndex) {
                            orders.remove(order);
                        } else if (destinationIndex == -1) {
                            SimpleValidator.LOG.warn("destination(" + destination + ") not visit on order: " + order);
                        } else {
                            SimpleValidator.LOG.warn("destination(" + destination + ", " + destinationIndex + ") visited before source(" + source
                                    + ", " + sourceIndex + ") on order: " + order);
                        }
                    } else {
                        orders.remove(order);
                    }
                } else {
                    SimpleValidator.LOG.warn("source(" + source + ") not visit on order: " + order);
                }
            }
        }

        return orders.isEmpty();
    }
}
