package de.hsbremen.kss.construction;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.simpleconstruction.SimpleConstruction;
import de.hsbremen.kss.util.RandomUtils;

/**
 * try to find a solution by random.
 * 
 * @author henrik
 * 
 */
public final class RandomConstruction extends BaseConstruction {

    /** logging interface */
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(RandomConstruction.class);

    /** some utils for random numbers */
    private final RandomUtils randomUtils;

    /**
     * ctor.
     * 
     * @param simpleConstruction
     *            construction methods to find simple routes
     * @param randomUtils
     *            some utils for random numbers
     */
    public RandomConstruction(final SimpleConstruction simpleConstruction, final RandomUtils randomUtils) {
        super(simpleConstruction);
        this.randomUtils = randomUtils;
    }

    @Override
    public void logStatistic() {
        // TODO Auto-generated method stub

    }

    @Override
    protected Station nextStation(final Tour tour, final Collection<Station> stations) {
        return this.randomUtils.randomElement(stations);
    }

    @Override
    protected Vehicle nextVehicle(final Collection<Vehicle> vehicle) {
        return this.randomUtils.randomElement(vehicle);
    }

    @Override
    protected List<Order> loadOrderSequence(final Collection<Order> orders) {
        return this.randomUtils.shuffle(orders);
    }
}
