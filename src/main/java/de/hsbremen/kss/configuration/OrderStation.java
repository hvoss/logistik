package de.hsbremen.kss.configuration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

/**
 * The Class OrderStation.
 */
public final class OrderStation {

    /** order to which the order station belongs to. */
    private Order order;

    /** the station of the order station. */
    private final Station station;

    /** the time window. */
    private final TimeWindow timeWindow;

    /** the service time */
    private final Double serviceTime;

    /**
     * ctor
     * 
     * @param station
     *            the station of the order station.
     * @param timeWindow
     *            the time window.
     * @param serviceTime
     *            the service time
     */
    OrderStation(final Station station, final TimeWindow timeWindow, final Double serviceTime) {
        Validate.notNull(station, "station is null");
        Validate.notNull(timeWindow, "timeWindow is null");
        Validate.notNull(serviceTime, "serviceTime is null");

        this.station = station;
        this.timeWindow = timeWindow;
        this.serviceTime = serviceTime;
    }

    /**
     * converts a {@link Station} into a {@link OrderStation}. time window goes
     * from negative infinity to positive infinity. services time is 0.
     * 
     * @param station
     *            station to convert
     * @return converted {@link OrderStation}.
     */
    public static OrderStation convert(final Station station) {
        return new OrderStation(station, TimeWindow.INFINITY_TIMEWINDOW, 0d);
    }

    /**
     * converts a collection of {@link Station} into a set of
     * {@link OrderStation}. time window goes from negative infinity to positive
     * infinity. services time is 0.
     * 
     * @param stations
     *            stations to convert
     * @return converted {@link OrderStation}.
     */
    public static Set<OrderStation> convert(final Collection<Station> stations) {
        final HashSet<OrderStation> orderStations = new HashSet<>();

        for (final Station station : stations) {
            final OrderStation orderStation = OrderStation.convert(station);
            orderStations.add(orderStation);
        }

        return orderStations;
    }

    /**
     * Gets the order to which the order station belongs to.
     * 
     * @return the order to which the order station belongs to
     */
    public Order getOrder() {
        return this.order;
    }

    /**
     * Gets the station of the order station.
     * 
     * @return the station of the order station
     */
    public Station getStation() {
        return this.station;
    }

    /**
     * Gets the time window.
     * 
     * @return the time window
     */
    public TimeWindow getTimeWindow() {
        return this.timeWindow;
    }

    /**
     * Sets the order to which the order station belongs to.
     * 
     * @param order
     *            the new order to which the order station belongs to
     */
    void setOrder(final Order order) {
        this.order = order;
    }

    /**
     * Gets the service time.
     * 
     * @return the service time
     */
    public Double getServiceTime() {
        return this.serviceTime;
    }

    public static Set<OrderStation> getAllDestinationStations(final Iterable<Order> orders) {
        final Set<OrderStation> orderStations = new HashSet<>();

        for (final Order order : orders) {
            orderStations.add(order.getDestination());
        }

        return orderStations;
    }

    @Override
    public String toString() {
        return this.station.toString();
    }

    public boolean isSource() {
        return this == this.order.getSource();
    }

    public boolean isDestination() {
        return this == this.order.getDestination();
    }
}
