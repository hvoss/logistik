package de.hsbremen.kss.model;

import java.util.ArrayList;
import java.util.List;

import de.hsbremen.kss.configuration.Order;
import de.hsbremen.kss.configuration.Station;

/**
 * Calculates the Savings-Value between a given tour and an given order
 * 
 * @author david
 *
 */
public class SavingTour implements Comparable<SavingTour>{
	
	private Tour existingTour;
	private Order addingOrder;
	private Station depot;
	private double savingsValue;
	private boolean direction;
	
	public SavingTour(Tour existingTour, Order addingOrder, Station depot){
		this.existingTour = existingTour;
		this.addingOrder = addingOrder;
		this.depot = depot;
		final List <Order> existingTourorders = new ArrayList<>(existingTour.getOrders()); 
		this.savingsValue = depot.distance(existingTourorders.get(existingTourorders.size()-1).getDestination()) 
				+ depot.distance(addingOrder.getSource()) - existingTourorders.get(existingTourorders.size()-1).
				getDestination().distance(addingOrder.getSource());
		this.direction = true;
	}
	
	public SavingTour(Order addingOrder, Tour existingTour, Station depot){
		this.existingTour = existingTour;
		this.addingOrder = addingOrder;
		this.depot = depot;
		final List <Order> existingTourorders = new ArrayList<>(existingTour.getOrders()); 
		this.savingsValue = depot.distance(addingOrder.getDestination()) + depot.distance(existingTourorders.get(0).getSource())
				- addingOrder.getDestination().distance(existingTourorders.get(0).getSource());
		this.direction = false;
	}

	public Tour getExistingTour() {
		return this.existingTour;
	}

	public Order getAddingOrder() {
		return this.addingOrder;
	}

	public Station getDepot() {
		return this.depot;
	}

	public double getSavingsValue() {
		return this.savingsValue;
	}
	
	public boolean getDirection(){
		return this.direction;
	}

	@Override
	public int compareTo(SavingTour savingTour) {
		if (this.savingsValue < savingTour.savingsValue){
			return 1;
		} else{
			return -1;
		}
	}
	
}
