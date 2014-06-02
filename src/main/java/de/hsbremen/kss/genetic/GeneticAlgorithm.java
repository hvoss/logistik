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
     *            Configuration
     * @param plans
     *            Plans
     * @return Plan
     */
    Plan startOptimize(Configuration configuration, Collection<Plan> population);

    void setListener(GeneticAlgorithmListener listener);
}
