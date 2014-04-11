package de.hsbremen.kss.configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

public class Product {

	/** the id */
	private final Integer id;

	/** the name */
	private final String name;
	
	private final Set<Vehicle> vehicles;

	/** Product groups to which the product belongs */
	private final Set<ProductGroup> productGroups;

	Product(Integer id, String name) {
		this.id = id;
		this.name = name;
		this.productGroups = new HashSet<>();
		this.vehicles = new HashSet<>();
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Set<ProductGroup> getProductGroups() {
		return Collections.unmodifiableSet(productGroups);
	}

	public Set<Vehicle> getVehicles() {
		return Collections.unmodifiableSet(vehicles);
	}
	
	public void addProductGroup(ProductGroup productGroup) {
		Validate.notNull(productGroup);
		this.productGroups.add(productGroup);
	}
	
	public void addVehicle(Vehicle vehicle) {
		Validate.notNull(vehicle);
		this.vehicles.add(vehicle);
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
