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
import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Saving;
import de.hsbremen.kss.configuration.Station;

/**
 * Realizes the sequential Savings-Algorithm
 * Has a problem, when the depot is in one of the orders
 * 
 * @author david
 *
 */
public class SavingsContruction implements Construction {
	
	private static final Logger LOG = LoggerFactory.getLogger(SavingsContruction.class);
	
	@Override
	public Plan constructPlan(Configuration configuration) {
		
		List<Order> orderList = new ArrayList<Order>(configuration.getOrders());
		List<Saving> savingList = new ArrayList<>();
		List<Order> savingsOrderList = new ArrayList<>();
		List<Order> processedOrders = new ArrayList<>();
		Vehicle vehicle = CollectionUtils.get(configuration.getVehicles(), 0);
		Station depot = vehicle.getSourceDepot();
		
		for (Order sourceOrder : orderList){
			processedOrders.add(sourceOrder);
			for (Order destinationOrder : orderList){
				if (!processedOrders.contains(destinationOrder)){
					savingList.add(new Saving(sourceOrder, destinationOrder, depot));
				}
			}
		}
		
		Collections.sort(savingList);
		
		LOG.info("Depot: " + depot.getName());
		
		for (Saving saving : savingList){
			LOG.info("Savings: " + saving.getSourceOrder().getSource().getName() + " => " + saving.getDestinationOrder()
					.getSource().getName() + ": " + saving.getSavingsValue());
		}
		
		LOG.info("Best Pair: " + savingList.get(0).getSourceOrder().getSource().getName() + " " + 
				savingList.get(0).getDestinationOrder().getSource().getName());
		savingsOrderList.add(savingList.get(0).getSourceOrder());
		savingsOrderList.add(savingList.get(0).getDestinationOrder());
		savingList.remove(0);
				
		while(savingsOrderList.size() < orderList.size()){
			int indexNextPair = searchNextPair(savingList, savingsOrderList);
			LOG.info("Next Pair: " + savingList.get(indexNextPair).getSourceOrder().getSource().getName() + " " + 
			savingList.get(indexNextPair).getDestinationOrder().getSource().getName());
			
			if(!savingsOrderList.contains(savingList.get(indexNextPair).getSourceOrder())){
				if (savingsOrderList.get(0).equals(savingList.get(indexNextPair).getDestinationOrder())){
					savingsOrderList.add(0, savingList.get(indexNextPair).getSourceOrder());
				} else{
					savingsOrderList.add(savingList.get(indexNextPair).getSourceOrder());
				} 
			} else{
				if (savingsOrderList.get(0).equals(savingList.get(indexNextPair).getSourceOrder())){
					savingsOrderList.add(0, savingList.get(indexNextPair).getDestinationOrder());
				} else{
					savingsOrderList.add(savingList.get(indexNextPair).getDestinationOrder());
				}
			}
			savingList.remove(indexNextPair);
		}
		
		logTravel(savingsOrderList, depot);
		
		return null;
	}
	
	private int searchNextPair(List<Saving> savingList, List<Order> savingsOrderList){
		int indexNextPair=-1;
		Order firstElement = savingsOrderList.get(0);
		Order lastElement = savingsOrderList.get(savingsOrderList.size()-1);
		for (Saving saving : savingList){
			if (!(savingsOrderList.contains(saving.getSourceOrder()) && savingsOrderList.contains(saving.getDestinationOrder()))){
				if (saving.getSourceOrder().equals(firstElement) || saving.getSourceOrder().equals(lastElement) 
						|| saving.getDestinationOrder().equals(firstElement) || saving.getDestinationOrder().
						equals(lastElement)){
					indexNextPair = savingList.indexOf(saving);
					break;
				}
			}
		}
		return indexNextPair;
	}
	
	private static void logTravel(List<Order> savingsOrderList, Station depot){
		List<Station> stationRoute = new ArrayList<>();
		for(Order order : savingsOrderList){
			stationRoute.add(order.getSource());
		}
		LOG.info("Route: ");
		stationRoute.add(0, depot);
		stationRoute.add(depot);
		for (int i=0; i<stationRoute.size()-1;i++){
			LOG.info(stationRoute.get(i).getName() + " => "+ stationRoute.get(i+1).getName() 
					+ " (" + Math.round(stationRoute.get(i).distance(stationRoute.get(i+1))) +" km)");
		}
		
	}

}
