package de.hsbremen.kss.model;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

public final class Vehicle {

	/** the id */
	private final Integer id;
	
	/** the name */
	private final String name;
	
	/** the source depot (station) */
	private final Station sourceDepot;
	
	/** the destination depot (station) */
	private final Station destinationDepot;
	
	private final Set<Capacity> capacities;

	public Vehicle(Integer id, String name, Station sourceDepot,  Station destinationDepot) {
		Validate.notNull(id, "id is null");
		Validate.notNull(name, "name is null");
		Validate.notNull(sourceDepot, "sourceDepot is null");
		Validate.notNull(destinationDepot, "destinationDepot is null");
		
		this.id = id;
		this.name = name;
		this.sourceDepot = sourceDepot;
		this.destinationDepot = destinationDepot;
		this.capacities = new HashSet<>();
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

	public Station getSourceDepot() {
		return sourceDepot;
	}

	public Station getDestinationDepot() {
		return destinationDepot;
	}

	public Set<Capacity> getCapacities() {
		return Collections.unmodifiableSet(capacities);
	}
	
	public boolean canBeTransported(Product product) {
		for (Capacity capacity : this.capacities){
			if (capacity.contains(product)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void addCapacity(Capacity capacity) {
		Validate.notNull(capacity);
		this.capacities.add(capacity);
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
