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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.order == null) ? 0 : this.order.hashCode());
        result = prime * result + ((this.serviceTime == null) ? 0 : this.serviceTime.hashCode());
        result = prime * result + ((this.station == null) ? 0 : this.station.hashCode());
        result = prime * result + ((this.timeWindow == null) ? 0 : this.timeWindow.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OrderStation other = (OrderStation) obj;
        if (this.order == null) {
            if (other.order != null) {
                return false;
            }
        } else if (!this.order.equals(other.order)) {
            return false;
        }
        if (this.serviceTime == null) {
            if (other.serviceTime != null) {
                return false;
            }
        } else if (!this.serviceTime.equals(other.serviceTime)) {
            return false;
        }
        if (this.station == null) {
            if (other.station != null) {
                return false;
            }
        } else if (!this.station.equals(other.station)) {
            return false;
        }
        if (this.timeWindow == null) {
            if (other.timeWindow != null) {
                return false;
            }
        } else if (!this.timeWindow.equals(other.timeWindow)) {
            return false;
        }
        return true;
    }
}
