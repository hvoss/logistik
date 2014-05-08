package de.hsbremen.kss.model;

import de.hsbremen.kss.configuration.Station;

public class SavingStation implements Comparable<SavingStation>{
	
	private Station sourceStation;
	private Station destinationStation;
	private Station depot;
	private double savingsValue;
	
	public SavingStation(Station sourceStation, Station destinationStation, Station depot) {
		this.sourceStation = sourceStation;
		this.destinationStation = destinationStation;
		this.depot = depot;
		this.savingsValue = depot.distance(sourceStation) + depot.distance(destinationStation)
				- sourceStation.distance(destinationStation);
	}

	public Station getSourceStation() {
		return sourceStation;
	}



	public Station getDestinationStation() {
		return destinationStation;
	}



	public Station getDepot() {
		return depot;
	}



	public double getSavingsValue() {
		return savingsValue;
	}



	@Override
	public int compareTo(SavingStation savingStation) {
		if (this.savingsValue < savingStation.savingsValue) {
			return 1;
		} else {
			return -1;
		}
	}

}
