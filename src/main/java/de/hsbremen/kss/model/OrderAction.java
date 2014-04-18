package de.hsbremen.kss.model;

import de.hsbremen.kss.configuration.Order;

/**
 * The Class OrderAction.
 */
abstract class OrderAction implements Action {

    /** the order. */
    private final Order order;

    /**
     * Instantiates a new order action.
     * 
     * @param order
     *            the order
     */
    OrderAction(final Order order) {
        this.order = order;
    }

    /**
     * Gets the order.
     * 
     * @return the order
     */
    public Order getOrder() {
        return this.order;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " : " + this.order;
    }
}
