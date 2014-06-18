package de.hsbremen.kss.genetic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.events.NewPopulationEvent;
import de.hsbremen.kss.fitness.FitnessTest;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.util.RandomUtils;
import de.hsbremen.kss.validate.Validator;

/**
 * 
 * @author Oliver Pohling
 * @author Henrik Voß
 * 
 */
public final class GeneticAlgorithmImpl extends Observable implements GeneticAlgorithm {

    /** logging interface */
    private static final Logger LOG = LoggerFactory.getLogger(GeneticAlgorithmImpl.class);

    /** fitness test used for ranking */
    private final FitnessTest fitnessTest;

    /** some util methods for random */
    private final RandomUtils randomUtils;

    /**
     * available mutation methods. one is chosen randomly.
     * 
     * Null-elements are permitted. a null element imply no mutation.
     */
    private final Collection<Mutation> mutationMethods;

    /**
     * available crossover methods. one is chosen randomly.
     * 
     * Null-elements are permitted. a null element imply no crossover.
     */
    private final Collection<Crossover> crossoverMethods;

    /** validator to check a plan */
    private final Validator validator;

    /** method which is used to select parents out of the population */
    private final Selection selectionMethod;

    /** method that checks the possibility of an abort */
    private final AbortionCheck abortionCheck;

    /** the global event bus */
    private final EventBus eventBus;

    /**
     * ctor.
     * 
     * @param fitnessTest
     *            fitness test used for ranking
     * @param randomUtils
     *            some util methods for random
     * @param mutationMethods
     *            available mutation methods. one is chosen randomly.
     * 
     *            Null-elements are permitted. a null element imply no mutation.
     * @param crossoverMethods
     *            available crossover methods. one is chosen randomly.
     * 
     *            Null-elements are permitted. a null element imply no
     *            crossover.
     * @param validator
     *            validator to check a plan
     * @param selectionMethod
     *            method which is used to select parents out of the population
     * @param eventBus
     *            the global event bus
     * @param abortionCheck
     *            method that checks the possibility of an abort
     */
    GeneticAlgorithmImpl(final EventBus eventBus, final FitnessTest fitnessTest, final RandomUtils randomUtils, final List<Mutation> mutationMethods,
            final List<Crossover> crossoverMethods, final Validator validator, final Selection selectionMethod, final AbortionCheck abortionCheck) {
        this.eventBus = eventBus;
        this.fitnessTest = fitnessTest;
        this.randomUtils = randomUtils;
        this.mutationMethods = mutationMethods;
        this.crossoverMethods = crossoverMethods;
        this.validator = validator;
        this.selectionMethod = selectionMethod;
        this.abortionCheck = abortionCheck;
    }

    @Override
    public Plan startOptimize(final Configuration configuration, final Collection<Plan> startPopulation) {
        List<Plan> population = new ArrayList<>(startPopulation);
        final PlanComparator planComparator = new PlanComparator(configuration, this.fitnessTest, this.validator);

        Collections.sort(population, planComparator);

        int i = 0;
        while (!this.abortionCheck.checkAbort(i++, population)) {
            population = createNextPopulation(configuration, planComparator, population);
            logPopulation(i, population);
            this.eventBus.post(new NewPopulationEvent(i, this.fitnessTest, population));
        }

        return population.get(0);
    }

    /**
     * creates the next population out of the old population
     * 
     * @param configuration
     *            the given configuration
     * @param planComparator
     *            comparator used to rank population
     * @param oldPopulation
     *            the old population
     * @return the next population
     */
    private List<Plan> createNextPopulation(final Configuration configuration, final PlanComparator planComparator, final List<Plan> oldPopulation) {
        final List<Plan> nextPopulation = new ArrayList<>(oldPopulation.size() * 2);

        for (int i = 0; i < oldPopulation.size(); i++) {
            final Plan firstParent = this.selectionMethod.select(oldPopulation);
            final Plan secondParent = this.selectionMethod.select(oldPopulation);
            final Plan child = createChild(configuration, firstParent, secondParent);
            nextPopulation.add(child);
        }

        nextPopulation.addAll(oldPopulation);

        Collections.sort(nextPopulation, planComparator);

        return nextPopulation.subList(0, oldPopulation.size());
    }

    /**
     * creates a new child.
     * 
     * @param configuration
     *            the given configuration
     * @param firstParent
     *            the first parent
     * @param secondParent
     *            the second parent
     * @return the created child
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

            child = crossoverChild;
        }

        final Mutation mutationMethod = this.randomUtils.randomElement(this.mutationMethods);
        if (mutationMethod != null) {
            final Plan mutatedChild = mutationMethod.mutate(child);

            child = mutatedChild;
        }

        return child;
    }

    /**
     * logs the population to the logging interface
     * 
     * @param iteration
     *            the actual iteration
     * @param population
     *            the actual population
     */
    private void logPopulation(final int iteration, final List<Plan> population) {
        final Plan bestPlan = population.get(0);
        final Double best = Precision.round(this.fitnessTest.calculateFitness(bestPlan), 2);
        final Double worst = Precision.round(this.fitnessTest.calculateFitness(population.get(population.size() - 1)), 2);
        final Double avg = Precision.round(this.fitnessTest.avgFitness(population), 2);
        final int numTours = bestPlan.getTours().size();
        GeneticAlgorithmImpl.LOG.info("iteration: #" + iteration + ", best: " + best + ", worst:" + worst + ", avg: " + avg + ", numTours: "
                + numTours);
    }
}
