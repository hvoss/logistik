package de.hsbremen.kss.model;

import java.util.HashSet;
import java.util.Set;

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
		return products;
	}


	public Set<Vehicle> getVehicles() {
		return vehicles;
	}
	
}
