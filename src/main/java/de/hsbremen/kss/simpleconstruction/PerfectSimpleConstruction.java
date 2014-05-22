package de.hsbremen.kss.simpleconstruction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hsbremen.kss.configuration.OrderStation;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;

/**
 * Implementation that finds the perfect solution. Should only be used with a
 * few stations.
 * 
 * @author henrik
 * 
 */
public final class PerfectSimpleConstruction implements SimpleConstruction {

    @Override
    public List<Station> findShortestRoute(final Station start, final Set<Station> stopovers, final Station end) {

        final Set<OrderStation> stopoversWithTimewindows = OrderStation.convert(stopovers);

        final List<OrderStation> route = findShortestRouteWithTimewindows(start, stopoversWithTimewindows, end, 0d, null);

        return Station.convert(route);
    }

    /**
     * recursion function that finds all routes.
     * 
     * @param vehicle
     *            the vehicle
     * @param routeConsumer
     *            end point of the recursion
     * @param time
     *            the actual time
     * @param length
     *            the actual length
     * @param nextStation
     *            the next station
     * @param stopovers
     *            remaining stop overs.
     * @param routeSoFar
     *            route so far
     * 
     */
    private void findBestRoute(final Vehicle vehicle, final List<OrderStation> routeSoFar, final double time, final double length,
            final OrderStation nextStation, final Set<OrderStation> stopovers, final RouteConsumer routeConsumer) {

        double newTime = time;
        double newLength = length;

        if (!routeSoFar.isEmpty()) {
            final OrderStation lastStation = routeSoFar.get(routeSoFar.size() - 1);
            final double distance = lastStation.getStation().distance(nextStation.getStation());
            newLength += distance;
            if (vehicle != null) {
                newTime += vehicle.calculateTavelingTime(distance);
            }
        }

        if (nextStation != null) {
            if (newTime > nextStation.getTimeWindow().getEnd()) {
                return;
            }

            if (newTime < nextStation.getTimeWindow().getStart()) {
                newTime = nextStation.getTimeWindow().getStart();
            }

            newTime += nextStation.getServiceTime();
        }

        final List<OrderStation> routeSoFarCopy = new ArrayList<>(routeSoFar);
        final Set<OrderStation> stopoversCopy = new HashSet<>(stopovers);
        if (nextStation != null) {
            routeSoFarCopy.add(nextStation);
            stopoversCopy.remove(nextStation);
        }

        if (stopoversCopy.size() == 0) {
            routeConsumer.foundRoute(routeSoFarCopy, newTime, newLength);
            return;
        }

        for (final OrderStation station : stopoversCopy) {
            findBestRoute(vehicle, routeSoFarCopy, newTime, newLength, station, stopoversCopy, routeConsumer);
        }
    }

    @Override
    public Set<Station> findPossibleNextStations(final Station start, final Set<Station> stopovers, final Station end, final double maxLength) {
        final Set<OrderStation> stopoversWithTimewindows = OrderStation.convert(stopovers);

        return findPossibleNextStationsWithTimewindows(start, stopoversWithTimewindows, end, maxLength, 0, null);
    }

    /**
     * calculates the length of a route.
     * 
     * @param start
     *            start of the route
     * @param stopovers
     *            sorted stop overs
     * @param end
     *            end of the route
     * @param stopoversLength
     *            length of the stopovers
     * @return length of the route
     */
    private static double length(final Station start, final List<OrderStation> stopovers, final Station end, final double stopoversLength) {
        double length = start.distance(stopovers.get(0).getStation());
        length += stopoversLength;
        length += stopovers.get(stopovers.size() - 1).getStation().distance(end);
        return length;
    }

    /**
     * Simple class that holds a route and its length.
     * 
     * @author henrik
     * 
     */
    private class RouteHolder {
        /** length of the route */
        private double length = Double.MAX_VALUE;
        /** the route */
        private List<OrderStation> route;
    }

    /**
     * end point of recursion.
     * 
     * @author henrik
     * 
     */
    private interface RouteConsumer {
        /**
         * route that is found by recursion algorithm
         * 
         * @param route
         *            route that is found by recursion algorithm
         */
        void foundRoute(List<OrderStation> route, double time, double length);
    }

    @Override
    public Set<Station> findPossibleNextStationsWithTimewindows(final Station start, final Set<OrderStation> stopovers, final Station end,
            final double maxLength, final double startTime, final Vehicle vehicle) {
        final Set<Station> possibleNextStations = new HashSet<>();

        if (!stopovers.isEmpty()) {

            final RouteConsumer routeConsumer = new RouteConsumer() {

                @Override
                public void foundRoute(final List<OrderStation> route, final double time, final double length) {
                    final double completeLength = length(start, route, end, length);
                    if (completeLength <= maxLength) {
                        possibleNextStations.add(route.get(0).getStation());
                    }
                }

            };

            for (final OrderStation stopover : stopovers) {
                final double time = startTime + vehicle.calculateTavelingTime(start, stopover.getStation());
                findBestRoute(vehicle, new ArrayList<OrderStation>(), time, 0d, stopover, stopovers, routeConsumer);
            }
        }

        return possibleNextStations;
    }

    @Override
    public List<OrderStation> findShortestRouteWithTimewindows(final Station start, final Set<OrderStation> stopovers, final Station end,
            final double startTime, final Vehicle vehicle) {
        final RouteHolder routeHolder = new RouteHolder();

        final Set<OrderStation> stopoversCopy = new HashSet<>(stopovers);

        final RouteConsumer routeConsumer = new RouteConsumer() {

            @Override
            public void foundRoute(final List<OrderStation> route, final double time, final double length) {
                final double routeLength = length(start, route, end, length);
                if (routeHolder.length > routeLength) {
                    routeHolder.length = routeLength;
                    routeHolder.route = route;
                }
            }

        };

        findBestRoute(vehicle, new ArrayList<OrderStation>(), startTime, 0d, null, stopoversCopy, routeConsumer);

        return routeHolder.route;
    }
}
