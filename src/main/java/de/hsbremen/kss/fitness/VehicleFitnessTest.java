package de.hsbremen.kss.fitness;

import de.hsbremen.kss.model.Plan;

/**
 * This class calculates the fitness of a plan (individual of a population). The fitness value
 * is the sum of the length of a plan and the used vehicles. The plan with the least fitness is the
 * best plan. Every used vehicle is added to the length of a plan. The importance of the vehicle is
 * defined with an factor.
 * 
 * @author david
 *
 */
public class VehicleFitnessTest extends AbstractFitnessTest {
	
	/** factor for the total number of used vehicles */
	private static final double NUMBER_OF_VEHICLES = 0.1;

	@Override
	public Double calculateFitness(final Plan plan) {
		
		double length = plan.length();
		int vehicles = plan.getTours().size();
		double totalFitness = length + length*NUMBER_OF_VEHICLES*vehicles;
		
		return totalFitness;
	}

}
