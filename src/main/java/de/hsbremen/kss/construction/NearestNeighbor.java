package de.hsbremen.kss.construction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.simpleconstruction.SimpleConstruction;

/**
 * always goes to the nearest station.
 * 
 * @author henrik
 * 
 */
public final class NearestNeighbor extends BaseConstruction {

    /** logging interface */
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(RandomConstruction.class);

    /**
     * ctor.
     * 
     * @param simpleConstruction
     *            construction methods to find simple routes
     */
    public NearestNeighbor(final SimpleConstruction simpleConstruction) {
        super(simpleConstruction);
    }

    @Override
    protected Station nextStation(final Tour tour, final Collection<Station> stations) {
        final Station actualStation = tour.actualStation();

        return actualStation.nearestStation(stations);
    }

    @Override
    protected Vehicle nextVehicle(final Collection<Vehicle> vehicle) {
        return CollectionUtils.get(vehicle, 0);
    }

    @Override
    protected List<Order> loadOrderSequence(final Collection<Order> orders) {
        return new ArrayList<>(orders);
    }

    @Override
    public void logStatistic() {
        // TODO Auto-generated method stub
    }

}
