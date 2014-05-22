package de.hsbremen.kss.configuration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import de.hsbremen.kss.util.TimeUtils;

// TODO: Auto-generated Javadoc
/**
 * represents a order of a customer.
 * 
 * @author henrik
 * 
 */
public final class Order {

    /** the id. */
    private final Integer id;

    /** the name. */
    private final String name;

    /** the source. */
    private final OrderStation source;

    /** the destination. */
    private final OrderStation destination;

    /** the product. */
    private final Product product;

    /** the amount. */
    private final Integer amount;

    /**
     * Instantiates a new order.
     * 
     * @param id
     *            the id
     * @param name
     *            the name
     * @param source
     *            the source
     * @param destination
     *            the destination
     * @param product
     *            the product
     * @param amount
     *            the amount
     */
    Order(final Integer id, final String name, final OrderStation source, final OrderStation destination, final Product product, final Integer amount) {
        Validate.notNull(id, "id is null");
        Validate.notNull(name, "name is null");
        Validate.notNull(source, "source station is null");
        Validate.notNull(destination, "destination station is null");
        Validate.notNull(product, "product station is null");
        Validate.notNull(amount, "amount station is null");

        this.id = id;
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.source.setOrder(this);
        this.destination.setOrder(this);
        this.product = product;
        this.amount = amount;

        source.getStation().addSourceOrder(this);
        destination.getStation().addDestinationOrder(this);
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the source station.
     * 
     * @return the source station
     */
    public Station getSourceStation() {
        return this.source.getStation();
    }

    /**
     * Gets the destination station.
     * 
     * @return the destination station
     */
    public Station getDestinationStation() {
        return this.destination.getStation();
    }

    /**
     * Gets the source.
     * 
     * @return the source
     */
    public OrderStation getSource() {
        return this.source;
    }

    /**
     * Gets the destination.
     * 
     * @return the destination
     */
    public OrderStation getDestination() {
        return this.destination;
    }

    /**
     * Gets the product.
     * 
     * @return the product
     */
    public Product getProduct() {
        return this.product;
    }

    /**
     * Gets the amount.
     * 
     * @return the amount
     */
    public Integer getAmount() {
        return this.amount;
    }

    /**
     * extracts all source stations of a collection of {@link Order}s.
     * 
     * @param orders
     *            collection of orders to check
     * @return a set of all source stations
     */
    public static Set<Station> getAllSourceStations(final Collection<Order> orders) {
        final Set<Station> stations = new HashSet<>(orders.size());

        for (final Order order : orders) {
            stations.add(order.getSourceStation());
        }

        return stations;
    }

    /**
     * extracts all destination stations of a collection of {@link Order}s.
     * 
     * @param orders
     *            collection of orders to check
     * @return a set of all destination stations
     */
    public static Set<Station> getAllDestinationStations(final Collection<Order> orders) {
        final Set<Station> stations = new HashSet<>(orders.size());

        for (final Order order : orders) {
            final Station dest = order.getDestinationStation();
            if (dest != null) {
                stations.add(dest);
            }
        }

        return stations;
    }

    /**
     * extracts all stations of a collection of {@link Order}s.
     * 
     * @param orders
     *            collection of orders to check
     * @return a set of all stations
     */
    public static Set<Station> getAllStations(final Collection<Order> orders) {
        final Set<Station> stations = new HashSet<>(orders.size() * 2);

        for (final Order order : orders) {
            final Station dest = order.getDestinationStation();

            stations.add(order.getSourceStation());
            if (dest != null) {
                stations.add(dest);
            }
        }

        return stations;
    }

    /**
     * filters the given orders by a maximum weight
     * 
     * @param orderToFilter
     *            orders to filter
     * @param maxAmount
     *            maximum amount (inclusive) of an order.
     * @return a set of orders with the maximum given weight
     */
    public static Set<Order> filterOrdersByAmount(final Collection<Order> orderToFilter, final Integer maxAmount) {
        final Set<Order> filteredOrders = new HashSet<>();

        for (final Order order : orderToFilter) {
            if (order.amount <= maxAmount) {
                filteredOrders.add(order);
            }
        }

        return filteredOrders;
    }

    /**
     * calculates the complete unload duration.
     * 
     * @param orders
     *            orders used for calculation.
     * @return the complete unload duration
     */
    public static double completeUnloadDuration(final Iterable<Order> orders) {
        double duration = 0;

        for (final Order order : orders) {
            duration += order.destination.getServiceTime();
        }

        return duration;
    }

    /**
     * filters the given orders by its destination station.
     * 
     * @param orderToFilter
     *            orders that should be filtered.
     * @param station
     *            station to search for.
     * @return the filtered stations
     */
    public static Set<Order> filterOrdersByDestinationStation(final Iterable<Order> orderToFilter, final Station station) {
        final Set<Order> filterdOrder = new HashSet<>();

        for (final Order order : orderToFilter) {
            if (order.getDestinationStation().equals(station)) {
                filterdOrder.add(order);
            }
        }

        return filterdOrder;
    }

    /**
     * filters the given orders by its source station.
     * 
     * @param orderToFilter
     *            orders that should be filtered.
     * @param station
     *            station to search for.
     * @return the filtered stations
     */
    public static Set<Order> filterOrdersBySourceStation(final Iterable<Order> orderToFilter, final Station station) {
        final Set<Order> filterdOrder = new HashSet<>();

        for (final Order order : orderToFilter) {
            if (order.getSourceStation().equals(station)) {
                filterdOrder.add(order);
            }
        }

        return filterdOrder;
    }

    /**
     * calculates the complete service time.
     * 
     * @return the complete service time
     */
    public double serviceTime() {
        return this.source.getServiceTime() + this.destination.getServiceTime();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
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
        final Order other = (Order) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final String start = TimeUtils.convertToClockString(this.source.getTimeWindow().getStart()) + " - "
                + TimeUtils.convertToClockString(this.source.getTimeWindow().getEnd());
        final String end = TimeUtils.convertToClockString(this.destination.getTimeWindow().getStart()) + " - "
                + TimeUtils.convertToClockString(this.destination.getTimeWindow().getEnd());

        return this.name + " (id: " + this.id + ", product: " + this.product + ", amount: " + this.amount + ", start: " + start + ", end: " + end
                + ")";
    }
}
