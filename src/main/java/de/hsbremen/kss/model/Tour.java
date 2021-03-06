package de.hsbremen.kss.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.TimeWindow;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.util.TimeUtils;

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
    private Integer actualLoadingAmount = 0;

    /** identifier of the tour */
    private int id;

    /** indicates whether the tour is locked or not */
    private boolean locked = true;

    /**
     * Instantiates a new tour.
     * 
     * @param vehicle
     *            the vehicle
     * @param id
     *            identifier of the tour
     */
    public Tour(final Vehicle vehicle) {
        this.vehicle = vehicle;

        this.actions = new LinkedList<>();

    }

    Tour(final Tour tour, final int id) {
        if (!tour.locked) {
            throw new IllegalArgumentException();
        }

        this.vehicle = tour.vehicle;
        this.locked = tour.locked;
        this.actions = tour.actions;
        this.id = id;

    }

    /**
     * adds the {@link FromDepotAction} to the tour.
     */
    public void leafSourceDepot() {
        if (!this.actions.isEmpty()) {
            throw new IllegalStateException();
        }

        this.actions.add(new FromDepotAction(this.vehicle.getSourceDepot(), this.vehicle.getTimeWindow()));
        this.locked = false;
    }

    /**
     * adds the {@link ToDepotAction} to the tour.
     */
    public void gotoDestinationDepot() {
        this.actions.add(new ToDepotAction(this.vehicle.getDestinationDepot(), this.vehicle.getTimeWindow()));
        this.locked = true;
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
        this.actualLoadingAmount += sourceOrder.getAmount();
        addOrderAction(unloadAction);

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
        this.actualLoadingAmount -= destinationOrder.getAmount();
        addOrderAction(unloadAction);
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

    private void addOrderAction(final OrderAction orderAction) {
        final Double start = orderAction.timewindow().getStart();
        final Station nextStation = orderAction.getStation();
        final Station actualStation = actualStation();

        final double nextTime = this.actualTime() + this.vehicle.calculateTavelingTime(actualStation, nextStation);

        if (start > nextTime) {
            final TimeWindow timeWindow = this.vehicle.getTimeWindow();
            final WaitingAction waitingAction = new WaitingAction(nextStation, timeWindow, nextTime, start);
            addAction(waitingAction);
        }
        addAction(orderAction);
    }

    /**
     * adds a action to the tour.
     * 
     * @param action
     *            action to add
     */
    private void addAction(final Action action) {
        checkLocked();
        Validate.notNull(action, "action is null");
        this.actions.add(action);
    }

    /**
     * Logs the tour on the logging interface.
     */
    public void logTour() {
        Action lastAction = null;
        Tour.LOG.info("Tour #" + this.id + " (Vehicle: " + this.vehicle + "):");
        Tour.LOG.info("length: " + Precision.round(length(), 2) + "km");
        Tour.LOG.info("duration: " + Precision.round(actualDuration(), 2) + "h");

        double time = this.vehicle.getTimeWindow().getStart();

        for (final Action action : this.actions) {
            if (lastAction != null) {
                final Station source = lastAction.getStation();
                final Station destination = action.getStation();

                if (!source.equals(destination)) {
                    final String startTime = TimeUtils.convertToClockString(time);
                    final double excactDuration = this.vehicle.calculateTavelingTime(source, destination);
                    final double duration = Precision.round(excactDuration, 1);
                    time += excactDuration;
                    final String endTime = TimeUtils.convertToClockString(time);

                    final long distance = Math.round(source.distance(destination));
                    Tour.LOG.info(source.getName() + " => " + destination.getName() + " (" + distance + " km) time: " + startTime + " -> " + endTime
                            + " (" + duration + "h)");
                }
            }
            time += action.duration();
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
     * returns the duration of the order
     * 
     * @return duration of the order
     */
    public double actualDuration() {
        double time = 0;

        Station actualStation = this.vehicle.getSourceDepot();

        for (final Action action : this.actions) {
            final Station station = action.getStation();
            time += this.vehicle.calculateTavelingTime(actualStation, station);
            if (action instanceof OrderAction) {
                final OrderAction orderAction = (OrderAction) action;
                time += orderAction.duration();
            }
            actualStation = station;
        }

        return time;
    }

    public double actualTime() {
        return this.vehicle.getTimeWindow().getStart() + actualDuration();
    }

    /**
     * Gets the actual loading weight.
     * 
     * @return the actual loading weight
     */
    public Integer getActualLoadingAmount() {
        return this.actualLoadingAmount;
    }

    /**
     * Free space.
     * 
     * @return the integer
     */
    public Integer freeAmount() {
        return this.vehicle.getCapacity() - this.actualLoadingAmount;
    }

    /**
     * returns free time of the vehicle.
     * 
     * @return free time of the vehicle.
     */
    public double freeTime() {
        double freeTime = getVehicle().getTimeWindow().timespan() - actualDuration();
        freeTime -= Order.completeUnloadDuration(notDeliveredOrders());
        return freeTime;
    }

    /**
     * returns the actual (last) station.
     * 
     * @return the actual (last) station
     */
    public Station actualStation() {
        if (this.actions.isEmpty()) {
            return null;
        }

        final Action lastAction = this.actions.get(this.actions.size() - 1);
        return lastAction.getStation();
    }

    /**
     * returns true if the tour contains at least one {@link OrderAction}.
     * 
     * @return true if the tour contains at least one {@link OrderAction}.
     */
    public boolean hasOrderActions() {
        for (final Action action : this.actions) {
            if (action instanceof OrderAction) {
                return true;
            }
        }
        return false;
    }

    /**
     * returns a list of all loaded orders, but not yet delivered orders.
     * 
     * @return a list of all loaded orders, but not yet delivered orders.
     */
    public Set<Order> notDeliveredOrders() {
        // XXX caching
        final Set<Order> orders = new HashSet<>();
        for (final Action action : this.actions) {
            if (action instanceof OrderLoadAction) {
                final OrderLoadAction loadAction = (OrderLoadAction) action;
                orders.add(loadAction.getOrder());
            } else if (action instanceof OrderUnloadAction) {
                final OrderUnloadAction unloadAction = (OrderUnloadAction) action;
                if (!orders.remove(unloadAction.getOrder())) {
                    throw new IllegalStateException();
                }
            }
        }
        return orders;
    }

    /**
     * returns the free length.
     * 
     * @return the free length.
     */
    public double freeLength() {
        return freeTime() * this.vehicle.getVelocity();
    }

    @Override
    public String toString() {
        return "Tour [vehicle=" + this.vehicle + ", actions=" + this.actions + ", actualStation()=" + actualStation() + ", freeLength()="
                + freeLength() + "]";
    }

    /**
     * Gets the identifier of the tour.
     * 
     * @return the identifier of the tour
     */
    public int getId() {
        return this.id;
    }

    /**
     * checks whether the given order is part of the tour.
     * 
     * @param order
     *            the order
     * @return true, if the given order is part of the tour.
     */
    public boolean contains(final Order order) {
        return getOrders().contains(order);
    }

    /**
     * returns the first action.
     * 
     * @return the first action.
     */
    public Action firstAction() {
        return this.actions.get(0);
    }

    /**
     * returns the last action.
     * 
     * @return the last action.
     */
    public Action lastAction() {
        return this.actions.get(this.actions.size() - 1);
    }

    public List<OrderAction> getOrderActions() {
        final List<OrderAction> orderActions = new ArrayList<>();

        for (final Action action : this.actions) {
            if (action instanceof OrderAction) {
                orderActions.add((OrderAction) action);
            }
        }

        return orderActions;
    }

    public void addOtherAction(final Action action) {
        if (action instanceof OrderLoadAction) {
            addSourceOrder(((OrderLoadAction) action).getOrder());
        } else if (action instanceof OrderUnloadAction) {
            addDestinationOrder(((OrderUnloadAction) action).getOrder());
        } else if (action instanceof WaitingAction) {

        } else {
            throw new IllegalArgumentException();
        }
    }

    public void addOtherActions(final Iterable<? extends Action> actions) {
        for (final Action action : actions) {
            addOtherAction(action);
        }
    }

    /**
     * checks whether the tour is locked or not. if the tour is locked, a
     * exception will be thrown.
     */
    private void checkLocked() {
        if (this.locked) {
            throw new IllegalStateException("the tour is locked.");
        }
    }

    /**
     * finds the longest of the given tours.
     * 
     * @param tours
     *            tours to search for
     * @return the longest tour.
     */
    public static Tour findLongestTour(final Iterable<Tour> tours) {
        Tour longestTour = null;
        for (final Tour tour : tours) {
            if (longestTour == null || longestTour.length() < tour.length()) {
                longestTour = tour;
            }
        }
        return longestTour;
    }

    /**
     * finds the shortest of the given tours.
     * 
     * @param tours
     *            tours to search for
     * @return the longest tour.
     */
    public static Tour findShortestTour(final Collection<Tour> tours) {
        Tour shortestTour = null;
        for (final Tour tour : tours) {
            if (shortestTour == null || shortestTour.length() > tour.length()) {
                shortestTour = tour;
            }
        }
        return shortestTour;
    }

    public double waitingTime() {
        double waitingTime = 0.0;

        for (final Action action : this.actions) {
            if (action instanceof WaitingAction) {
                waitingTime += action.duration();
            }
        }

        return waitingTime;
    }

    public double delayTime() {
        double delayTime = 0.0;
        double time = this.vehicle.getTimeWindow().getStart();

        Station lastStation = this.vehicle.getSourceDepot();
        for (final Action action : this.actions) {
            time += this.vehicle.calculateTavelingTime(lastStation, action.getStation());

            if (time > action.timewindow().getEnd()) {
                delayTime += time - action.timewindow().getEnd();
            }

            time += action.duration();

            lastStation = action.getStation();
        }

        return delayTime;
    }

}
