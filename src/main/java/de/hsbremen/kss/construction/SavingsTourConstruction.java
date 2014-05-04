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
import de.hsbremen.kss.model.SavingOrder;
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
        final List<SavingOrder> savingOrderList = new ArrayList<>(configurationOrderList.size());
        
        List<Integer> indexes = getOrdersfromActualStation(configurationOrderList, depot);
        
        // find the best initial tour with the normal Savings-Algorithm
        for (Order order : configurationOrderList){
        	savingOrderList.add(new SavingOrder(order, depot));
        }
        
        Collections.sort(savingOrderList);
                
        tour.addSourceOrder(savingOrderList.get(0).getOrder());
        tour.addDestinationOrder(savingOrderList.get(0).getOrder());
        
        configurationOrderList.remove(savingOrderList.get(0).getOrder());
        
        // add all orders to the tour. Order with the best savings value first
        while(!configurationOrderList.isEmpty()){
        	final List<SavingTour> savingTourList = new ArrayList<>();
        	for (Order order : configurationOrderList){
            	savingTourList.add(new SavingTour(tour, order, depot));
            	savingTourList.add(new SavingTour(order, tour, depot));
            }
        	
        	Collections.sort(savingTourList);
        	
//        	SavingsTourConstruction.LOG.info("best Element: " + savingTourList.get(0).getAddingOrder());
        	
//        	if(savingTourList.get(0).getDirection()){
        		tour.addSourceOrder(savingTourList.get(0).getAddingOrder());
                tour.addDestinationOrder(savingTourList.get(0).getAddingOrder());
                SavingsTourConstruction.LOG.info("Order to add: " + savingTourList.get(0).getAddingOrder());
                SavingsTourConstruction.LOG.info("Direction: right");
//        	} else{
//        		Tour tempTour = new Tour(vehicle);
//        		tempTour.addSourceOrder(savingTourList.get(0).getAddingOrder());
//        		tempTour.addDestinationOrder(savingTourList.get(0).getAddingOrder());
//        		SavingsTourConstruction.LOG.info("Order to add: " + savingTourList.get(0).getAddingOrder());
//        		SavingsTourConstruction.LOG.info("Direction: left");
//        		for (Order order : tour.getOrders()){
//        			tempTour.addSourceOrder(order);
//        			tempTour.addDestinationOrder(order);
//        		}
//        		tour = tempTour;
//        	}
        	
        	
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
	
	/**
	 * Returns orders, where the source equals a given station
	 * 
	 * @param orders
	 * @param station
	 * @return
	 */
	private List<Integer> getOrdersfromActualStation(final List<Order> orders, Station station){
		List<Integer> indexes = new ArrayList<>();
		for(int i=0; i<orders.size();i++){
			if(orders.get(i).getSource().equals(station)){
				indexes.add(i);
			}
		}
		return indexes;
	}

}
