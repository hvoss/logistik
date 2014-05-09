package de.hsbremen.kss.configuration;

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

    /**
     * ctor
     * 
     * @param station
     *            the station of the order station.
     * @param timeWindow
     *            the time window.
     */
    public OrderStation(final Station station, final TimeWindow timeWindow) {
        Validate.notNull(station, "station is null");
        Validate.notNull(timeWindow, "timeWindow is null");

        this.station = station;
        this.timeWindow = timeWindow;
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

}
