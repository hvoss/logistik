package de.hsbremen.kss.model;


public class Saving implements Comparable<Saving>{
	
	private Order sourceOrder;
	private Order destinationOrder;
	private Station depot;
	private double savingsValue;
	
	public Saving(Order sourceOrder, Order destinationOrder, Station depot){
		this.sourceOrder = sourceOrder;
		this.destinationOrder = destinationOrder;
		this.depot = depot;
		this.savingsValue = depot.distance(sourceOrder.getSource()) + depot.distance(destinationOrder.getSource())
				- sourceOrder.getSource().distance(destinationOrder.getSource());
	}

	public Order getSourceOrder() {
		return sourceOrder;
	}

	public Order getDestinationOrder() {
		return destinationOrder;
	}

	public Station getDepot() {
		return depot;
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
