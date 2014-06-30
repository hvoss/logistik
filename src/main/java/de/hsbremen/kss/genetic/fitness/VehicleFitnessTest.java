package de.hsbremen.kss.genetic.fitness;

import org.apache.commons.math3.util.FastMath;

import de.hsbremen.kss.model.Plan;

/**
 * This class calculates the fitness of a plan (individual of a population). The
 * fitness value is the sum of the length of a plan and the used vehicles. The
 * plan with the least fitness is the best plan. Every used vehicle is added to
 * the length of a plan. The importance of the vehicle is defined with an
 * factor.
 * 
 * @author david
 * 
 */
public class VehicleFitnessTest extends AbstractFitnessTest {

    /** factor for the total number of used vehicles */
    private final int numberOfVehicles;
    private final double factor;

    public VehicleFitnessTest(final int numberOfVehicles, final double factor) {
        this.numberOfVehicles = numberOfVehicles;
        this.factor = factor;
    }

    @Override
    public Double calculateFitness(final Plan plan) {

        final double length = plan.length();
        int vehicles = plan.getTours().size() - this.numberOfVehicles;
        vehicles = FastMath.max(vehicles, 0);

        final double totalFitness = length * this.factor * vehicles;

        return totalFitness;
    }

}
