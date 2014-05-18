package de.hsbremen.kss.simpleconstruction;

import java.util.List;
import java.util.Set;

import de.hsbremen.kss.configuration.Station;

/**
 * "Simple" construction methods. Uses only stations.
 * 
 * @author henrik
 * 
 */
public interface SimpleConstruction {

    /**
     * finds the shortest route from start to end. The given stop overs could be
     * arrived in various sequence.
     * 
     * @param start
     *            start of the route
     * @param stopovers
     *            stop overs that must be reached.
     * @param end
     *            end of the route
     * @return the shortest route found.
     */
    List<Station> findShortestRoute(Station start, Set<Station> stopovers, Station end);

    /**
     * 
     * finds multiple routes with a maximum length. the first station out of
     * each route will be returned.
     * 
     * @param start
     *            start of the route
     * @param stopovers
     *            stop overs that must be reached.
     * @param end
     *            end of the route
     * @param maxLength
     *            max length of the route.
     * @return possible next stations
     */
    Set<Station> findPossibleNextStations(Station start, Set<Station> stopovers, Station end, double maxLength);
}
