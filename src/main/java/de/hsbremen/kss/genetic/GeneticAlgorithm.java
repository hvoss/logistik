package de.hsbremen.kss.genetic;

import java.util.Collection;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.model.Plan;

/**
 * 
 * @author olli
 * 
 */
public interface GeneticAlgorithm {

    /**
     * 
     * @param configuration
     *            the given configuration
     * @param population
     *            the initial population
     * @return the best plan
     */
    Plan startOptimize(Configuration configuration, Collection<Plan> population);
}
