package de.hsbremen.kss.configuration;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

/**
 * The Class Vehicle.
 */
public final class Vehicle {

    /** the id. */
    private final String id;

    /** the name. */
    private final String name;

    /** the source depot (station). */
    private final Station sourceDepot;

    /** the destination depot (station). */
    private final Station destinationDepot;

    /** the velocity (km/h) */
    private final Double velocity;

    /** the timespan */
    private final TimeWindow timewindow;

    /** the capacities. */
    private final Product product;

    /** the capacity */
    private final Integer capacity;

    /**
     * Instantiates a new vehicle.
     * 
     * @param id
     *            the id
     * @param name
     *            the name
     * @param sourceDepot
     *            the source depot
     * @param destinationDepot
     *            the destination depot
     * @param velocity
     *            the velocity (km/h)
     * @param timewindow
     *            the timespan
     * @param product
     *            the product
     * @param capacity
     *            the capacity
     */
    Vehicle(final String id, final String name, final Station sourceDepot, final Station destinationDepot, final Double velocity,
            final TimeWindow timewindow, final Product product, final Integer capacity) {
        Validate.notNull(id, "id is null");
        Validate.notNull(name, "name is null");
        Validate.notNull(sourceDepot, "sourceDepot is null");
        Validate.notNull(destinationDepot, "destinationDepot is null");
        Validate.notNull(velocity, "velocity is null");
        Validate.notNull(timewindow, "timewindow is null");
        Validate.notNull(product, "product is null");
        Validate.notNull(capacity, "capacity is null");

        this.id = id;
        this.name = name;
        this.sourceDepot = sourceDepot;
        this.destinationDepot = destinationDepot;
        this.velocity = velocity;
        this.timewindow = timewindow;
        this.product = product;
        this.capacity = capacity;
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getId() {
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
     * Gets the source depot (station).
     * 
     * @return the source depot (station)
     */
    public Station getSourceDepot() {
        return this.sourceDepot;
    }

    /**
     * Gets the destination depot (station).
     * 
     * @return the destination depot (station)
     */
    public Station getDestinationDepot() {
        return this.destinationDepot;
    }

    @Override
    public String toString() {
        return this.name;
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
        final Vehicle other = (Vehicle) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    /**
     * calculates the traveling time between two given stations.
     * 
     * @param source
     *            first station
     * @param destination
     *            second station
     * @return the calculated traveling time.
     */
    public double calculateTavelingTime(final Station source, final Station destination) {
        final double distance = source.distance(destination);
        return calculateTavelingTime(distance);
    }

    /**
     * calculates the traveling time for the given distance.
     * 
     * @param distance
     *            the distance
     * @return the calculated traveling time.
     */
    public double calculateTavelingTime(final double distance) {
        return distance / this.velocity;
    }

    /**
     * Gets the timespan.
     * 
     * @return the timespan
     */
    public TimeWindow getTimeWindow() {
        return this.timewindow;
    }

    /**
     * Gets the velocity (km/h).
     * 
     * @return the velocity (km/h)
     */
    public Double getVelocity() {
        return this.velocity;
    }

    /**
     * Gets the capacities.
     * 
     * @return the capacities
     */
    public Product getProduct() {
        return this.product;
    }

    /**
     * Gets the capacity.
     * 
     * @return the capacity
     */
    public Integer getCapacity() {
        return this.capacity;
    }

    /**
     * checks if the order could be transported with this vehicle.
     * 
     * @param order
     *            order to check
     * @return true: order could be transported with this vehicle.
     */
    public boolean canTransport(final Order order) {
        return this.product.equals(order.getProduct());
    }

    public static Set<Vehicle> filterByProduct(final Iterable<Vehicle> vehicles, final Product product) {
        final Set<Vehicle> filteredVehicles = new HashSet<>();

        for (final Vehicle vehicle : vehicles) {
            if (vehicle.product.equals(product)) {
                filteredVehicles.add(vehicle);
            }
        }

        return filteredVehicles;
    }

    public static Set<Vehicle> filterByProducts(final Set<Vehicle> vehiclesToFilter, final Set<Product> products) {
        final Set<Vehicle> filteredVehicles = new HashSet<>();

        for (final Vehicle vehicle : vehiclesToFilter) {
            if (products.contains(vehicle.product)) {
                filteredVehicles.add(vehicle);
            }
        }

        return filteredVehicles;
    }

    public static Set<Product> extractProducts(final Set<Vehicle> vehicles) {
        final HashSet<Product> products = new HashSet<>();
        for (final Vehicle vehicle : vehicles) {
            products.add(vehicle.product);
        }

        return products;
    }
}
