package de.hsbremen.kss.model;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;

/**
 * The Class OrderLoadAction.
 */
public class OrderLoadAction extends OrderAction {

    /**
     * Instantiates a new order load action.
     * 
     * @param order
     *            the order
     */
    OrderLoadAction(final Order order) {
        super(order);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hsbremen.kss.model.Action#getStation()
     */
    @Override
    public final Station getStation() {
        return getOrder().getSource();
    }

}
