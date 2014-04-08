package de.hsbremen.kss.model;

import java.util.Collection;

import org.apache.commons.lang3.Validate;

public final class Configuration {

	/** a collection of all orders */
	private final Collection<Order> orders;
	
	/** a collection of all stations */
	private final Collection<Station> stations;
	
	/** a collection of all vehicles */
	private final Collection<Vehicle> vehicles;

	public Configuration(Collection<Order> orders,
			Collection<Station> stations, Collection<Vehicle> vehicles) {
		Validate.noNullElements(orders);
		Validate.noNullElements(stations);
		Validate.noNullElements(vehicles);
		
		this.orders = orders;
		this.stations = stations;
		this.vehicles = vehicles;
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
	
	
}
