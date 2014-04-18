package de.hsbremen.kss.construction;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
        final Plan plan = new Plan(RadialConstruction.class);

        final Vehicle vehicle = CollectionUtils.get(configuration.getVehicles(), 0);
        final Tour tour = new Tour(vehicle);
        final Station sourceDepot = vehicle.getSourceDepot();

        final Set<Station> allSourceStations = Order.getAllSourceStations(configuration.getOrders());

        final StationAngleComparator angleComparator = new StationAngleComparator(sourceDepot);

        final SortedSet<Station> sortedStations = new TreeSet<>(angleComparator);

        final Set<Order> visitedSourceOrders = new HashSet<>();
        final Set<Order> visitedDestinationOrders = new HashSet<>();

        sortedStations.addAll(allSourceStations);

        while (!sortedStations.isEmpty()) {
            final Station station = sortedStations.first();
            sortedStations.remove(station);

            final Set<Order> newSourceOrders = new HashSet<>(station.getSourceOrders());
            final Set<Order> newDestinationOrders = new HashSet<>(station.getDestinationOrders());
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
            sortedStations.addAll(Order.getAllDestinationStations(newSourceOrders));
        }

        plan.addTour(tour);

        return plan;
    }

    @Override
    public void logStatistic() {
        // TODO Auto-generated method stub

    }

}
