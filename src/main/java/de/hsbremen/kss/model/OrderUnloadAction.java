package de.hsbremen.kss.model;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.OrderStation;
import de.hsbremen.kss.configuration.TimeWindow;

/**
 * The Class OrderUnloadAction.
 */
public final class OrderUnloadAction extends OrderAction {

    /**
     * Instantiates a new order unload action.
     * 
     * @param order
     *            the order
     */
    public OrderUnloadAction(final Order order) {
        super(order);
    }

    @Override
    public double duration() {
        return getOrder().getDestination().getServiceTime();
    }

    @Override
    public TimeWindow timewindow() {
        return getOrder().getDestination().getTimeWindow();
    }

    @Override
    public OrderStation orderStation() {
        return getOrder().getDestination();
    }
}
