package de.hsbremen.kss.model;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;

public class SavingOrder implements Comparable<SavingOrder>{
	
	private Order order;
	private Station depot;
	private double savingsValue;
	
	public SavingOrder(Order order, Station depot){
		this.order = order;
		this.depot = depot;
		this.savingsValue = depot.distance(order.getSourceStation()) + depot.distance(order.getDestinationStation())
				- order.getSourceStation().distance(order.getDestinationStation());
	}

	public Order getOrder() {
		return order;
	}

	public Station getDepot() {
		return depot;
	}


	public double getSavingsValue() {
		return savingsValue;
	}

	@Override
	public int compareTo(SavingOrder savingOrder) {
		if (this.savingsValue < savingOrder.savingsValue){
			return 1;
		} else{
			return -1;
		}
	}

}
