package de.hsbremen.kss.model;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.TimeWindow;

/**
 * The Class OrderLoadAction.
 */
public final class OrderLoadAction extends OrderAction {

    /**
     * Instantiates a new order load action.
     * 
     * @param order
     *            the order
     */
    OrderLoadAction(final Order order) {
        super(order);
    }

    @Override
    public Station getStation() {
        return getOrder().getSourceStation();
    }

    @Override
    public double duration() {
        return getOrder().getSource().getServiceTime();
    }

    @Override
    public TimeWindow timewindow() {
        return getOrder().getSource().getTimeWindow();
    }

}
