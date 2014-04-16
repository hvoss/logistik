package de.hsbremen.kss.construction;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.model.Plan;

/**
 * calls a construction method as long as it does not provide a better result.
 * 
 * @author henrik
 * 
 */
public final class MissAbortMultipleRandomConstruction implements Construction {

    /** construction method which is used */
    private final Construction construction;

    /** number of misses to abort */
    private final int maxMisses;

    /**
     * ctor.
     * 
     * @param construction
     *            construction method which is used
     * @param maxMisses
     *            number of misses to abort
     */
    public MissAbortMultipleRandomConstruction(final Construction construction, final int maxMisses) {
        this.construction = construction;
        this.maxMisses = maxMisses;
    }

    @Override
    public Plan constructPlan(final Configuration configuration) {
        Plan bestRandomPlan = null;
        int missCounter = 0;
        while (true) {
            final Plan randomPlan = this.construction.constructPlan(configuration);
            if (bestRandomPlan == null || bestRandomPlan.length() > randomPlan.length()) {
                bestRandomPlan = randomPlan;
                missCounter = 0;
            } else if (++missCounter > this.maxMisses) {
                break;
            }

        }
        return new Plan(MissAbortMultipleRandomConstruction.class, bestRandomPlan);
    }

    @Override
    public void logStatistic() {
        // TODO Auto-generated method stub

    }

}
