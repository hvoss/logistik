package de.hsbremen.fitness;

import org.junit.Before;
import org.junit.Test;

import de.hsbremen.kss.configuration.SampleOrders;
import de.hsbremen.kss.configuration.SampleVehicle;
import de.hsbremen.kss.construction.SweepConstruction;
import de.hsbremen.kss.fitness.CapacityFitnessTest;
import de.hsbremen.kss.fitness.FitnessTest;
import de.hsbremen.kss.fitness.LengthFitnessTest;
import de.hsbremen.kss.fitness.VehicleFitnessTest;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;

public class FitnessTestTest {
	
	private Plan firstPlan;
	private Plan secondPlan;
	
	@Before
	public void setup() {
		this.firstPlan = new Plan(SweepConstruction.class);
		final Tour firstTour = this.firstPlan.newTour(SampleVehicle.CAPACITY_LKW);
		
		this.secondPlan = new Plan(SweepConstruction.class);
		final Tour secondTour = this.secondPlan.newTour(SampleVehicle.CAPACITY_LKW);
		final Tour thirdTour = this.secondPlan.newTour(SampleVehicle.CAPACITY_LKW);
		
		firstTour.leafSourceDepot();
		firstTour.addSourceOrder(SampleOrders.BERLIN_ELLENBOGEN);
		firstTour.addSourceOrder(SampleOrders.BREMEN_HAMURG);
		firstTour.addSourceOrder(SampleOrders.BREMEN_OSNABRUECK);
		firstTour.addSourceOrder(SampleOrders.MUENCHEN_HANNOVER);
		firstTour.addDestinationOrder(SampleOrders.BERLIN_ELLENBOGEN);
		firstTour.addDestinationOrder(SampleOrders.BREMEN_HAMURG);
		firstTour.addDestinationOrder(SampleOrders.BREMEN_OSNABRUECK);
		firstTour.addDestinationOrder(SampleOrders.MUENCHEN_HANNOVER);
		firstTour.gotoDestinationDepot();
		this.firstPlan.addTour(firstTour);
		
		secondTour.leafSourceDepot();
		secondTour.addSourceOrder(SampleOrders.BERLIN_ELLENBOGEN);
		secondTour.addSourceOrder(SampleOrders.BREMEN_HAMURG);
		secondTour.addDestinationOrder(SampleOrders.BERLIN_ELLENBOGEN);
		secondTour.addDestinationOrder(SampleOrders.BREMEN_HAMURG);
		secondTour.gotoDestinationDepot();
		this.secondPlan.addTour(secondTour);
		
		thirdTour.leafSourceDepot();
		thirdTour.addSourceOrder(SampleOrders.BREMEN_OSNABRUECK);
		thirdTour.addSourceOrder(SampleOrders.MUENCHEN_HANNOVER);
		thirdTour.addDestinationOrder(SampleOrders.BREMEN_OSNABRUECK);
		thirdTour.addDestinationOrder(SampleOrders.MUENCHEN_HANNOVER);
		thirdTour.gotoDestinationDepot();
		this.secondPlan.addTour(thirdTour);
	}
	
	@Test
	public void testLength() {		
		FitnessTest firstLengthFitnessTest = new LengthFitnessTest();
		double firstLengthFitnessValue = firstLengthFitnessTest.calculateFitness(this.firstPlan);
		System.out.println("Length Fitness Value (First Plan): " + firstLengthFitnessValue);
		
		FitnessTest secondLengthFitnessTest = new LengthFitnessTest();
		double secondLengthFitnessValue = secondLengthFitnessTest.calculateFitness(this.secondPlan);
		System.out.println("Length Fitness Value (Second Plan): " + secondLengthFitnessValue);		
	}
	
	@Test
	public void testVehicle() {
		FitnessTest firstVehicleFitnessTest = new VehicleFitnessTest();
		double firstVehicleFitnessValue = firstVehicleFitnessTest.calculateFitness(this.firstPlan);
		System.out.println("Vehicle Fitness Value (First Plan): " + firstVehicleFitnessValue);
		
		FitnessTest secondVehicleFitnessTest = new VehicleFitnessTest();
		double secondVehicleFitnessValue = secondVehicleFitnessTest.calculateFitness(this.secondPlan);
		System.out.println("Vehicle Fitness Value (Second Plan): " + secondVehicleFitnessValue);
	}
	
	@Test
	public void testCapacity() {
		FitnessTest firstCapacityFitnessTest = new CapacityFitnessTest();
		double firstCapacityFitnessValue = firstCapacityFitnessTest.calculateFitness(this.firstPlan);
		System.out.println("Capacity Fitness Value (First Plan): " + firstCapacityFitnessValue);
		
		FitnessTest secondCapacityFitnessTest = new CapacityFitnessTest();
		double secondCapacityFitnessValue = secondCapacityFitnessTest.calculateFitness(this.secondPlan);
		System.out.println("Capacity Fitness Value (Second Plan): " + secondCapacityFitnessValue);
	}

}
