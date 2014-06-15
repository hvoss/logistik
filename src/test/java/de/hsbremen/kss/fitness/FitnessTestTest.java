package de.hsbremen.kss.fitness;

import org.junit.Before;
import org.junit.Test;

import de.hsbremen.kss.configuration.SampleOrders;
import de.hsbremen.kss.configuration.SampleVehicle;
import de.hsbremen.kss.construction.SweepConstruction;
import de.hsbremen.kss.fitness.CapacityFitnessTest;
import de.hsbremen.kss.fitness.FitnessTestBuilder;
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
		FitnessTestBuilder fitnessTestBuilder = new FitnessTestBuilder();
		fitnessTestBuilder.addFitnessTest(new LengthFitnessTest());
		
		double firstFitnessTestBuilderValue = fitnessTestBuilder.calculateFitness(this.firstPlan);
		System.out.println("Fitness Value (Length) (first Plan): " + firstFitnessTestBuilderValue);
		
		double secondFitnessTestBuilderValue = fitnessTestBuilder.calculateFitness(this.secondPlan);
		System.out.println("Fitness Value (Length) (second Plan): " + secondFitnessTestBuilderValue);		
	}
	
	@Test
	public void testVehicle() {
		
		FitnessTestBuilder fitnessTestBuilder = new FitnessTestBuilder();
		fitnessTestBuilder.addFitnessTest(new LengthFitnessTest())
			.addFitnessTest(new VehicleFitnessTest());
		
		double firstFitnessTestBuilderValue = fitnessTestBuilder.calculateFitness(this.firstPlan);
		System.out.println("Fitness Value (Length + Vehicle) (first Plan): " + firstFitnessTestBuilderValue);
		
		double secondFitnessTestBuilderValue = fitnessTestBuilder.calculateFitness(this.secondPlan);
		System.out.println("Fitness Value (Length + Vehicle) (second Plan): " + secondFitnessTestBuilderValue);
	}
	
	@Test
	public void testCapacity() {
		FitnessTestBuilder fitnessTestBuilder = new FitnessTestBuilder();
		fitnessTestBuilder.addFitnessTest(new LengthFitnessTest())
			.addFitnessTest(new CapacityFitnessTest());
		
		double firstFitnessTestBuilderValue = fitnessTestBuilder.calculateFitness(this.firstPlan);
		System.out.println("Fitness Value (Length + Capacity) (first Plan): " + firstFitnessTestBuilderValue);
		
		double secondFitnessTestBuilderValue = fitnessTestBuilder.calculateFitness(this.secondPlan);
		System.out.println("Fitness Value (Length + Capacity) (second Plan): " + secondFitnessTestBuilderValue);
	}

}
