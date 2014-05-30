package de.hsbremen.kss.fitness;

import de.hsbremen.kss.model.Plan;

public class LengthFitnessTest implements FitnessTest {

    @Override
    public Double calculateFitness(final Plan plan) {
        return plan.length();
    }

}
