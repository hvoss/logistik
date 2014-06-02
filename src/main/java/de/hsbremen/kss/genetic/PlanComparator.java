package de.hsbremen.kss.genetic;

import java.util.Comparator;

import de.hsbremen.kss.fitness.FitnessTest;
import de.hsbremen.kss.model.Plan;

public final class PlanComparator implements Comparator<Plan> {

    private final FitnessTest fitnessTest;

    public PlanComparator(final FitnessTest fitnessTest) {
        this.fitnessTest = fitnessTest;
    }

    @Override
    public int compare(final Plan firstPlan, final Plan secondPlan) {
        final Double firstFitness = this.fitnessTest.calculateFitness(firstPlan);
        final Double secondFitness = this.fitnessTest.calculateFitness(secondPlan);

        if (firstFitness < secondFitness) {
            return -1;
        } else if (firstFitness > secondFitness) {
            return 1;
        } else {
            return 0;
        }
    }
}
