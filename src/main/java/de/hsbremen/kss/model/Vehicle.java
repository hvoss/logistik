package de.hsbremen.kss.model;

import org.apache.commons.lang3.Validate;

public final class Vehicle {

	/** the id */
	private final Integer id;
	
	/** the name */
	private final String name;

	public Vehicle(Integer id, String name) {
		Validate.notNull(id);
		Validate.notNull(name);
		
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
}
