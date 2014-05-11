package de.hsbremen.kss.construction;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.StationAngleComparator;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.util.ConstructionUtils;

/**
 * The Class RadialConstruction.
 */
public final class SweepConstruction implements Construction {

    /** logging interface. */
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(SweepConstruction.class);

    @Override
    public Plan constructPlan(final Configuration configuration) {
        return constructPlan(configuration, null, true);
    }

    /**
     * constructs a plan based on the sweep-algorithm.
     * 
     * @param configuration
     *            the given configuration
     * @param startStation
     *            station to start
     * @param forward
     *            indicates whether to go forward (against the clock) or
     *            backward (with the clock).
     * @return the constructed plan
     */
    public Plan constructPlan(final Configuration configuration, final Station startStation, final boolean forward) {
        final Plan plan = new Plan(SweepConstruction.class);

        final Vehicle vehicle = CollectionUtils.get(configuration.getVehicles(), 0);
        final Tour tour = new Tour(vehicle);
        final Station sourceDepot = vehicle.getSourceDepot();

        final Set<Order> visitedSourceOrders = new HashSet<>();
        final Set<Order> visitedDestinationOrders = new HashSet<>();

        Station actualStation;
        if (startStation != null) {
            actualStation = startStation;
        } else {
            final StationAngleComparator comparator = new StationAngleComparator(sourceDepot, sourceDepot, forward);
            final Set<Station> stations = Order.getAllSourceStations(configuration.getOrders());
            actualStation = Collections.min(stations, comparator);
        }

        while (true) {
            final Set<Station> processableStations = ConstructionUtils.processableStations(configuration.getOrders(), visitedSourceOrders,
                    visitedDestinationOrders, tour);
            if (processableStations.isEmpty()) {
                break;
            }
            final StationAngleComparator comparator = new StationAngleComparator(sourceDepot, actualStation, forward);
            actualStation = Collections.min(processableStations, comparator);

            final Set<Order> newSourceOrders = new HashSet<>(actualStation.getSourceOrders());
            final Set<Order> newDestinationOrders = new HashSet<>(actualStation.getDestinationOrders());
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

    @Override
    public void logStatistic() {
        // TODO Auto-generated method stub

    }

}
