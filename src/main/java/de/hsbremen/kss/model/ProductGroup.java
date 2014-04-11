package de.hsbremen.kss.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

public class ProductGroup {

	/** the id */
	private final Integer id;
	
	/** the name */
	private final String name;

	/** Products belonging to this product group */
	private final Set<Product> products;
	
	private final Set<Vehicle> vehicles;

	
	public ProductGroup(Integer id, String name) {
		this.id = id;
		this.name = name;
		this.products = new HashSet<>();
		this.vehicles = new HashSet<>();
	}


	public Integer getId() {
		return id;
	}


	public String getName() {
		return name;
	}


	public Set<Product> getProducts() {
		return Collections.unmodifiableSet(products);
	}


	public Set<Vehicle> getVehicles() {
		return Collections.unmodifiableSet(vehicles);
	}
	
	public boolean contains(Product product) {
		return this.vehicles.contains(product);
	}
	
	public void addProduct(Product product) {
		Validate.notNull(product);
		this.products.add(product);
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
