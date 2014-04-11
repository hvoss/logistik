package de.hsbremen.kss.model;

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
	private Collection<Order> sourceOrders;
	
	/** a list of orders for which this station is assigned as the destination station */
	private Collection<Order> destinationOrders;

	public Station(Integer id, String name, Vector2D coordinates) {
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
	
	public Collection<Order> getSourceOrders() {
		return sourceOrders;
	}

	public Collection<Order> getDestinationOrders() {
		return destinationOrders;
	}
	
	void makeUnmodifyable() {
		this.sourceOrders = Collections.unmodifiableCollection(this.sourceOrders);
		this.destinationOrders = Collections.unmodifiableCollection(this.destinationOrders);
	}
	
	public double distance(Station station) {
		return station.coordinates.distance(this.coordinates);
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
}
