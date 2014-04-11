package de.hsbremen.kss.construction;

import java.util.ArrayList;
import java.util.List;

import de.hsbremen.kss.model.Configuration;
import de.hsbremen.kss.model.Order;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Station;

public class SavingsContruction implements Construction {

	private List<Order> orderList;
	private double savingsValue;
	private Order sourceOrder;
	private Order destinationOrder;
	private Station depot;
	
	@Override
	public Plan constructPlan(Configuration configuration) {
		orderList = new ArrayList<Order>(configuration.getOrders());
		List<Station> stationList = new ArrayList<Station>(configuration.getStations());
		depot = stationList.get(3);
		calculateSavingsValue();
		return null;
	}
	
	private void calculateSavingsValue(){
		for (Order order1 : orderList){
			for (Order order2 : orderList){
				if (order1.getId() != order2.getId()){
					double savingsValueTemp = depot.distance(order1.getSource()) + depot.distance(order2.getSource()) - 
							order1.getSource().distance(order2.getSource());
					System.out.println(order1.getSource().getName() + " " + order2.getSource().getName() + " Savings: " 
							+ savingsValue);
				}
			}
		}
	}

}
