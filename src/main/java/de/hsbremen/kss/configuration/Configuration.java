package de.hsbremen.kss.configuration;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.Validate;

public final class Configuration {

	/** a collection of all orders */
	private final Collection<Order> orders;
	
	/** a collection of all stations */
	private final Collection<Station> stations;
	
	/** a collection of all vehicles */
	private final Collection<Vehicle> vehicles;
	
	/** a collection of all products */
	private final Collection<Product> products;
	
	/** a collection of all product groups */
	private final Collection<ProductGroup> productGroups;

	public Configuration(Collection<Order> orders,
			Collection<Station> stations, Collection<Vehicle> vehicles, Collection<Product> products, Collection<ProductGroup> productGroups) {
		Validate.noNullElements(orders);
		Validate.noNullElements(stations);
		Validate.noNullElements(vehicles);
		Validate.noNullElements(products);
		Validate.noNullElements(productGroups);
		
		this.orders = Collections.unmodifiableCollection(orders);
		this.stations = Collections.unmodifiableCollection(stations);
		this.vehicles = Collections.unmodifiableCollection(vehicles);
		this.products = Collections.unmodifiableCollection(products);
		this.productGroups = Collections.unmodifiableCollection(productGroups);
		
		for (Order order : this.orders) {
			order.makeUnmodifyable();
		}
		
		for (Station station : this.stations) {
			station.makeUnmodifyable();
		}
		
		for (Vehicle vehicle : this.vehicles) {
			vehicle.makeUnmodifyable();
		}
	}

	public Collection<Order> getOrders() {
		return orders;
	}

	public Collection<Station> getStations() {
		return stations;
	}

	public Collection<Vehicle> getVehicles() {
		return vehicles;
	}

	public Collection<Product> getProducts() {
		return products;
	}

	public Collection<ProductGroup> getProductGroups() {
		return productGroups;
	}
	
}
