package de.hsbremen.kss.genetic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.construction.Construction;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.util.RandomUtils;

public class PopulationGeneratorImpl {

    private final RandomUtils randomUtils;

    public PopulationGeneratorImpl(final RandomUtils randomUtils) {
        this.randomUtils = randomUtils;
    }

    public List<Plan> createPopulation(final Configuration configuration, final Collection<Construction> constructionMethods, final int number) {
        final List<Plan> population = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            final Construction construction = this.randomUtils.randomElement(constructionMethods);
            final Plan plan = construction.constructPlan(configuration);
            population.add(plan);
        }
        return population;
    }
}
