package de.hsbremen.kss.construction;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;

public class TestNearestNeighbor implements Construction {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(TestNearestNeighbor.class);

	@Override
	public Plan constructPlan(Configuration configuration) {
		Plan plan = new Plan();
		Vehicle vehicle = CollectionUtils.get(configuration.getVehicles(), 0);

		Set<Order> orders = new HashSet<>(configuration.getOrders());
		Station actualStation = vehicle.getSourceDepot();
		Tour tour = new Tour(vehicle);

		while (!orders.isEmpty()) {
			Order nearestOrder = actualStation.findNearestSourceStation(orders);
			Station nearestStation = nearestOrder.getSource();
			tour.addOrderAndStation(nearestOrder, nearestStation);
			actualStation = nearestStation;
			orders.remove(nearestOrder);
		}
		
		plan.addTour(tour);
		
		return plan;
	}
	
	

}
