package de.hsbremen.kss.validate;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.model.Plan;

/**
 * validotor of plans.
 * 
 * @author henrik
 * 
 */
public interface Validator {

    /**
     * validates a plan.
     * 
     * @param configuration
     *            configuration used for this plan.
     * @param plan
     *            the plan to validate
     * @return true, if the plan is valid
     */
    boolean validate(Configuration configuration, Plan plan);

    void enableLogging(boolean enable);
}
