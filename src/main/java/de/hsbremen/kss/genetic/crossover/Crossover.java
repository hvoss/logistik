package de.hsbremen.kss.genetic.crossover;

import de.hsbremen.kss.model.Plan;

public interface Crossover {

    Plan crossover(Plan firstPlan, Plan secondPlan);
}
