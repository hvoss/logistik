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

    /** all items wrapped by a {@link Collections#unmodifiableSet(Set)} */
    private final Set<Item> umItems;

    /** a cache of the products */
    private final CollectionCache<Set<Product>, Product> productsCache;

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
        this.umItems = Collections.unmodifiableSet(this.items);

        this.productsCache = new CollectionCache<Set<Product>, Product>();
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
        return this.umItems;
    }

    /**
     * adds a item to the order.
     * 
     * @param item
     *            item to add
     */
    void addItem(final Item item) {
        Validate.notNull(item, "item is null");
        if (!this.items.add(item)) {
            throw new IllegalStateException("order " + this.name + " already contain the given item: " + item);
        }
        this.productsCache.clearCache();
    }

    /**
     * Gets the products.
     * 
     * @return the products
     */
    public Set<Product> getProducts() {
        if (!this.productsCache.isValid()) {
            final Set<Product> products = new HashSet<>(this.items.size());

            for (final Item item : this.items) {
                products.add(item.getProduct());
            }

            this.productsCache.setCollection(products);
        }

        return this.productsCache.getCollection();
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
            final Station dest = order.getDestination();
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
            final Station dest = order.getDestination();

            stations.add(order.getSource());
            if (dest != null) {
                stations.add(dest);
            }
        }

        return stations;
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
}
