package de.hsbremen.kss.genetic.crossover;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.model.Plan;

public interface Crossover {

    Plan crossover(Configuration configuration, Plan firstPlan, Plan secondPlan);
}
