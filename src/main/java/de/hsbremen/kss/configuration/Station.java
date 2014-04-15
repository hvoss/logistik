package de.hsbremen.kss.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * represents a station.
 * 
 * @author henrik
 * 
 */
public final class Station {

    /** logging interface */
    private static final Logger LOG = LoggerFactory.getLogger(Station.class);

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

    /** source orders wrapped by a {@link Collections#unmodifiableSet(Set)} */
    private final Set<Order> umSourceOrders;

    /**
     * a list of orders for which this station is assigned as the destination
     * station.
     */
    private final Set<Order> destinationOrders;

    /** destination orders wrapped by a {@link Collections#unmodifiableSet(Set)} */
    private final Set<Order> umDestinationOrders;

    /** cached distances to other stations */
    private final Map<Station, Double> distances;

    /** cached angles to other stations */
    private final Map<Station, Double> angles;

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

        this.umDestinationOrders = Collections.unmodifiableSet(this.destinationOrders);
        this.umSourceOrders = Collections.unmodifiableSet(this.sourceOrders);

        this.distances = new HashMap<>();
        this.angles = new HashMap<>();
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
        return this.umSourceOrders;
    }

    /**
     * Gets the a list of orders for which this station is assigned as the
     * destination station.
     * 
     * @return the a list of orders for which this station is assigned as the
     *         destination station
     */
    public Set<Order> getDestinationOrders() {
        return this.umDestinationOrders;
    }

    /**
     * Calculates the distance to an other station.
     * 
     * @param station
     *            the other station
     * @return the distance
     */
    public double distance(final Station station) {
        Double distance = this.distances.get(station);
        if (distance == null) {
            distance = station.coordinates.distance(this.coordinates);
            this.distances.put(station, distance);
            station.distances.put(this, distance);
        }

        return distance;
    }

    /**
     * returns the angle (rad) to another station.
     * 
     * @param station
     *            the other station
     * @return the angle (rad) to another station.
     */
    public double angle(final Station station) {
        Double angle = this.angles.get(station);

        if (angle == null) {
            final Vector2D localVec = station.coordinates.subtract(this.coordinates);
            angle = Math.atan2(localVec.getY(), localVec.getX());
            this.angles.put(station, angle);
            // XXX calculate the angle of the other station
        }

        return angle;
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
        if (!this.sourceOrders.add(order)) {
            throw new IllegalStateException("station " + this.name + " already contain the given source order: " + order);
        }
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
        if (!this.destinationOrders.add(order)) {
            throw new IllegalStateException("station " + this.name + " already contain the given destination order: " + order);
        }
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

    /**
     * logs the distance between all given stations.
     * 
     * @param stations
     *            stations to log.
     */
    public static void logDistancesBetweenStations(final Collection<Station> stations) {
        final Set<Station> processedStations = new HashSet<>();
        for (final Station station : stations) {
            processedStations.add(station);
            for (final Station otherStation : stations) {
                if (!processedStations.contains(otherStation)) {
                    Station.LOG.debug("distance between " + station.getName() + " and " + otherStation.getName() + ": "
                            + Math.round(station.distance(otherStation)) + " km");
                }
            }
        }
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
        final Station other = (Station) obj;
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
