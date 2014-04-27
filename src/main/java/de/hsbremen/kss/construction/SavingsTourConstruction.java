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
import de.hsbremen.kss.model.SavingTour;
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
		final Plan plan = new Plan(SavingsTourConstruction.class);
		
		final Vehicle vehicle = CollectionUtils.get(configuration.getVehicles(), 0);
        final Station depot = vehicle.getSourceDepot();
        Tour tour = new Tour(vehicle);
		
        final List<Order> configurationOrderList = new ArrayList<Order>(configuration.getOrders());
        
        // TODO find the best initial tour with the normal Savings-Algorithm
        
        tour.addSourceOrder(configurationOrderList.get(2));
        tour.addDestinationOrder(configurationOrderList.get(2));
        
        configurationOrderList.remove(2);
        
//        for (SavingTour savingTour : savingTourList){
//        	SavingsTourConstruction.LOG.info("Savings: " + savingTour.getExistingTour().getOrders()+ " => " + 
//        			savingTour.getAddingOrder() + ": " + savingTour.getSavingsValue());
//        }
                
        
        while(!configurationOrderList.isEmpty()){
        	final List<SavingTour> savingTourList = new ArrayList<>();
        	for (Order order : configurationOrderList){
            	savingTourList.add(new SavingTour(tour, order, depot));
            	savingTourList.add(new SavingTour(order, tour, depot));
            }
        	
        	Collections.sort(savingTourList);
        	
        	SavingsTourConstruction.LOG.info("best Element: " + savingTourList.get(0).getAddingOrder());
        	
        	if(savingTourList.get(0).getDirection()){
        		tour.addSourceOrder(savingTourList.get(0).getAddingOrder());
                tour.addDestinationOrder(savingTourList.get(0).getAddingOrder());
                SavingsTourConstruction.LOG.info("Direction: right");
        	} else{
        		Tour tempTour = new Tour(vehicle);
        		tempTour.addSourceOrder(savingTourList.get(0).getAddingOrder());
        		tempTour.addDestinationOrder(savingTourList.get(0).getAddingOrder());
        		SavingsTourConstruction.LOG.info("Direction: left");
        		for (Order order : tour.getOrders()){
        			tempTour.addSourceOrder(order);
        			tempTour.addDestinationOrder(order);
        		}
        		tour = tempTour;
        	}
        	
        	
        	configurationOrderList.remove(savingTourList.get(0).getAddingOrder());
        }
        
//        configurationOrderList.remove(savingTourList.get(0).getAddingOrder());
        
        SavingsTourConstruction.LOG.info("Size of the list: " + configurationOrderList.size());
        		
        plan.addTour(tour);
        
		return plan;
	}

	@Override
	public void logStatistic() {
		// TODO Auto-generated method stub

	}

}
