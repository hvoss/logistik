package de.hsbremen.kss.model;

import org.apache.commons.lang3.Validate;

/**
 * represents a order of a customer.
 * 
 * @author henrik
 *
 */
public final class Order {
	
	/** the id */
	private final Integer id;
	
	/** the name */
	private final String name;

	/** the source station */
	private final Station source;
	
	/** the destination station. could be null */
	private final Station destination;
	
	public Order(Integer id, String name, Station source) {
		this(id, name, source, null);
	}
		
	public Order(Integer id, String name, Station source, Station destination) {
		Validate.notNull(id, name, source);
		this.id = id;
		this.name = name;
		this.source = source;
		this.destination = destination;
	}

	public int getId() {
		return id;
	}


	public String getName() {
		return name;
	}


	public Station getSource() {
		return source;
	}


	public Station getDestination() {
		return destination;
	}
	
	
}
