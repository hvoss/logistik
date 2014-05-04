package de.hsbremen.kss.construction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
 * Realizes the Savings-algorithm with tours instead of locations
 * Not ready yet. Connects only Orders: Load --> Unload
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
		
        final List<Order> configurationOrderList = new ArrayList<Order>(configuration.getOrders());

        if(startOrder == null){
        	final List<SavingOrder> savingsOrderList = new ArrayList<SavingOrder>(configurationOrderList.size());
        	for(Order order : configurationOrderList) {
        		savingsOrderList.add(new SavingOrder(order, depot));
        	}
        	Collections.sort(savingsOrderList);
        	tour.addSourceOrder(savingsOrderList.get(0).getOrder());
        	tour.addDestinationOrder(savingsOrderList.get(0).getOrder());
        	configurationOrderList.remove(savingsOrderList.get(0).getOrder());
        } else {
			tour.addSourceOrder(startOrder);
			tour.addDestinationOrder(startOrder);
			configurationOrderList.remove(startOrder);
		}
        
        while(!configurationOrderList.isEmpty()) {
        	final List<Order> existingTourOrders = new ArrayList<>(tour.getOrders());
        	final int index = existingTourOrders.size() - 1;
        	final List<Saving> savingsList = new ArrayList<>();
        	
        	for(Order order : configurationOrderList) {
        		savingsList.add(new Saving(existingTourOrders.get(index), order, depot));
        	}
        	
        	Collections.sort(savingsList);
        	
        	tour.addSourceOrder(savingsList.get(0).getDestinationOrder());
        	tour.addDestinationOrder(savingsList.get(0).getDestinationOrder());
        	
        	configurationOrderList.remove(savingsList.get(0).getDestinationOrder());
        }
                        		
        plan.addTour(tour);
        
		return plan;
	}

	@Override
	public void logStatistic() {
		// TODO Auto-generated method stub

	}

}
