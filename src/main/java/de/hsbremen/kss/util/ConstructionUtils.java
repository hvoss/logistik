package de.hsbremen.kss.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.model.Tour;

public class ConstructionUtils {

    /**
     * static class
     */
    private ConstructionUtils() {

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
        return Order.filterOrdersByWeight(notVisitedSourceOrders, tourSoFar.freeSpace());
    }

    public static Set<Order> processableDestinationOrders(final Collection<Order> allOrders, final Collection<Order> visitedSourceOrders,
            final Collection<Order> visitedDestinationOrders, final Tour tourSoFar) {
        final Set<Order> notVisitedDestinationOrders = new HashSet<>(allOrders);
        notVisitedDestinationOrders.removeAll(visitedDestinationOrders);
        return new HashSet<>(CollectionUtils.intersection(visitedSourceOrders, notVisitedDestinationOrders));
    }
}
