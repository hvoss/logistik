package de.hsbremen.kss.simpleconstruction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hsbremen.kss.configuration.Station;

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

        final RouteHolder routeHolder = new RouteHolder();

        final Set<Station> stopoversCopy = new HashSet<>(stopovers);

        final RouteConsumer routeConsumer = new RouteConsumer() {

            @Override
            public void foundRoute(final List<Station> route) {
                final double length = length(start, route, end);
                if (routeHolder.length > length) {
                    routeHolder.length = length;
                    routeHolder.route = route;
                }
            }

        };

        final ArrayList<Station> routeSoFar = new ArrayList<Station>();
        findBestRoute(routeSoFar, stopoversCopy, routeConsumer);

        final ArrayList<Station> bestRoute = new ArrayList<>();
        bestRoute.add(start);
        bestRoute.addAll(routeHolder.route);
        bestRoute.add(end);

        return bestRoute;
    }

    /**
     * recursion function that finds all routes.
     * 
     * @param routeSoFar
     *            route so far
     * @param stopovers
     *            remaining stop overs.
     * @param routeConsumer
     *            end point of the recursion
     */
    private void findBestRoute(final List<Station> routeSoFar, final Set<Station> stopovers, final RouteConsumer routeConsumer) {

        if (stopovers.size() == 0) {
            routeConsumer.foundRoute(routeSoFar);
            return;
        }

        for (final Station station : stopovers) {
            final List<Station> routeCopy = new ArrayList<>(routeSoFar);
            final Set<Station> stopoversCopy = new HashSet<>(stopovers);

            stopoversCopy.remove(station);
            routeCopy.add(station);

            findBestRoute(routeCopy, stopoversCopy, routeConsumer);
        }
    }

    @Override
    public Set<Station> findPossibleNextStations(final Station start, final Set<Station> stopovers, final Station end, final double maxLength) {
        final Set<Station> possibleNextStations = new HashSet<>();

        final Set<Station> stopoversCopy = new HashSet<>(stopovers);
        if (!stopoversCopy.isEmpty()) {

            final RouteConsumer routeConsumer = new RouteConsumer() {

                @Override
                public void foundRoute(final List<Station> route) {
                    final double length = length(start, route, end);
                    if (length <= maxLength) {
                        possibleNextStations.add(route.get(0));
                    }
                }

            };

            final ArrayList<Station> routeSoFar = new ArrayList<Station>();
            findBestRoute(routeSoFar, stopoversCopy, routeConsumer);
        }

        return possibleNextStations;
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
     * @return length of the route
     */
    private static double length(final Station start, final List<Station> stopovers, final Station end) {
        double length = start.distance(stopovers.get(0));
        length += Station.length(stopovers);
        length += stopovers.get(stopovers.size() - 1).distance(end);
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
        private List<Station> route;
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
        void foundRoute(List<Station> route);
    }
}
