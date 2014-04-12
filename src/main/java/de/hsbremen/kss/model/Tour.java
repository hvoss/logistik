package de.hsbremen.kss.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;

public class Tour {
	
	private static final Logger LOG = LoggerFactory.getLogger(Tour.class);

	private final Vehicle vehicle;
	
	private final List<Station> stations;
	
	private final Set<Order> orders;
	
	public Tour(Vehicle vehicle) {
		this.vehicle = vehicle;
		this.stations = new ArrayList<Station>();
		this.orders = new HashSet<Order>();
		
		this.stations.add(this.vehicle.getSourceDepot());
		this.stations.add(this.vehicle.getDestinationDepot());
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public List<Station> getStations() {
		return Collections.unmodifiableList(stations);
	}

	public Set<Order> getOrders() {
		return Collections.unmodifiableSet(orders);
	}
	
	public void addStation(Station station) {
		int index = this.stations.size() - 1;
		this.stations.add(index, station);
	}
	
	public void addOrder(Order order) {
		this.orders.add(order);
	}
	
	public void addOrderAndStation(Order order, Station station) {
		addOrder(order);
		addStation(station);
	}
	
	public void logTour() {
		Station source = this.stations.get(0);
		for (int i = 1; i < this.stations.size(); i++) {
			Station destination = this.stations.get(i);
			LOG.info(source.getName() + " => "
					+ destination.getName() + " (" + Math.round(source.distance(destination)) +" km)");
			source = destination;
		}
	}
	
	public double length() {
		double length = 0;
		Station source = this.stations.get(0);
		for (int i = 1; i < this.stations.size(); i++) {
			Station destination = this.stations.get(i);
			length += source.distance(destination);
			source = destination;
		}
		return length;
	}
}
