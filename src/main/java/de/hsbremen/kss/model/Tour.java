package de.hsbremen.kss.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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

    /** visited stations in order. */
    private final List<Station> stations;

    /** the orders. */
    private final Set<Order> orders;

    /**
     * Instantiates a new tour.
     * 
     * @param vehicle
     *            the vehicle
     */
    public Tour(final Vehicle vehicle) {
        this.vehicle = vehicle;
        this.stations = new ArrayList<Station>();
        this.orders = new HashSet<Order>();

        this.stations.add(this.vehicle.getSourceDepot());
        this.stations.add(this.vehicle.getDestinationDepot());
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
    public List<Station> getStations() {
        return Collections.unmodifiableList(this.stations);
    }

    /**
     * Gets the orders.
     * 
     * @return the orders
     */
    public Set<Order> getOrders() {
        return Collections.unmodifiableSet(this.orders);
    }

    /**
     * adds a station.
     * 
     * @param station
     *            station to add
     */
    public void addStation(final Station station) {
        Validate.notNull(station);
        final int index = this.stations.size() - 1;
        this.stations.add(index, station);
    }

    /**
     * adds a order.
     * 
     * @param order
     *            order to add
     */
    public void addOrder(final Order order) {
        Validate.notNull(order);
        this.orders.add(order);
    }

    /**
     * Adds an order and a station to the tour.
     * 
     * @param order
     *            order to add
     * @param station
     *            station to add
     */
    public void addOrderAndStation(final Order order, final Station station) {
        addOrder(order);
        addStation(station);
    }

    /**
     * Logs the tour on the logging interface.
     */
    public void logTour() {
        Station source = this.stations.get(0);
        for (int i = 1; i < this.stations.size(); i++) {
            final Station destination = this.stations.get(i);
            Tour.LOG.info(source.getName() + " => " + destination.getName() + " (" + Math.round(source.distance(destination)) + " km)");
            source = destination;
        }
    }

    /**
     * calculates the length of the tour and returns the
     * 
     * @return the length of the tour
     */
    public double length() {
        // XXX cache length
        double length = 0;
        Station source = this.stations.get(0);
        for (int i = 1; i < this.stations.size(); i++) {
            final Station destination = this.stations.get(i);
            length += source.distance(destination);
            source = destination;
        }
        return length;
    }
}
