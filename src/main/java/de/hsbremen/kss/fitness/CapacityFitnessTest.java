package de.hsbremen.kss.fitness;

import java.util.List;

import org.apache.commons.math3.util.FastMath;

import de.hsbremen.kss.configuration.Vehicle;
import de.hsbremen.kss.model.Action;
import de.hsbremen.kss.model.OrderLoadAction;
import de.hsbremen.kss.model.OrderUnloadAction;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.model.Tour;

public class CapacityFitnessTest extends AbstractFitnessTest {

    /** factor for the total number of overloads */
    private static final double NUMBER_OF_OVERLOADS = 10;

    @Override
    public Double calculateFitness(final Plan plan) {

        final double length = plan.length();
        final int overloads = getNumberOfOverloads(plan);

        final double totalFitness = length + FastMath.pow(overloads, CapacityFitnessTest.NUMBER_OF_OVERLOADS);

        return totalFitness;
    }

    private int getNumberOfOverloads(final Plan plan) {

        int overloads = 0;

        for (final Tour tour : plan.getTours()) {
            final Vehicle vehicle = tour.getVehicle();
            int amount = 0;
            final List<Action> actions = tour.getActions();

            for (final Action action : actions) {

                if (action instanceof OrderUnloadAction) {
                    final OrderUnloadAction orderUnloadAction = (OrderUnloadAction) action;
                    amount -= orderUnloadAction.getOrder().getAmount();
                }

                if (action instanceof OrderLoadAction) {
                    final OrderLoadAction orderLoadAction = (OrderLoadAction) action;
                    amount += orderLoadAction.getOrder().getAmount();
                }

                if (amount > vehicle.getCapacity()) {
                    overloads++;
                }
            }
        }

        return overloads;
    }

}
