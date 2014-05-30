package de.hsbremen.kss.model;

import de.hsbremen.kss.configuration.Order;

/**
 * The Class OrderAction.
 */
public abstract class OrderAction implements Action {

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
    public final Order getOrder() {
        return this.order;
    }

    @Override
    public final String toString() {
        return getClass().getSimpleName() + " : " + this.order;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderAction other = (OrderAction) obj;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		return true;
	}
}
