package de.hsbremen.kss.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.OrderStation;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Tour;

public class ConstructionUtils {

    private final RandomUtils randomUtils;

    /**
     * static class
     */
    public ConstructionUtils(final RandomUtils randomUtils) {
        this.randomUtils = randomUtils;
    }

    public static Set<Station> processableStations(final Collection<Order> allOrders, final Collection<Order> visitedSourceOrders,
            final Collection<Order> visitedDestinationOrders, final Tour tourSoFar) {
        final Set<Station> processableOrders = new HashSet<>();

        final Set<Order> processableSourceOrders = processableSourceOrders(allOrders, visitedSourceOrders, tourSoFar);
        final Set<Order> processableDestinationOrders = processableDestinationOrders(allOrders, visitedSourceOrders, visitedDestinationOrders,
                tourSoFar);

        processableOrders.addAll(Order.getAllSourceStations(processableSourceOrders));
        processableOrders.addAll(Order.getAllDestinationStations(processableDestinationOrders));

        return processableOrders;
    }

    public static Set<Order> processableSourceOrders(final Collection<Order> allOrders, final Collection<Order> visitedSourceOrders,
            final Tour tourSoFar) {
        final Set<Order> notVisitedSourceOrders = new HashSet<>(allOrders);
        notVisitedSourceOrders.removeAll(visitedSourceOrders);
        return Order.filterOrdersByAmount(notVisitedSourceOrders, tourSoFar.freeAmount());
    }

    public static Set<Order> processableDestinationOrders(final Collection<Order> allOrders, final Collection<Order> visitedSourceOrders,
            final Collection<Order> visitedDestinationOrders, final Tour tourSoFar) {
        final Set<Order> notVisitedDestinationOrders = new HashSet<>(allOrders);
        notVisitedDestinationOrders.removeAll(visitedDestinationOrders);
        return new HashSet<>(CollectionUtils.intersection(visitedSourceOrders, notVisitedDestinationOrders));
    }

    public Tour createTourFromOrders(final Vehicle vehicle, final Collection<Order> orders) {
        final List<OrderStation> stations = new ArrayList<>();

        for (final Order order : orders) {
            stations.add(order.getSource());
        }

        final Tour tour = new Tour(vehicle);
        tour.leafSourceDepot();
        while (!stations.isEmpty()) {
            final OrderStation station = this.randomUtils.removeRandomElement(stations);
            final Order order = station.getOrder();
            if (station.isSource()) {
                tour.addSourceOrder(order);
                stations.add(order.getDestination());
            } else if (station.isDestination()) {
                tour.addDestinationOrder(order);
            } else {
                throw new IllegalStateException();
            }
        }
        tour.gotoDestinationDepot();

        return tour;
    }
}
