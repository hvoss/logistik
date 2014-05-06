package de.hsbremen.kss.construction;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.model.Plan;

/**
 * construction method of logistics problems.
 * 
 * @author henrik
 * 
 */
public interface Construction {

    /**
     * construct a plan for the given configuration.
     * 
     * @param configuration
     *            configuration of a scenario
     * @return a valid solution
     */
    Plan constructPlan(Configuration configuration);

    /**
     * logs some statistics.
     */
    void logStatistic();
}
