package de.hsbremen.kss.construction;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.model.Plan;

/**
 * calls a other construction method multiple times and returns the best result.
 * 
 * @author henrik
 * 
 */
public final class FixMultipleRandomConstruction implements Construction {

    /** construction method which is used */
    private final Construction construction;

    /** number of constructions tries */
    private final int numOfRandomPlans;

    /**
     * ctor.
     * 
     * @param construction
     *            construction method which is used
     * @param numOfRandomPlans
     *            number of constructions tries
     */
    public FixMultipleRandomConstruction(final Construction construction, final int numOfRandomPlans) {
        this.construction = construction;
        this.numOfRandomPlans = numOfRandomPlans;
    }

    @Override
    public Plan constructPlan(final Configuration configuration) {
        Plan bestRandomPlan = null;
        for (int i = 0; i < this.numOfRandomPlans; i++) {
            final Plan randomPlan = this.construction.constructPlan(configuration);
            if (bestRandomPlan == null || bestRandomPlan.length() > randomPlan.length()) {
                bestRandomPlan = randomPlan;
            }
        }
        return new Plan(FixMultipleRandomConstruction.class, bestRandomPlan);
    }

}
