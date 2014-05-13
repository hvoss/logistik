package de.hsbremen.kss.model;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;

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
    OrderUnloadAction(final Order order) {
        super(order);
    }

    @Override
    public Station getStation() {
        return getOrder().getDestinationStation();
    }

    @Override
    public double duration() {
        return getOrder().getDestination().getServiceTime();
    }
}
