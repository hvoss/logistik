package de.hsbremen.kss.construction;

import java.util.ArrayList;
import java.util.Collections;
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
import de.hsbremen.kss.model.Saving;
import de.hsbremen.kss.model.SavingOrder;
import de.hsbremen.kss.model.Tour;

/**
 * Realizes the Savings-Algorithm with orders (one element is an order instead of a location)
 * 
 * @author david
 *
 */
public class SavingsTourConstruction implements Construction {
	
	private static final Logger LOG = LoggerFactory.getLogger(SavingsTourConstruction.class);

	@Override
	public Plan constructPlan(Configuration configuration) {
		return constructPlan(configuration, null);
	}
	
	public Plan constructPlan(Configuration configuration, Order startOrder) {
		final Plan plan = new Plan(SavingsTourConstruction.class);
		
		final Vehicle vehicle = CollectionUtils.get(configuration.getVehicles(), 0);
        final Station depot = vehicle.getSourceDepot();
        final Tour tour = new Tour(vehicle);
		
        final Set<Order> configurationOrders = new HashSet<>(configuration.getOrders());
        final List<Order> visitedOrders = new ArrayList<>(configuration.getOrders().size());

        if(startOrder == null){
        	final List<SavingOrder> savingsOrderList = new ArrayList<SavingOrder>(configurationOrders.size());
        	for(Order order : configurationOrders) {
        		savingsOrderList.add(new SavingOrder(order, depot));
        	}
        	Collections.sort(savingsOrderList);
        	tour.addSourceOrder(savingsOrderList.get(0).getOrder());
        	tour.addDestinationOrder(savingsOrderList.get(0).getOrder());
        	visitedOrders.add(savingsOrderList.get(0).getOrder());
        	configurationOrders.remove(savingsOrderList.get(0).getOrder());
        } else {
			tour.addSourceOrder(startOrder);
			tour.addDestinationOrder(startOrder);
			visitedOrders.add(startOrder);
			configurationOrders.remove(startOrder);
		}
        
        while(!configurationOrders.isEmpty()) {
        	final int index = visitedOrders.size() - 1;
        	final List<Saving> savingsList = new ArrayList<>();
        	        	
        	for(Order order : configurationOrders) {
        		savingsList.add(new Saving(visitedOrders.get(index), order, depot));
        	}
        	
        	Collections.sort(savingsList);
        	
        	// load action
        	if(tour.freeSpace() >= savingsList.get(0).getDestinationOrder().weightOfProducts()) {
        		tour.addSourceOrder(savingsList.get(0).getDestinationOrder());
        	}
        	
        	// unload action
        	tour.addDestinationOrder(savingsList.get(0).getDestinationOrder());
        	visitedOrders.add(savingsList.get(0).getDestinationOrder());
        	
        	configurationOrders.remove(savingsList.get(0).getDestinationOrder());
        }
                        		
        plan.addTour(tour);
        
		return plan;
	}

	@Override
	public void logStatistic() {
		// TODO Auto-generated method stub

	}

}
