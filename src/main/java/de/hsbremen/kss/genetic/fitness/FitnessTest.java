package de.hsbremen.kss.genetic.fitness;

import java.util.Collection;

import de.hsbremen.kss.model.Plan;

public interface FitnessTest {

    Double calculateFitness(Plan plan);

    Double avgFitness(Collection<Plan> plan);
}
