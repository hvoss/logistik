package de.hsbremen.kss.model;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang3.Validate;

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
	
	/** the x coordinate */
	private final Integer xCoordinate;
	
	/** the y coordinate */
	private final Integer yCoordinate;
	
	/** a list of orders for which this station is assigned as the source station */
	private final Collection<Order> sourceOrders;
	
	/** a list of orders for which this station is assigned as the destination station */
	private final Collection<Order> destinationOrders;

	public Station(Integer id, String name, Integer xCoordinate, Integer yCoordinate) {
		Validate.notNull(id);
		Validate.notNull(name);
		Validate.notNull(xCoordinate);
		Validate.notNull(yCoordinate);
		
		this.id = id;
		this.name = name;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.sourceOrders = new HashSet<>();
		this.destinationOrders = new HashSet<Order>();
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getxCoordinate() {
		return xCoordinate;
	}

	public Integer getyCoordinate() {
		return yCoordinate;
	}

	public Collection<Order> getSourceOrders() {
		return sourceOrders;
	}

	public Collection<Order> getDestinationOrders() {
		return destinationOrders;
	}
	
}
