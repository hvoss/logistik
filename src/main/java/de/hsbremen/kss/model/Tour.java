package de.hsbremen.kss.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;

/**
 * The Class Tour.
 */
public final class Tour {

    /** logging interface. */
    private static final Logger LOG = LoggerFactory.getLogger(Tour.class);

    /** the vehicle which performed the tour. */
    private final Vehicle vehicle;

    /** actions performed within the tour. */
    private final List<Action> actions;

    /** The actual loading weight. */
    private Integer actualLoadingWeight = 0;

    /**
     * Instantiates a new tour.
     * 
     * @param vehicle
     *            the vehicle
     */
    public Tour(final Vehicle vehicle) {
        this.vehicle = vehicle;

        this.actions = new LinkedList<>();
        this.actions.add(new FromDepotAction(this.vehicle.getSourceDepot()));
        this.actions.add(new ToDepotAction(this.vehicle.getDestinationDepot()));
    }

    /**
     * Gets the vehicle which performed the tour.
     * 
     * @return the vehicle which performed the tour
     */
    public Vehicle getVehicle() {
        return this.vehicle;
    }

    /**
     * Gets the visited stations in order.
     * 
     * @return the visited stations in order
     */
    public Set<Station> getStations() {
        final Set<Station> stations = new HashSet<>();

        // XXX cache

        for (final Action action : this.actions) {
            stations.add(action.getStation());
        }

        return stations;
    }

    /**
     * Gets the orders.
     * 
     * @return the orders
     */
    public Set<Order> getOrders() {
        final Set<Order> orders = new HashSet<>();

        // XXX cache

        for (final Action action : this.actions) {
            if (action instanceof OrderAction) {
                orders.add(((OrderAction) action).getOrder());
            }
        }

        return orders;
    }

    /**
     * Gets the actions performed within the tour.
     * 
     * @return the actions performed within the tour
     */
    public List<Action> getActions() {
        return Collections.unmodifiableList(this.actions);
    }

    /**
     * adds a visited source order.
     * 
     * @param sourceOrder
     *            source order to add
     */
    public void addSourceOrder(final Order sourceOrder) {
        Validate.notNull(sourceOrder, "source order is null");
        final OrderLoadAction unloadAction = new OrderLoadAction(sourceOrder);
        this.actualLoadingWeight += sourceOrder.weightOfProducts();
        addAction(unloadAction);

    }

    /**
     * adds a collection of visited source orders.
     * 
     * @param sourceOrders
     *            collection to add
     */
    public void addSourceOrders(final Collection<Order> sourceOrders) {
        for (final Order sourceOrder : sourceOrders) {
            addSourceOrder(sourceOrder);
        }
    }

    /**
     * adds a visited destination order.
     * 
     * @param destinationOrder
     *            destination order to add
     */
    public void addDestinationOrder(final Order destinationOrder) {
        Validate.notNull(destinationOrder, "destination order is null");
        final OrderUnloadAction unloadAction = new OrderUnloadAction(destinationOrder);
        this.actualLoadingWeight -= destinationOrder.weightOfProducts();
        addAction(unloadAction);
    }

    /**
     * adds a collection of visited destination orders.
     * 
     * @param destinationOrders
     *            collection to add
     */
    public void addDestinationOrders(final Collection<Order> destinationOrders) {
        for (final Order destinationOrder : destinationOrders) {
            addDestinationOrder(destinationOrder);
        }
    }

    /**
     * adds a action to the tour.
     * 
     * @param action
     *            action to add
     */
    private void addAction(final Action action) {
        Validate.notNull(action, "action is null");
        final int index = this.actions.size() - 1;
        this.actions.add(index, action);
    }

    /**
     * Logs the tour on the logging interface.
     */
    public void logTour() {
        Action lastAction = null;
        for (final Action action : this.actions) {
            if (lastAction != null) {
                final Station source = lastAction.getStation();
                final Station destination = action.getStation();
                if (!source.equals(destination)) {
                    Tour.LOG.info(source.getName() + " => " + destination.getName() + " (" + Math.round(source.distance(destination)) + " km)");
                }
            }
            Tour.LOG.info(action.toString());
            lastAction = action;
        }
    }

    /**
     * calculates the length of the tour and returns the
     * 
     * @return the length of the tour
     */
    public double length() {
        double length = 0;

        // XXX cache length
        Action lastAction = null;
        for (final Action action : this.actions) {
            if (lastAction != null) {
                final Station source = lastAction.getStation();
                final Station destination = action.getStation();
                if (!source.equals(destination)) {
                    length += source.distance(destination);

                }
            }
            lastAction = action;
        }

        return length;
    }

    /**
     * Gets the actual loading weight.
     * 
     * @return the actual loading weight
     */
    public Integer getActualLoadingWeight() {
        return this.actualLoadingWeight;
    }

    /**
     * Free space.
     * 
     * @return the integer
     */
    public Integer freeSpace() {
        return this.vehicle.maxCapacityWeight() - this.actualLoadingWeight;
    }
}
