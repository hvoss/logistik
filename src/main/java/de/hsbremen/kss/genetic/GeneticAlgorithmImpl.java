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

    private final double mutationRate = 1;

    private final double crossoverRate = 1;

    private final int maxIterations = 1000;

    private final double abortCriterion = 0.001;

    private final FitnessTest fitnessTest = new LengthFitnessTest();

    private final RandomUtils randomUtils = new RandomUtils(0);

    private final List<Mutation> mutationMethods = new ArrayList<>();

    private final List<Crossover> crossoverMethods = new ArrayList<>();

    private final Validator validator = new SimpleValidator();

    private final PlanComparator planComparator = new PlanComparator(this.fitnessTest);

    private final Selection selectionMethod = new RandomSelection(this.randomUtils);

    private GeneticAlgorithmListener listener;

    public GeneticAlgorithmImpl() {
        this.mutationMethods.add(new MoveActionMutationImpl(this.randomUtils));
        this.crossoverMethods.add(new ControlStringCrossoverImpl(this.randomUtils));
    }

    @Override
    public Plan startOptimize(final Configuration configuration, final Collection<Plan> startPopulation) {

        List<Plan> population = new ArrayList<>(startPopulation);
        Collections.sort(population, this.planComparator);

        for (int i = 0; i < this.maxIterations && checkNotAbort(population, this.abortCriterion); i++) {
            population = optimize(configuration, population);
            this.listener.newPlan(population.get(0));
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

    public List<Plan> optimize(final Configuration configuration, final List<Plan> population) {
        final List<Plan> nextPopulation = new ArrayList<>(population.size() * 2);

        for (int i = 0; i < population.size(); i++) {
            final Plan firstParent = this.selectionMethod.select(population);
            final Plan secondParent = this.selectionMethod.select(population);
            final Plan child = createChild(configuration, firstParent, secondParent);
            nextPopulation.add(child);
        }

        nextPopulation.addAll(population);

        Collections.sort(nextPopulation, this.planComparator);

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
    private Plan createChild(final Configuration configuration, final Plan firstParent, final Plan secondParent) {

        Plan child;

        if (this.randomUtils.randomBoolean()) {
            child = firstParent;
        } else {
            child = secondParent;
        }

        final Crossover crossoverMethod = this.randomUtils.randomElement(this.crossoverMethods);
        if (crossoverMethod != null) {
            final Plan crossoverChild = crossoverMethod.crossover(firstParent, secondParent);

            if (this.validator.validate(configuration, crossoverChild)) {
                child = crossoverChild;
            }
        }

        final Mutation mutationMethod = this.randomUtils.randomElement(this.mutationMethods);
        if (mutationMethod != null) {
            final Plan mutatedChild = mutationMethod.mutate(child);

            if (this.validator.validate(configuration, mutatedChild)) {
                child = mutatedChild;
            }
        }

        return child;
    }

    private void logPopulation(final int iteration, final List<Plan> population) {
        final Double best = Precision.round(this.fitnessTest.calculateFitness(population.get(0)), 2);
        final Double worst = Precision.round(this.fitnessTest.calculateFitness(population.get(population.size() - 1)), 2);
        final Double avg = Precision.round(this.fitnessTest.avgFitness(population), 2);
        GeneticAlgorithmImpl.LOG.info("iteration: #" + iteration + ", best: " + best + ", worst:" + worst + ", avg: " + avg);
    }

    @Override
    public void setListener(final GeneticAlgorithmListener listener) {
        this.listener = listener;
    }
}
