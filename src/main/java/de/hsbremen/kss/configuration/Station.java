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

	/** the id */
	private final Integer id;
	
	/** the name */
	private final String name;
	
	/** the coordinates */
	private final Vector2D coordinates;

	/** a list of orders for which this station is assigned as the source station */
	private Set<Order> sourceOrders;
	
	/** a list of orders for which this station is assigned as the destination station */
	private Set<Order> destinationOrders;

	Station(Integer id, String name, Vector2D coordinates) {
		Validate.notNull(id, "id is null");
		Validate.notNull(name, "name is null");
		Validate.notNull(coordinates, "coordinates is null");
		
		this.id = id;
		this.name = name;
		this.coordinates = coordinates;
		this.sourceOrders = new HashSet<>();
		this.destinationOrders = new HashSet<Order>();
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Vector2D getCoordinates() {
		return coordinates;
	}
	
	public Set<Order> getSourceOrders() {
		return Collections.unmodifiableSet(sourceOrders);
	}

	public Set<Order> getDestinationOrders() {
		return Collections.unmodifiableSet(destinationOrders);
	}
	
	public double distance(Station station) {
		return station.coordinates.distance(this.coordinates);
		
	}
	
	public double angle(Station station) {
		Vector2D localVec = station.coordinates.subtract(this.coordinates);
		return Math.atan2(localVec.getY(), localVec.getX());
	}
	
	public Set<Product> getSourceProducts() {
		Set<Product> sourceProducts = new HashSet<>();
		
		for (Order order : this.sourceOrders) {
			Set<Product> products = order.getProducts();
			sourceProducts.addAll(products);
		}
		
		return sourceProducts;
	}
	
	public Set<Product> getDestinationProducts() {
		Set<Product> destinationProducts = new HashSet<>();
		
		for (Order order : this.destinationOrders) {
			Set<Product> products = order.getProducts();
			destinationProducts.addAll(products);
		}
		
		return destinationProducts;
	}
	
	void addSourceOrder(Order order) {
		Validate.notNull(order, "order is null");
		this.sourceOrders.add(order);
	}
	
	void addDestinationOrder(Order order) {
		Validate.notNull(order, "order is null");
		this.destinationOrders.add(order);
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public Station findNearestStation(Collection<Station> stations) {
		Station nearestStation = null;
		for (Station otherStation : stations) {
			if (nearestStation == null || distance(nearestStation) > distance(otherStation)) {
				nearestStation = otherStation;
			}
		}
		return nearestStation;
	}
	
	public Order findNearestSourceStation(Collection<Order> orders) {
		Order nearestOrder = null;
		
		for (Order order : orders) {
			if (nearestOrder == null) {
				nearestOrder = order;
			} else {
				Station nearestStation = nearestOrder.getSource();
				Station otherStation = order.getSource();
				if (distance(nearestStation) > distance(otherStation)) {
					nearestOrder = order;
				}
			}
		}
		
		return nearestOrder;
	}
}
