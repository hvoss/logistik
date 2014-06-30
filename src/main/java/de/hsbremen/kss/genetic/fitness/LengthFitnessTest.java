package de.hsbremen.kss.genetic.fitness;

import de.hsbremen.kss.model.Plan;

public class LengthFitnessTest extends AbstractFitnessTest {

    private final double factor;

    public LengthFitnessTest(final double factor) {
        this.factor = factor;
    }

    @Override
    public Double calculateFitness(final Plan plan) {
        return this.factor * plan.length();
    }

}
