package de.hsbremen.kss.fitness;

import java.util.ArrayList;
import java.util.List;

import de.hsbremen.kss.model.Plan;

public class FitnessTestBuilder extends AbstractFitnessTest {

    private final List<FitnessTest> fitnessTests;

    public FitnessTestBuilder() {
        this.fitnessTests = new ArrayList<FitnessTest>();
    }

    public FitnessTestBuilder addFitnessTest(final FitnessTest fitnessTest) {
        if (!this.fitnessTests.contains(fitnessTest)) {
            this.fitnessTests.add(fitnessTest);
        }
        return this;
    }

    @Override
    public Double calculateFitness(final Plan plan) {
        if (plan.getFitness() != null) {
            return plan.getFitness();
        }

        double totalFitness = 0.0;

        for (final FitnessTest fitnessTest : this.fitnessTests) {
            totalFitness += fitnessTest.calculateFitness(plan);
        }

        plan.setFitness(totalFitness);

        return totalFitness;
    }

}
