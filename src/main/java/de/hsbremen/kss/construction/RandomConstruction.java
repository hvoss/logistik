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

        final Set<Order> visitedSourceOrder = new HashSet<>(orders.size());
        final Set<Order> visitedDestinationOrder = new HashSet<>(orders.size());

        while (!stations.isEmpty()) {
            final Station rElement = RandomUtils.randomElement(stations);
            tour.addStation(rElement);
            stations.remove(rElement);

            final Set<Order> sourceOrders = new HashSet<>(rElement.getSourceOrders());
            final Set<Order> destinationOrders = new HashSet<>(rElement.getDestinationOrders());

            // order where the source station reached
            visitedSourceOrder.addAll(sourceOrders);

            // orders where the destination station reached
            final Collection<Order> destinationStationReached = CollectionUtils.intersection(visitedSourceOrder, destinationOrders);
            visitedDestinationOrder.addAll(destinationStationReached);

            // add new stations
            sourceOrders.removeAll(visitedDestinationOrder);
            stations.addAll(Order.getAllDestinationStations(sourceOrders));
        }

        tour.addOrders(orders);

        plan.addTour(tour);

        return plan;
    }
}
