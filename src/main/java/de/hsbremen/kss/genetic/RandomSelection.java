package de.hsbremen.kss.genetic;

import java.util.List;

import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.util.RandomUtils;

public class RandomSelection implements Selection {

    private final RandomUtils randomUtils;

    public RandomSelection(final RandomUtils randomUtils) {
        this.randomUtils = randomUtils;
    }

    @Override
    public Plan select(final List<Plan> sortedPopulation) {
        return this.randomUtils.randomElement(sortedPopulation);
    }

}
