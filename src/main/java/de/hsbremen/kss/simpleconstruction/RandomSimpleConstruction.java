package de.hsbremen.kss.simpleconstruction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hsbremen.kss.configuration.OrderStation;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.util.RandomUtils;

public class RandomSimpleConstruction implements SimpleConstruction {

    private final RandomUtils randomUtils;

    public RandomSimpleConstruction(final RandomUtils randomUtils) {
        this.randomUtils = randomUtils;
    }

    @Override
    public List<Station> findShortestRoute(final Station start, final Set<Station> stopovers, final Station end) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<OrderStation> findShortestRouteWithTimewindows(final Station start, final Set<OrderStation> stopovers, final Station end,
            final double startTime, final Vehicle vehicle) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Station> findPossibleNextStations(final Station start, final Set<Station> stopovers, final Station end, final double maxLength) {
        return new HashSet<>(stopovers);
    }

    @Override
    public Set<Station> findPossibleNextStationsWithTimewindows(final Station start, final Set<OrderStation> stopovers, final Station end,
            final double maxLength, final double startTime, final Vehicle vehicle) {
        return new HashSet<>(Station.convert(stopovers));
    }

}
