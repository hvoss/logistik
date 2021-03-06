package de.hsbremen.kss.configuration;

import java.util.Collections;
import java.util.HashSet;
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

    /** a collection of all complex orders */
    private final Set<ComplexOrder> complexOrders;

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
     */
    Configuration(final Set<Order> orders, final Set<Station> stations, final Set<Vehicle> vehicles, final Set<Product> products,
            final Set<ComplexOrder> complexOrders) {
        Validate.noNullElements(orders);
        Validate.noNullElements(stations);
        Validate.noNullElements(vehicles);
        Validate.noNullElements(products);
        Validate.noNullElements(complexOrders);

        this.orders = Collections.unmodifiableSet(orders);
        this.stations = Collections.unmodifiableSet(stations);
        this.vehicles = Collections.unmodifiableSet(vehicles);
        this.products = Collections.unmodifiableSet(products);
        this.complexOrders = Collections.unmodifiableSet(complexOrders);
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
     * Gets the a collection of all complex orders.
     * 
     * @return the a collection of all complex orders
     */
    public Set<ComplexOrder> getComplexOrders() {
        return this.complexOrders;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.orders == null) ? 0 : this.orders.hashCode());
        result = prime * result + ((this.products == null) ? 0 : this.products.hashCode());
        result = prime * result + ((this.stations == null) ? 0 : this.stations.hashCode());
        result = prime * result + ((this.vehicles == null) ? 0 : this.vehicles.hashCode());
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
        final Configuration other = (Configuration) obj;
        if (this.orders == null) {
            if (other.orders != null) {
                return false;
            }
        } else if (!this.orders.equals(other.orders)) {
            return false;
        }
        if (this.products == null) {
            if (other.products != null) {
                return false;
            }
        } else if (!this.products.equals(other.products)) {
            return false;
        }
        if (this.stations == null) {
            if (other.stations != null) {
                return false;
            }
        } else if (!this.stations.equals(other.stations)) {
            return false;
        }
        if (this.vehicles == null) {
            if (other.vehicles != null) {
                return false;
            }
        } else if (!this.vehicles.equals(other.vehicles)) {
            return false;
        }
        return true;
    }

    public Set<Product> usedProducts() {
        final Set<Product> usedProducts = new HashSet<>();

        for (final Order order : this.orders) {
            usedProducts.add(order.getProduct());
        }

        return usedProducts;
    }
}
