package de.hsbremen.kss.construction;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.RandomUtils;

/**
 * try to find a solution by random.
 * 
 * @author henrik
 * 
 */
public final class RandomConstruction implements Construction {

    /** logging interface */
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(RandomConstruction.class);

    /**
     * ctor.
     * 
     */
    public RandomConstruction() {
    }

    @Override
    public Plan constructPlan(final Configuration configuration) {
        final Plan plan = new Plan(RandomConstruction.class);
        final Vehicle vehicle = CollectionUtils.get(configuration.getVehicles(), 0);
        final Tour tour = new Tour(vehicle);

        final Set<Order> orders = configuration.getOrders();
        final Set<Station> stations = Order.getAllSourceStations(orders);

        final Set<Order> visitedSourceOrders = new HashSet<>(orders.size());
        final Set<Order> visitedDestinationOrders = new HashSet<>(orders.size());

        while (!stations.isEmpty()) {
            final Station rElement = RandomUtils.randomElement(stations);
            stations.remove(rElement);

            final Set<Order> newSourceOrders = new HashSet<>(rElement.getSourceOrders());
            final Set<Order> newDestinationOrders = new HashSet<>(rElement.getDestinationOrders());
            newSourceOrders.removeAll(visitedSourceOrders);
            newDestinationOrders.removeAll(visitedDestinationOrders);

            tour.addSourceOrders(newSourceOrders);

            // order where the source station reached
            visitedSourceOrders.addAll(newSourceOrders);

            // orders where the destination station reached
            final Collection<Order> destinationStationReached = CollectionUtils.intersection(visitedSourceOrders, newDestinationOrders);
            visitedDestinationOrders.addAll(destinationStationReached);

            tour.addDestinationOrders(destinationStationReached);

            // add new stations
            newSourceOrders.removeAll(visitedDestinationOrders);
            stations.addAll(Order.getAllDestinationStations(newSourceOrders));
        }

        plan.addTour(tour);

        return plan;
    }

    @Override
    public void logStatistic() {
        // TODO Auto-generated method stub

    }
}
