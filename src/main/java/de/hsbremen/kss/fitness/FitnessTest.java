package de.hsbremen.kss.fitness;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.model.Plan;

public interface FitnessTest {

    Double calculateFitness(Configuration configuration, Plan plan);
}
