package de.hsbremen.kss.fitness;

import org.apache.commons.math3.util.FastMath;

import de.hsbremen.kss.configuration.Station;
import de.hsbremen.kss.model.Action;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;

public class LoadingFitnessTest extends AbstractFitnessTest {

    /** factor for the total number of overloads */
    private static final double NUMBER_OF_OVERLOADS = 1.6;

    @Override
    public Double calculateFitness(final Plan plan) {

        final double length = plan.length();
        double fitness = 0;

        for (final Tour tour : plan.getTours()) {
            Station lastStation = null;
            double time = tour.getVehicle().getTimeWindow().getStart();
            for (final Action action : tour.getActions()) {
                if (lastStation != null) {
                    time += tour.getVehicle().calculateTavelingTime(lastStation, action.getStation());
                }
                if (action.timewindow().getEnd() < time) {
                    final double diff = time - action.timewindow().getEnd();
                    fitness += length * FastMath.pow(diff, LoadingFitnessTest.NUMBER_OF_OVERLOADS);
                }

                time += action.duration();

                lastStation = action.getStation();
            }
        }

        return fitness;
    }

}
