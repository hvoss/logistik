package de.hsbremen.kss.genetic;

import java.util.Comparator;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.fitness.FitnessTest;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.validate.Validator;

public final class PlanComparator implements Comparator<Plan> {

    private final FitnessTest fitnessTest;

    private final Validator validator;

    private final Configuration configuration;

    public PlanComparator(final Configuration configuration, final FitnessTest fitnessTest, final Validator validator) {
        this.configuration = configuration;
        this.fitnessTest = fitnessTest;
        this.validator = validator;
    }

    @Override
    public int compare(final Plan firstPlan, final Plan secondPlan) {
        final Double firstFitness = this.fitnessTest.calculateFitness(firstPlan);
        final Double secondFitness = this.fitnessTest.calculateFitness(secondPlan);

        final boolean firstValid = this.validator.validate(this.configuration, firstPlan);
        final boolean secondValid = this.validator.validate(this.configuration, secondPlan);

        if (firstValid && !secondValid) {
            return -1;
        } else if (!firstValid && secondValid) {
            return 1;
        } else if (firstFitness < secondFitness) {
            return -1;
        } else if (firstFitness > secondFitness) {
            return 1;
        } else {
            return 0;
        }
    }
}
