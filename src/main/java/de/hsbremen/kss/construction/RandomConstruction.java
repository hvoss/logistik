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

        final Set<Order> visitedSourceOrders = new HashSet<>(orders.size());
        final Set<Order> visitedDestinationOrders = new HashSet<>(orders.size());

        while (true) {
            final Set<Station> processableOrders = processableOrders(configuration.getOrders(), visitedSourceOrders, visitedDestinationOrders, tour);
            if (processableOrders.isEmpty()) {
                break;
            }
            final Station rElement = RandomUtils.randomElement(processableOrders);

            final Set<Order> newSourceOrders = new HashSet<>(rElement.getSourceOrders());
            final Set<Order> newDestinationOrders = new HashSet<>(rElement.getDestinationOrders());
            newSourceOrders.removeAll(visitedSourceOrders);
            newDestinationOrders.removeAll(visitedDestinationOrders);

            // unload orders
            final Collection<Order> destinationStationReached = CollectionUtils.intersection(visitedSourceOrders, newDestinationOrders);
            visitedDestinationOrders.addAll(destinationStationReached);

            tour.addDestinationOrders(destinationStationReached);

            // Load new orders
            for (final Order newSourceOrder : newSourceOrders) {
                if (tour.freeSpace() >= newSourceOrder.weightOfProducts()) {
                    tour.addSourceOrder(newSourceOrder);

                    // order where the source station reached
                    visitedSourceOrders.add(newSourceOrder);
                }
            }

        }

        plan.addTour(tour);

        return plan;
    }

    private static Set<Station> processableOrders(final Set<Order> allOrders, final Set<Order> visitedSourceOrders,
            final Set<Order> visitedDestinationOrders, final Tour tourSoFar) {
        final Set<Station> processableOrders = new HashSet<>();

        final Set<Order> notVisitedSourceOrders = new HashSet<>(allOrders);
        final Set<Order> notVisitedDestinationOrders = new HashSet<>(allOrders);
        notVisitedSourceOrders.removeAll(visitedSourceOrders);
        notVisitedDestinationOrders.removeAll(visitedDestinationOrders);

        final Set<Order> processableSourceOrders = Order.filterOrderByWeight(notVisitedSourceOrders, tourSoFar.freeSpace());
        final Collection<Order> processableDestinationOrders = CollectionUtils.intersection(visitedSourceOrders, notVisitedDestinationOrders);

        processableOrders.addAll(Order.getAllSourceStations(processableSourceOrders));
        processableOrders.addAll(Order.getAllDestinationStations(processableDestinationOrders));

        return processableOrders;
    }

    @Override
    public void logStatistic() {
        // TODO Auto-generated method stub

    }
}
