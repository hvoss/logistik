package de.hsbremen.kss.fitness;

import java.util.Collection;

import de.hsbremen.kss.model.Plan;

public abstract class AbstractFitnessTest implements FitnessTest {

    @Override
    public Double avgFitness(final Collection<Plan> plans) {
        double avg = 0;

        for (final Plan plan : plans) {
            avg += calculateFitness(plan) / plans.size();
        }

        return avg;
    }
}
