package de.hsbremen.kss.model;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;

/**
 * Calculates the Savings-Value between the destination station of the first order and the
 * source station of the second order
 * 
 * @author david
 *
 */
public class Saving implements Comparable<Saving>{
	
	private Order sourceOrder;
	private Order destinationOrder;
	private Station depot;
	private double savingsValue;
	
	public Saving(Order sourceOrder, Order destinationOrder, Station depot){
		this.sourceOrder = sourceOrder;
		this.destinationOrder = destinationOrder;
		this.depot = depot;
		this.savingsValue = depot.distance(sourceOrder.getDestination()) + depot.distance(destinationOrder.getSource())
				- sourceOrder.getDestination().distance(destinationOrder.getSource());
	}

	public Order getSourceOrder() {
		return this.sourceOrder;
	}

	public Order getDestinationOrder() {
		return this.destinationOrder;
	}

	public Station getDepot() {
		return this.depot;
	}
	
	public double getSavingsValue(){
		return this.savingsValue;
	}

	@Override
	public int compareTo(Saving saving) {
		if (this.savingsValue < saving.savingsValue){
			return 1;
		} else{
			return -1;
		}
	}

}
