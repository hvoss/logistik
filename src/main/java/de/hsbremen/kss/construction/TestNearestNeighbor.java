package de.hsbremen.kss.construction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Plan;

public class TestNearestNeighbor implements Construction {

	private static final Logger LOG = LoggerFactory
			.getLogger(TestNearestNeighbor.class);

	@Override
	public Plan constructPlan(Configuration configuration) {
		Vehicle vehicle = CollectionUtils.get(configuration.getVehicles(), 0);

		Set<Order> orders = new HashSet<>(configuration.getOrders());
		Station actualStation = vehicle.getSourceDepot();
		List<Order> orderSequence = new ArrayList<>();

		while (!orders.isEmpty()) {
			Order nearestOrder = actualStation.findNearestSourceStation(orders);
			orderSequence.add(nearestOrder);
			Station nearestStation = nearestOrder.getSource();
			logTravel(actualStation, nearestStation);
			actualStation = nearestStation;
			orders.remove(nearestOrder);
		}
		
		logTravel(actualStation, vehicle.getDestinationDepot());

		LOG.info(orderSequence.toString());

		return null;
	}
	
	private static void logTravel(Station source, Station destination) {
		LOG.info(source.getName() + " => "
				+ destination.getName() + " (" + Math.round(source.distance(destination)) +" km)");
	}

}
