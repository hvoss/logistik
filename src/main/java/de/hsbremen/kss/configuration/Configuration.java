package de.hsbremen.kss.configuration;

import java.util.Collection;
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

	Configuration(Set<Order> orders, Set<Station> stations,
			Set<Vehicle> vehicles, Set<Product> products,
			Set<ProductGroup> productGroups) {
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
		return orders;
	}

	public Set<Station> getStations() {
		return stations;
	}

	public Set<Vehicle> getVehicles() {
		return vehicles;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public Set<ProductGroup> getProductGroups() {
		return productGroups;
	}

}
