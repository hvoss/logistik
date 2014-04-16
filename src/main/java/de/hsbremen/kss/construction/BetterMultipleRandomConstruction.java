package de.hsbremen.kss.construction;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.model.Plan;

public final class BetterMultipleRandomConstruction implements Construction {

    private final Construction randomConstruction = new RandomConstruction();

    private final int numOfRandomPlans;

    public BetterMultipleRandomConstruction(final int numOfRandomPlans) {
        this.numOfRandomPlans = numOfRandomPlans;
    }

    @Override
    public Plan constructPlan(final Configuration configuration) {
        Plan bestRandomPlan = null;
        for (int i = 0; i < this.numOfRandomPlans; i++) {
            final Plan randomPlan = this.randomConstruction.constructPlan(configuration);
            if (bestRandomPlan == null || bestRandomPlan.length() > randomPlan.length()) {
                bestRandomPlan = randomPlan;
            }
        }
        return bestRandomPlan;
    }

}
