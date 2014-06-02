package de.hsbremen.kss.genetic;

import java.util.List;

import de.hsbremen.kss.model.Plan;

public interface Selection {

    Plan select(List<Plan> sortedPopulation);
}
