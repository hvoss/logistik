package de.hsbremen.kss.construction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.model.Plan;

/**
 * calls a other construction method multiple times and returns the best result.
 * 
 * @author henrik
 * 
 */
public final class FixMultipleRandomConstruction implements CloneableConstruction {

    /** logging interface */
    private static final Logger LOG = LoggerFactory.getLogger(FixMultipleRandomConstruction.class);

    /** construction method which is used */
    private final Construction construction;

    /** number of constructions tries */
    private final int numOfRandomPlans;

    /** stores number of the iteration in which the best plan was found */
    private int planFoundInIteration;

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
                this.planFoundInIteration = i;
            }
        }
        return new Plan(FixMultipleRandomConstruction.class, bestRandomPlan);
    }

    @Override
    public void logStatistic() {
        FixMultipleRandomConstruction.LOG.info("best plan was found in iteration " + this.planFoundInIteration + " of " + this.numOfRandomPlans);
    }

    @Override
    public CloneableConstruction clone() {
        try {
            return (CloneableConstruction) super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
