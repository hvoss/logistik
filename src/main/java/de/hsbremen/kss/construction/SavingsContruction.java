package de.hsbremen.kss.construction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hsbremen.kss.model.Configuration;
import de.hsbremen.kss.model.Order;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Saving;
import de.hsbremen.kss.model.Station;

/**
 * Realizes the sequential Savings-Algorithm
 * 
 * @author david
 *
 */
public class SavingsContruction implements Construction {
	
	@Override
	public Plan constructPlan(Configuration configuration) {
		
		List<Order> orderList = new ArrayList<Order>(configuration.getOrders());
		List<Station> stationList = new ArrayList<Station>(configuration.getStations());
		List<Saving> savingList = new ArrayList<Saving>();
		List<Order> savingsOrderList = new ArrayList<Order>();
		Set<Order> processedOrders = new HashSet<>();
		Station depot = stationList.get(2);
		
		for (Order sourceOrder : orderList){
			processedOrders.add(sourceOrder);
			for (Order destinationOrder : orderList){
				if (!processedOrders.contains(destinationOrder)){
					savingList.add(new Saving(sourceOrder, destinationOrder, depot));
				}
			}
		}
		
		Collections.sort(savingList);
		
		System.out.println("Depot: " + depot.getName());
		
		for (Saving saving : savingList){
			System.out.println("Savings: " + saving.getSourceOrder().getSource().getName() + " " + saving.getDestinationOrder()
					.getSource().getName() + ": " + saving.getSavingsValue());
		}
		
		System.out.println("Best Pair: " + savingList.get(0).getSourceOrder().getSource().getName() + " " + 
				savingList.get(0).getDestinationOrder().getSource().getName());
		savingsOrderList.add(savingList.get(0).getSourceOrder());
		savingsOrderList.add(savingList.get(0).getDestinationOrder());
		savingList.remove(0);
				
		while(savingsOrderList.size() < orderList.size()){
			int indexNextPair = searchNextPair(savingList, savingsOrderList.get(0), savingsOrderList.get(savingsOrderList.size()-1));
			System.out.println("Next Pair: " + savingList.get(indexNextPair).getSourceOrder().getSource().getName() + " " + 
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
		
		System.out.print("Route: ");
		for (Order order : savingsOrderList){
			if (order.equals(savingsOrderList.get(savingsOrderList.size()-1))){
				System.out.print(order.getSource().getName());
			} else{
				System.out.print(order.getSource().getName() + " - ");
			}
		}
		
		return null;
	}
	
	private int searchNextPair(List<Saving> savingList, Order firstElement, Order lastElement){
		int indexNextPair=-1;
		for (Saving saving : savingList){
			if (saving.getSourceOrder().equals(firstElement) || saving.getSourceOrder().equals(lastElement) 
					|| saving.getDestinationOrder().equals(firstElement) || saving.getDestinationOrder().
					equals(lastElement)){
				indexNextPair = savingList.indexOf(saving);
				break;
			}
		}
		return indexNextPair;
	}

}
