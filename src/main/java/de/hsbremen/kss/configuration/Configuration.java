package de.hsbremen.kss.configuration;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang3.Validate;

/**
 * represents the configuration.
 * 
 * @author henrik
 * 
 */
public final class Configuration {

    /** a collection of all orders. */
    private final Set<Order> orders;

    /** a collection of all stations. */
    private final Set<Station> stations;

    /** a collection of all vehicles. */
    private final Set<Vehicle> vehicles;

    /** a collection of all products. */
    private final Set<Product> products;

    /** a collection of all product groups. */
    private final Set<ProductGroup> productGroups;

    /**
     * Instantiates a new configuration.
     * 
     * @param orders
     *            the orders
     * @param stations
     *            the stations
     * @param vehicles
     *            the vehicles
     * @param products
     *            the products
     * @param productGroups
     *            the product groups
     */
    Configuration(final Set<Order> orders, final Set<Station> stations, final Set<Vehicle> vehicles, final Set<Product> products,
            final Set<ProductGroup> productGroups) {
        Validate.noNullElements(orders);
        Validate.noNullElements(stations);
        Validate.noNullElements(vehicles);
        Validate.noNullElements(products);
        Validate.noNullElements(productGroups);

        this.orders = Collections.unmodifiableSet(orders);
        this.stations = Collections.unmodifiableSet(stations);
        this.vehicles = Collections.unmodifiableSet(vehicles);
        this.products = Collections.unmodifiableSet(products);
        this.productGroups = Collections.unmodifiableSet(productGroups);
    }

    /**
     * Gets the a collection of all orders.
     * 
     * @return the a collection of all orders
     */
    public Set<Order> getOrders() {
        return this.orders;
    }

    /**
     * Gets the a collection of all stations.
     * 
     * @return the a collection of all stations
     */
    public Set<Station> getStations() {
        return this.stations;
    }

    /**
     * Gets the a collection of all vehicles.
     * 
     * @return the a collection of all vehicles
     */
    public Set<Vehicle> getVehicles() {
        return this.vehicles;
    }

    /**
     * Gets the a collection of all products.
     * 
     * @return the a collection of all products
     */
    public Set<Product> getProducts() {
        return this.products;
    }

    /**
     * Gets the a collection of all product groups.
     * 
     * @return the a collection of all product groups
     */
    public Set<ProductGroup> getProductGroups() {
        return this.productGroups;
    }

}
