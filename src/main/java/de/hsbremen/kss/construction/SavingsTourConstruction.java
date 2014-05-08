package de.hsbremen.kss.construction;

import java.util.ArrayList;
import java.util.Collection;
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
        final Set<Order> visitedOrders = new HashSet<>(configurationOrders.size());
        
        Order lastOrder = null;
        Order actualOrder;
        
        // define the actual order with savings over the two stations of an order
        if(startOrder == null){
        	final List<SavingOrder> savingsOrderList = new ArrayList<SavingOrder>(configurationOrders.size());
        	
        	for(Order order : configurationOrders) {
        		savingsOrderList.add(new SavingOrder(order, depot));
        	}
        	Collections.sort(savingsOrderList);
        	actualOrder = savingsOrderList.get(0).getOrder();
        } else {
        	actualOrder = startOrder;
        }

        while(!configurationOrders.isEmpty()) {
        	
        	final List<Saving> savingsList = new ArrayList<>();
        	
        	// calculate savings value and define actual order
        	if(lastOrder != null) {
        		for (Order order : configurationOrders) {
        			savingsList.add(new Saving(lastOrder, order, depot));
        		}
        		
        		Collections.sort(savingsList);
        		actualOrder = savingsList.get(0).getDestinationOrder();
        		SavingsTourConstruction.LOG.info("New Iteration");
        		for(Saving saving : savingsList) {
        			SavingsTourConstruction.LOG.info("Savingsvalue: " + saving.getSavingsValue() 
            				+ " ID: " + saving.getDestinationOrder().getId());
        		}
        	}
            
        	Station actualStation = actualOrder.getSource();
        	final Set<Order> newSourceOrders = new HashSet<>(actualStation.getSourceOrders());
        	newSourceOrders.removeAll(visitedOrders);
        	final Set<Order> loadedSourceOrders = new HashSet<>();
        	
        	// load new orders for the actual station
        	for(Order newSourceorder : newSourceOrders) {
        		if(tour.freeSpace() >= newSourceorder.weightOfProducts()) {
        			tour.addSourceOrder(newSourceorder);
        			loadedSourceOrders.add(newSourceorder);
        		}
        	}
        	
        	// unload orders
        	final List<Order> bestOrderSequence = getBestOrderSequence(loadedSourceOrders);
        	final int index = bestOrderSequence.size() - 1 ;
        	for(Order order : bestOrderSequence) {
        		tour.addDestinationOrder(order);
        	}
        	
        	visitedOrders.addAll(loadedSourceOrders);
        	configurationOrders.removeAll(loadedSourceOrders);
        	lastOrder = bestOrderSequence.get(index);
        }
                        		
        plan.addTour(tour);
        
		return plan;
	}

	@Override
	public void logStatistic() {
		// TODO Auto-generated method stub

	}
	
	private List<Order> getBestOrderSequence(Collection<Order> loadedSourceOrders) {
		// TODO find best order sequence
		final List<Order> bestOrderSequence = new ArrayList<>(loadedSourceOrders.size());
		for(Order order : loadedSourceOrders) {
			bestOrderSequence.add(order);
		}
		return bestOrderSequence;
	}

}
