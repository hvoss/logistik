package de.hsbremen.kss.construction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.model.Plan;

/**
 * calls a construction method as long as it does not provide a better result.
 * 
 * @author henrik
 * 
 */
public final class MissAbortMultipleConstruction implements Construction {

    /** logging interface */
    private static final Logger LOG = LoggerFactory.getLogger(MissAbortMultipleConstruction.class);

    /** construction method which is used */
    private final Construction construction;

    /** number of misses to abort */
    private final int maxMisses;

    /** number of iterations */
    private int numOfIterations;

    /**
     * ctor.
     * 
     * @param construction
     *            construction method which is used
     * @param maxMisses
     *            number of misses to abort
     */
    public MissAbortMultipleConstruction(final Construction construction, final int maxMisses) {
        this.construction = construction;
        this.maxMisses = maxMisses;
    }

    @Override
    public Plan constructPlan(final Configuration configuration) {
        Plan bestRandomPlan = null;
        int missCounter = 0;
        this.numOfIterations = 0;
        while (true) {
            ++this.numOfIterations;
            final Plan randomPlan = this.construction.constructPlan(configuration);
            if (bestRandomPlan == null || bestRandomPlan.length() > randomPlan.length()) {
                bestRandomPlan = randomPlan;
                missCounter = 0;
            } else if (++missCounter > this.maxMisses) {
                break;
            }

        }
        final Plan plan = new Plan(MissAbortMultipleConstruction.class, bestRandomPlan);

        plan.lock();
        return plan;
    }

    @Override
    public void logStatistic() {
        MissAbortMultipleConstruction.LOG.info("best plan was found after " + this.numOfIterations + " iterations");
    }

}
