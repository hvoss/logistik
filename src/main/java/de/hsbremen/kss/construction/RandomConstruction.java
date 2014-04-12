package de.hsbremen.kss.construction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;

public class RandomConstruction implements Construction {

	@Override
	public Plan constructPlan(Configuration configuration) {
		Plan plan = new Plan();
		Vehicle vehicle = CollectionUtils.get(configuration.getVehicles(), 0);

		List<Order> orders = new ArrayList<>(configuration.getOrders());
		Collections.shuffle(orders); // TODO fix start value
		Tour tour = new Tour(vehicle);
		
		for (Order order : orders) {
			tour.addOrderAndStation(order, order.getSource());
		}
		
		plan.addTour(tour);
		
		return plan;
	}

}
