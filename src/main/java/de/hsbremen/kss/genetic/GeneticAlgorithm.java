package de.hsbremen.kss.genetic;

import java.util.Collection;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.model.Plan;

public interface GeneticAlgorithm {

    Plan optimize(Configuration configuration, Collection<Plan> plans);
}
