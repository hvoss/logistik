package de.hsbremen.kss.genetic;

import de.hsbremen.kss.model.Plan;

public interface Crossover {

    Plan crossover(Plan firstPlan, Plan secondPlan);
}
