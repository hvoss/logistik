package de.hsbremen.kss.genetic.mutation;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.model.Plan;

public interface Mutation {

    Plan mutate(Configuration configuration, Plan plan);
}
