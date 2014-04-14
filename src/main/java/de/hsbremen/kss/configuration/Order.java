package de.hsbremen.kss.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

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

    /** the source station. */
    private final Station source;

    /** the destination station. could be null */
    private final Station destination;

    /** all items. */
    private final Set<Item> items;

    /**
     * Instantiates a new order.
     * 
     * @param id
     *            the id
     * @param name
     *            the name
     * @param source
     *            the source
     */
    Order(final Integer id, final String name, final Station source) {
        this(id, name, source, null);
    }

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
     */
    Order(final Integer id, final String name, final Station source, final Station destination) {
        Validate.notNull(id, "id is null");
        Validate.notNull(name, "name is null");
        Validate.notNull(source, "source station is null");

        this.id = id;
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.items = new HashSet<>();
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
    public Station getSource() {
        return this.source;
    }

    /**
     * Gets the destination station.
     * 
     * @return the destination station
     */
    public Station getDestination() {
        return this.destination;
    }

    /**
     * Gets the all items.
     * 
     * @return the all items
     */
    public Set<Item> getItems() {
        return Collections.unmodifiableSet(this.items);
    }

    /**
     * adds a item to the order.
     * 
     * @param item
     *            item to add
     */
    void addItem(final Item item) {
        Validate.notNull(item, "item is null");
        this.items.add(item);
    }

    /**
     * Gets the products.
     * 
     * @return the products
     */
    public Set<Product> getProducts() {
        final Set<Product> products = new HashSet<>(this.items.size());

        for (final Item item : this.items) {
            products.add(item.getProduct());
        }

        return products;
    }

    @Override
    public String toString() {
        return this.name;
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
            stations.add(order.getSource());
        }

        return stations;
    }
}
