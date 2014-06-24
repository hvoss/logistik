package de.hsbremen.kss.fitness;

import org.apache.commons.math3.util.FastMath;

import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;

public class VehicleMakespanFitnessTest extends AbstractFitnessTest {

    /** factor for the total number of overloads */
    private static final double NUMBER_OF_OVERLOADS = 1.2d;

    @Override
    public Double calculateFitness(final Plan plan) {

        final double length = plan.length();
        double fitness = 0;

        for (final Tour tour : plan.getTours()) {
            final double diff = tour.actualDuration() - tour.getVehicle().getTimeWindow().timespan();
            if (diff > 0) {
                fitness += length * FastMath.pow(diff, VehicleMakespanFitnessTest.NUMBER_OF_OVERLOADS);
            }
        }

        return fitness;
    }

}
