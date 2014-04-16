package de.hsbremen.kss.construction;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.model.Plan;

public final class MultipleRandomConstruction implements Construction {

    private final Construction randomConstruction = new RandomConstruction();

    private final int maxMisses;

    public MultipleRandomConstruction(final int maxMisses) {
        this.maxMisses = maxMisses;
    }

    @Override
    public Plan constructPlan(final Configuration configuration) {
        Plan bestRandomPlan = null;
        int missCounter = 0;
        while (true) {
            final Plan randomPlan = this.randomConstruction.constructPlan(configuration);
            if (bestRandomPlan == null || bestRandomPlan.length() > randomPlan.length()) {
                bestRandomPlan = randomPlan;
                missCounter = 0;
            } else if (++missCounter > this.maxMisses) {
                break;
            }

        }
        return bestRandomPlan;
    }

}
