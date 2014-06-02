package de.hsbremen.kss.genetic;

import de.hsbremen.kss.model.Plan;

public interface Mutation {

    Plan mutate(Plan plan);
}
