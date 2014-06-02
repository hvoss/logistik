package de.hsbremen.kss.genetic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.fitness.FitnessTest;
import de.hsbremen.kss.fitness.LengthFitnessTest;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.util.RandomUtils;
import de.hsbremen.kss.validate.SimpleValidator;
import de.hsbremen.kss.validate.Validator;

/**
 * 
 * @author olli
 * 
 */

public final class GeneticAlgorithmImpl implements GeneticAlgorithm {

    /** logging interface */
    private static final Logger LOG = LoggerFactory.getLogger(GeneticAlgorithmImpl.class);

    private final double mutationRate = 0;

    private final double crossoverRate = 1;

    private final int maxIterations = 1000;

    private final double abortCriterion = 0.01;

    private final FitnessTest fitnessTest = new LengthFitnessTest();

    private final RandomUtils randomUtils = new RandomUtils(0);

    private final List<Mutation> mutationMethods = new ArrayList<>();

    private final List<Crossover> crossoverMethods = new ArrayList<>();

    private final Validator validator = new SimpleValidator();

    public GeneticAlgorithmImpl() {
        this.mutationMethods.add(new MoveActionMutationImpl(this.randomUtils));
        this.crossoverMethods.add(new ControlStringCrossoverImpl(this.randomUtils));
    }

    @Override
    public Plan startOptimize(final Configuration configuration, final Collection<Plan> startPopulation) {

        final PlanComparator planComparator = new PlanComparator(this.validator, configuration);

        List<Plan> population = new ArrayList<>(startPopulation);
        Collections.sort(population, planComparator);

        for (int i = 0; i < this.maxIterations && checkNotAbort(population, this.abortCriterion); i++) {
            population = optimize(planComparator, population);
            logPopulation(i, population);
        }

        return population.get(0);
    }

    private boolean checkNotAbort(final List<Plan> population, final double abortCriterion) {
        final double bestFiness = this.fitnessTest.calculateFitness(population.get(0));
        final double avgFiness = this.fitnessTest.avgFitness(population);
        final double factor = 1 - bestFiness / avgFiness;
        return factor > abortCriterion;
    }

    public List<Plan> optimize(final PlanComparator planComparator, final List<Plan> population) {
        final List<Plan> nextPopulation = new ArrayList<>(population.size() * 2);

        for (int i = 0; i < population.size(); i++) {
            final Plan firstParent = this.randomUtils.randomElementByLinearDistribution(population);
            final Plan secondParent = this.randomUtils.randomElementByLinearDistribution(population);
            final Plan child = createChild(firstParent, secondParent);
            nextPopulation.add(child);
        }

        nextPopulation.addAll(population);

        Collections.sort(nextPopulation, planComparator);

        return nextPopulation.subList(0, population.size());
    }

    /**
     * 
     * @param firstParent
     *            Parent1
     * @param secondParent
     *            Parent2
     * @return child
     */
    private Plan createChild(final Plan firstParent, final Plan secondParent) {

        Plan child;

        if (this.randomUtils.randomBoolean(this.crossoverRate)) {
            final Crossover crossover = this.randomUtils.randomElement(this.crossoverMethods);
            child = crossover.crossover(firstParent, secondParent);
        } else if (this.randomUtils.randomBoolean()) {
            child = firstParent;
        } else {
            child = secondParent;
        }

        if (this.randomUtils.randomBoolean(this.mutationRate)) {
            final Mutation mutation = this.randomUtils.randomElement(this.mutationMethods);
            child = mutation.mutate(child);
        }

        return child;
    }

    private void logPopulation(final int iteration, final List<Plan> population) {
        final Double best = Precision.round(this.fitnessTest.calculateFitness(population.get(0)), 2);
        final Double worst = Precision.round(this.fitnessTest.calculateFitness(population.get(population.size() - 1)), 2);
        final Double avg = Precision.round(this.fitnessTest.avgFitness(population), 2);
        GeneticAlgorithmImpl.LOG.info("iteration: #" + iteration + ", best: " + best + ", worst:" + worst + ", avg: " + avg);
    }
}
