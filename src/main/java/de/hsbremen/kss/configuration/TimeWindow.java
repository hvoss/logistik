package de.hsbremen.kss.configuration;

import org.apache.commons.lang3.Validate;

import de.hsbremen.kss.util.TimeUtils;

/**
 * The Class TimeWindow.
 */
public final class TimeWindow {

    /** the order station to which these time window belongs to. */
    private OrderStation orderStation;

    /** start of the time window. */
    private final Double start;

    /** end of the time window. */
    private final Double end;

    /** infinity time window */
    static final TimeWindow INFINITY_TIMEWINDOW = new TimeWindow(Double.MIN_VALUE, Double.MAX_VALUE);

    /**
     * ctor.
     * 
     * @param start
     *            start of the time window
     * @param end
     *            end of the time window
     */
    public TimeWindow(final Double start, final Double end) {
        Validate.notNull(start, "start is null");
        Validate.notNull(end, "end is null");

        Validate.isTrue(start < end, "start after end");

        this.start = start;
        this.end = end;
    }

    /**
     * Gets the order station to which these time window belongs to.
     * 
     * @return the order station to which these time window belongs to
     */
    public OrderStation getOrderStation() {
        return this.orderStation;
    }

    /**
     * Sets the order station to which these time window belongs to.
     * 
     * @param orderStation
     *            the new order station to which these time window belongs to
     */
    void setOrderStation(final OrderStation orderStation) {
        this.orderStation = orderStation;
    }

    /**
     * Gets the start of the time window.
     * 
     * @return the start of the time window
     */
    public Double getStart() {
        return this.start;
    }

    /**
     * Gets the end of the time window.
     * 
     * @return the end of the time window
     */
    public Double getEnd() {
        return this.end;
    }

    /**
     * returns the timespan in hours.
     * 
     * @return the timespan in hours
     */
    public double timespan() {
        return this.end - this.start;
    }

    /**
     * checks if the value is between start and end (inclusive).
     * 
     * @param value
     *            value to check.
     * @return true, if the value is between start and end.
     */
    public boolean between(final Double value) {
        return this.start <= value && value <= this.end;
    }

    @Override
    public String toString() {
        return "[" + TimeUtils.convertToClockString(this.start) + " - " + TimeUtils.convertToClockString(this.end) + "]";
    }
}
