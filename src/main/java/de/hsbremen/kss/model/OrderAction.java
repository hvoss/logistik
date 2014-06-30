package de.hsbremen.kss.model;

import java.util.ArrayList;
import java.util.List;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.OrderStation;
import de.hsbremen.kss.configuration.Station;

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

    public static List<OrderStation> extractOrderStations(final List<OrderAction> actions) {
        final ArrayList<OrderStation> stations = new ArrayList<>(actions.size());

        for (final OrderAction action : actions) {
            stations.add(action.orderStation());
        }

        return stations;
    }

    public abstract OrderStation orderStation();

    @Override
    public final Station getStation() {
        return orderStation().getStation();
    }

    @Override
    public final String toString() {
        return getClass().getSimpleName() + " : " + this.order;
    }

    public boolean isSource() {
        return getClass().equals(OrderLoadAction.class);
    }

    public boolean isDestination() {
        return getClass().equals(OrderUnloadAction.class);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.order == null) ? 0 : this.order.hashCode());
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
        final OrderAction other = (OrderAction) obj;
        if (this.order == null) {
            if (other.order != null) {
                return false;
            }
        } else if (!this.order.equals(other.order)) {
            return false;
        }
        return true;
    }
}
