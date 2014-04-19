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

/**
 * The Class RadialConstruction.
 */
public final class RadialConstruction implements Construction {

    /** logging interface. */
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(RadialConstruction.class);

    @Override
    public Plan constructPlan(final Configuration configuration) {
        return constructPlan(configuration, null, true);
    }

    public Plan constructPlan(final Configuration configuration, final Station startStation, final boolean forward) {
        final Plan plan = new Plan(RadialConstruction.class);

        final Vehicle vehicle = CollectionUtils.get(configuration.getVehicles(), 0);
        final Tour tour = new Tour(vehicle);
        final Station sourceDepot = vehicle.getSourceDepot();

        final Set<Station> stations = Order.getAllSourceStations(configuration.getOrders());

        final Set<Order> visitedSourceOrders = new HashSet<>();
        final Set<Order> visitedDestinationOrders = new HashSet<>();

        Station actualStation;
        if (startStation != null) {
            actualStation = startStation;
        } else {
            final StationAngleComparator comparator = new StationAngleComparator(sourceDepot, sourceDepot, forward);
            actualStation = Collections.min(stations, comparator);
        }

        for (final Station station : configuration.getStations()) {
            if (sourceDepot != station) {
                RadialConstruction.LOG.info(sourceDepot + " -> " + station + " : " + sourceDepot.angle(station));
            }
        }

        while (!stations.isEmpty()) {
            final StationAngleComparator comparator = new StationAngleComparator(sourceDepot, actualStation, forward);
            actualStation = Collections.min(stations, comparator);
            stations.remove(actualStation);

            final Set<Order> newSourceOrders = new HashSet<>(actualStation.getSourceOrders());
            final Set<Order> newDestinationOrders = new HashSet<>(actualStation.getDestinationOrders());
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
