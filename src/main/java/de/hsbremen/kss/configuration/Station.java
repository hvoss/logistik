package de.hsbremen.kss.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * represents a station.
 * 
 * @author henrik
 * 
 */
public final class Station {

    /** the id. */
    private final Integer id;

    /** the name. */
    private final String name;

    /** the coordinates. */
    private final Vector2D coordinates;

    /**
     * a list of orders for which this station is assigned as the source
     * station.
     */
    private final Set<Order> sourceOrders;

    /**
     * a list of orders for which this station is assigned as the destination
     * station.
     */
    private final Set<Order> destinationOrders;

    /**
     * Instantiates a new station.
     * 
     * @param id
     *            the id
     * @param name
     *            the name
     * @param coordinates
     *            the coordinates
     */
    Station(final Integer id, final String name, final Vector2D coordinates) {
        Validate.notNull(id, "id is null");
        Validate.notNull(name, "name is null");
        Validate.notNull(coordinates, "coordinates is null");

        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.sourceOrders = new HashSet<>();
        this.destinationOrders = new HashSet<Order>();
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public Integer getId() {
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
     * Gets the coordinates.
     * 
     * @return the coordinates
     */
    public Vector2D getCoordinates() {
        return this.coordinates;
    }

    /**
     * Gets the a list of orders for which this station is assigned as the
     * source station.
     * 
     * @return the a list of orders for which this station is assigned as the
     *         source station
     */
    public Set<Order> getSourceOrders() {
        return Collections.unmodifiableSet(this.sourceOrders);
    }

    /**
     * Gets the a list of orders for which this station is assigned as the
     * destination station.
     * 
     * @return the a list of orders for which this station is assigned as the
     *         destination station
     */
    public Set<Order> getDestinationOrders() {
        return Collections.unmodifiableSet(this.destinationOrders);
    }

    /**
     * Calculates the distance to an other station.
     * 
     * @param station
     *            the other station
     * @return the distance
     */
    public double distance(final Station station) {
        return station.coordinates.distance(this.coordinates);

    }

    /**
     * returns the angle (rad) to another station.
     * 
     * @param station
     *            the other station
     * @return the angle (rad) to another station.
     */
    public double angle(final Station station) {
        final Vector2D localVec = station.coordinates.subtract(this.coordinates);
        return Math.atan2(localVec.getY(), localVec.getX());
    }

    /**
     * Gets the source products.
     * 
     * @return the source products
     */
    public Set<Product> getSourceProducts() {
        final Set<Product> sourceProducts = new HashSet<>();

        for (final Order order : this.sourceOrders) {
            final Set<Product> products = order.getProducts();
            sourceProducts.addAll(products);
        }

        return sourceProducts;
    }

    /**
     * Gets the destination products.
     * 
     * @return the destination products
     */
    public Set<Product> getDestinationProducts() {
        final Set<Product> destinationProducts = new HashSet<>();

        for (final Order order : this.destinationOrders) {
            final Set<Product> products = order.getProducts();
            destinationProducts.addAll(products);
        }

        return destinationProducts;
    }

    /**
     * adds a order for which this station is assigned as the source station.
     * 
     * @param order
     *            order for which this station is assigned as the source station
     */
    void addSourceOrder(final Order order) {
        Validate.notNull(order, "order is null");
        this.sourceOrders.add(order);
    }

    /**
     * adds a order for which this station is assigned as the destination
     * station.
     * 
     * @param order
     *            order for which this station is assigned as the destination
     *            station
     */
    void addDestinationOrder(final Order order) {
        Validate.notNull(order, "order is null");
        this.destinationOrders.add(order);
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * finds the nearest station in a collection of stations.
     * 
     * @param stations
     *            stations to check
     * @return the nearest station
     */
    public Station findNearestStation(final Collection<Station> stations) {
        Station nearestStation = null;
        for (final Station otherStation : stations) {
            if (nearestStation == null || distance(nearestStation) > distance(otherStation)) {
                nearestStation = otherStation;
            }
        }
        return nearestStation;
    }

    /**
     * finds the nearest source station in a collection of orders.
     * 
     * @param orders
     *            orders to check
     * @return the nearest source station
     */
    public Order findNearestSourceStation(final Collection<Order> orders) {
        Order nearestOrder = null;

        for (final Order order : orders) {
            if (nearestOrder == null) {
                nearestOrder = order;
            } else {
                final Station nearestStation = nearestOrder.getSource();
                final Station otherStation = order.getSource();
                if (distance(nearestStation) > distance(otherStation)) {
                    nearestOrder = order;
                }
            }
        }

        return nearestOrder;
    }
}
