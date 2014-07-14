package de.hsbremen.kss.genetic.selection;

import java.util.List;

import org.apache.commons.lang3.Validate;

import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.util.RandomUtils;

public class OnlyTheBestSelection implements Selection {

    private final RandomUtils randomUtils;

    private final double best;

    public OnlyTheBestSelection(final RandomUtils randomUtils, final double best) {
        Validate.inclusiveBetween(0, 1, best);
        this.randomUtils = randomUtils;
        this.best = best;
    }

    @Override
    public Plan select(final List<Plan> sortedPopulation) {
        final int num = (int) (sortedPopulation.size() * this.best);

        return this.randomUtils.randomElement(sortedPopulation.subList(0, num));
    }

}
