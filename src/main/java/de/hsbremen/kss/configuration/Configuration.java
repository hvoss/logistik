package de.hsbremen.kss.configuration;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang3.Validate;

public final class Configuration {

	/** a collection of all orders */
	private final Set<Order> orders;

	/** a collection of all stations */
	private final Set<Station> stations;

	/** a collection of all vehicles */
	private final Set<Vehicle> vehicles;

	/** a collection of all products */
	private final Set<Product> products;

	/** a collection of all product groups */
	private final Set<ProductGroup> productGroups;

	/**
	 * 
	 * @param orders
	 * @param stations
	 * @param vehicles
	 * @param products
	 * @param productGroups
	 */
	Configuration(final Set<Order> orders, final Set<Station> stations,
			final Set<Vehicle> vehicles, final Set<Product> products,
			final Set<ProductGroup> productGroups) {
		Validate.noNullElements(orders);
		Validate.noNullElements(stations);
		Validate.noNullElements(vehicles);
		Validate.noNullElements(products);
		Validate.noNullElements(productGroups);

		this.orders = Collections.unmodifiableSet(orders);
		this.stations = Collections.unmodifiableSet(stations);
		this.vehicles = Collections.unmodifiableSet(vehicles);
		this.products = Collections.unmodifiableSet(products);
		this.productGroups = Collections.unmodifiableSet(productGroups);
	}

	public Set<Order> getOrders() {
		return this.orders;
	}

	public Set<Station> getStations() {
		return this.stations;
	}

	public Set<Vehicle> getVehicles() {
		return this.vehicles;
	}

	public Set<Product> getProducts() {
		return this.products;
	}

	public Set<ProductGroup> getProductGroups() {
		return this.productGroups;
	}

}
