package de.hsbremen.kss.model;


import org.apache.commons.lang3.Validate;

public final class Vehicle {

	/** the id */
	private final Integer id;
	
	/** the name */
	private final String name;

	public Vehicle(Integer id, String name) {
		Validate.notNull(id, "id is null");
		Validate.notNull(name, "name is null");
		
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	void makeUnmodifyable() {
		// nothing to do yet
	}
}
