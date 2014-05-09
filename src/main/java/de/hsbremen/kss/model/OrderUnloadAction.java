package de.hsbremen.kss.model;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;

/**
 * The Class OrderUnloadAction.
 */
public class OrderUnloadAction extends OrderAction {

    /**
     * Instantiates a new order unload action.
     * 
     * @param order
     *            the order
     */
    OrderUnloadAction(final Order order) {
        super(order);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hsbremen.kss.model.Action#getStation()
     */
    @Override
    public final Station getStation() {
        return getOrder().getDestinationStation();
    }
}
