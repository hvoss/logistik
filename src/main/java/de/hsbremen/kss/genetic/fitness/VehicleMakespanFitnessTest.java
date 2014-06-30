package de.hsbremen.kss.genetic.fitness;

import org.apache.commons.math3.util.FastMath;

import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;

public class VehicleMakespanFitnessTest extends AbstractFitnessTest {

    /** factor for the total difference of vehicle time */
    private final double diffOfVehicle;

    public VehicleMakespanFitnessTest(final double diffOfVehicle) {
        this.diffOfVehicle = diffOfVehicle;
    }

    @Override
    public Double calculateFitness(final Plan plan) {

        final double length = plan.length();
        double fitness = 0;

        for (final Tour tour : plan.getTours()) {
            final double diff = tour.actualDuration() - tour.getVehicle().getTimeWindow().timespan();
            if (diff > 0) {
                fitness += length * FastMath.pow(diff + 1, this.diffOfVehicle);
            }
        }

        return fitness;
    }

}
