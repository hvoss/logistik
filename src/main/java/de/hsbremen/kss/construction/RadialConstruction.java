package de.hsbremen.kss.construction;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;

public class RadialConstruction implements Construction {

	private static final Logger LOG = LoggerFactory.getLogger(RadialConstruction.class);
	
	@Override
	public Plan constructPlan(Configuration configuration) {
		Plan plan = new Plan();

		Vehicle vehicle = CollectionUtils.get(configuration.getVehicles(), 0);
		Tour tour = new Tour(vehicle);
		Station sourceDepot = vehicle.getSourceDepot();
		
		TreeMap<Double, Order> sortedOrders = new TreeMap<>();

		for (Order order : configuration.getOrders()) {
			Station station = order.getSource();
			double angle = sourceDepot.angle(station);
			sortedOrders.put(Double.valueOf(angle), order);
		}
		
		LOG.debug(sortedOrders.toString());
		
		for (Map.Entry<Double,Order> order : sortedOrders.entrySet()) {
			tour.addOrderAndStation(order.getValue(), order.getValue().getSource());
		}
		
		plan.addTour(tour);
		
		return plan;
	}

}
