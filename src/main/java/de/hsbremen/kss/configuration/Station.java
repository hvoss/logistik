package de.hsbremen.kss.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.Precision;
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

    /** cached source products */
    private final CollectionCache<Set<Product>, Product> sourceProductsCache;

    /** cached destination products */
    private final CollectionCache<Set<Product>, Product> destinationProductsCache;

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

        this.sourceProductsCache = new CollectionCache<>();
        this.destinationProductsCache = new CollectionCache<>();
    }

    /**
     * copy constructor.
     * 
     * @param station
     *            station to copy
     */
    Station(final Station station) {
        this(station.id, station.name, station.coordinates);
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
        Validate.isTrue(!this.coordinates.equals(station.coordinates), "can't calculate angle of the same coordinates");

        Double angle = this.angles.get(station);

        if (angle == null) {
            final Vector2D localVec = station.coordinates.subtract(this.coordinates);
            angle = Math.atan2(localVec.getY(), localVec.getX());
            if (angle < 0) {
                angle += Math.PI * 2;
            }
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
        if (!this.sourceProductsCache.isValid()) {
            final Set<Product> sourceProducts = new HashSet<>();

            for (final Order order : this.sourceOrders) {
                sourceProducts.add(order.getProduct());
            }

            this.sourceProductsCache.setCollection(sourceProducts);
        }

        return this.sourceProductsCache.getCollection();
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
        this.sourceProductsCache.clearCache();
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
        this.destinationProductsCache.clearCache();
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
                final Station nearestStation = nearestOrder.getSourceStation();
                final Station otherStation = order.getSourceStation();
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
     * @param vehicle
     *            vehicle used for traveling
     * @param stations
     *            stations to log.
     */
    public static void logDistancesBetweenStations(final Vehicle vehicle, final Collection<Station> stations) {
        final Set<Station> processedStations = new HashSet<>();
        for (final Station station : stations) {
            processedStations.add(station);
            for (final Station otherStation : stations) {
                if (!processedStations.contains(otherStation)) {
                    final double distance = Precision.round(station.distance(otherStation), 1);
                    final double travelingTime = Precision.round(vehicle.calculateTavelingTime(station, otherStation), 2);

                    Station.LOG.debug("distance between " + station.getName() + " and " + otherStation.getName() + ": " + distance + " km ("
                            + travelingTime + " h)");
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

    /**
     * calculates the length of a route.
     * 
     * @param route
     *            route to calculate
     * @return length of the route
     */
    public static double length(final List<Station> route) {
        double length = 0;
        Station actualStation = null;

        for (final Station station : route) {
            if (actualStation != null) {
                length += actualStation.distance(station);
            }
            actualStation = station;
        }

        return length;
    }

    /**
     * returns the nearest station out of a collection of stations.
     * 
     * @param stations
     *            stations to be analyzed
     * @return the nearest station
     */
    public Station nearestStation(final Collection<Station> stations) {
        Station nearestStation = null;
        for (final Station station : stations) {
            if (nearestStation == null || distance(station) < distance(nearestStation)) {
                nearestStation = station;
            }
        }

        return nearestStation;
    }

    /**
     * copies a collection of stations.
     * 
     * @param stations
     *            stations to copy.
     * @return copied stations
     */
    public static Set<Station> copy(final Collection<Station> stations) {
        final Set<Station> copiedStations = new HashSet<>(stations.size());
        for (final Station station : stations) {
            copiedStations.add(new Station(station));
        }
        return copiedStations;
    }

    public static List<Station> convert(final Collection<OrderStation> route) {
        final List<Station> list = new ArrayList<>(route.size());

        for (final OrderStation station : route) {
            list.add(station.getStation());
        }

        return list;
    }
}
