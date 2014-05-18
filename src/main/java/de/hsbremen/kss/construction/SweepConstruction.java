package de.hsbremen.kss.construction;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.StationAngleComparator;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Tour;
import de.hsbremen.kss.simpleconstruction.SimpleConstruction;
import de.hsbremen.kss.util.RandomUtils;

/**
 * The Class RadialConstruction.
 */
public final class SweepConstruction extends BaseConstruction {

    /** logging interface. */
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(SweepConstruction.class);

    /** station to start */
    private Station startStation;

    /** some utils for random numbers */
    private final RandomUtils randomUtils;

    /**
     * indicates whether to go forward (against the clock) or backward (with the
     * clock).
     */
    private boolean forward;

    /**
     * ctor.
     * 
     * @param simpleConstruction
     *            construction methods to find simple routes
     * @param randomUtils
     *            some utils for random numbers
     */
    public SweepConstruction(final SimpleConstruction simpleConstruction, final RandomUtils randomUtils) {
        super(simpleConstruction);
        this.randomUtils = randomUtils;
    }

    @Override
    protected Station nextStation(final Tour tour, final Collection<Station> stations) {
        final Station actualStation = tour.actualStation();
        final Station sourceDepot = tour.getVehicle().getSourceDepot();

        final StationAngleComparator comparator = new StationAngleComparator(sourceDepot, actualStation, this.isForward());
        return Collections.min(stations, comparator);
    }

    @Override
    protected Vehicle nextVehicle(final Collection<Vehicle> vehicle) {
        return this.randomUtils.randomElement(vehicle);
    }

    @Override
    protected List<Order> loadOrderSequence(final Collection<Order> orders) {
        return this.randomUtils.shuffle(orders);
    }

    /**
     * Gets the station to start.
     * 
     * @return the station to start
     */
    public Station getStartStation() {
        return this.startStation;
    }

    /**
     * Sets the station to start.
     * 
     * @param startStation
     *            the new station to start
     */
    public void setStartStation(final Station startStation) {
        this.startStation = startStation;
    }

    /**
     * Checks if is indicates whether to go forward (against the clock) or
     * backward (with the clock).
     * 
     * @return the indicates whether to go forward (against the clock) or
     *         backward (with the clock)
     */
    public boolean isForward() {
        return this.forward;
    }

    /**
     * Sets the indicates whether to go forward (against the clock) or backward
     * (with the clock).
     * 
     * @param forward
     *            the new indicates whether to go forward (against the clock) or
     *            backward (with the clock)
     */
    public void setForward(final boolean forward) {
        this.forward = forward;
    }

    @Override
    public void logStatistic() {
        // TODO Auto-generated method stub

    }
}
