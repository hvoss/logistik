package de.hsbremen.kss.genetic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.kss.configuration.Configuration;
import de.hsbremen.kss.fitness.FitnessTest;
import de.hsbremen.kss.fitness.LengthFitnessTest;
import de.hsbremen.kss.model.Plan;
import de.hsbremen.kss.util.RandomUtils;

/**
 * 
 * @author olli
 * 
 */

public final class GeneticAlgorithmImpl implements GeneticAlgorithm {

    /** logging interface */
    private static final Logger LOG = LoggerFactory.getLogger(GeneticAlgorithmImpl.class);

    // Constans
    private final int SELECTION_RANGE_POPULATION = 100; // Welche Eltern
                                                        // erzeugen Nachkommen
                                                        // (Die 10 Besten)
    private final double MUTATION_RATE = 1;
    private final double CROSSOVER_RATE = 1;
    private final double FIFTY_FIFTY = 0.5;

    private double durchschnittsFitness = Double.MAX_VALUE;

    private final FitnessTest fitnessTest;
    private List<Plan> population;

    private final RandomUtils randomUtils = new RandomUtils(0);

    private final List<Mutation> mutationMethods = new ArrayList<>();

    private final List<Crossover> crossoverMethods = new ArrayList<>();

    public GeneticAlgorithmImpl(final Configuration configuration, final Collection<Plan> plans) {
        this.population = new ArrayList<Plan>(plans);
        this.fitnessTest = new LengthFitnessTest();

        this.mutationMethods.add(new MoveActionMutationImpl(this.randomUtils));
        this.crossoverMethods.add(new ControlStringCrossoverImpl(this.randomUtils));
    }

    @Override
    public Plan startOptimize() {

        this.population = rankPopulation(this.population);
        Plan bestplan = this.population.get(0);

        int count = 0;
        while (Math.round(this.durchschnittsFitness) > Math.round(this.fitnessTest.calculateFitness(bestplan))) {
            // GeneticAlgorithmImpl.LOG.info("Durchschnittsfitness: " +
            // this.durchschnittsFitness);
            GeneticAlgorithmImpl.LOG.info("Beste Fitness: " + bestplan.length());
            bestplan = optimize().get(0);

            if (count++ > 10) {
                break;
            }
        }
        GeneticAlgorithmImpl.LOG.info("ENDE Durchschnittsfitness: " + this.durchschnittsFitness);
        GeneticAlgorithmImpl.LOG.info("ENDE Beste Fitness: " + this.fitnessTest.calculateFitness(bestplan));
        return bestplan;
    }

    public List<Plan> optimize() {

        this.durchschnittsFitness = 0;

        List<Plan> nextPopulation = createNewPopulation(this.population);
        nextPopulation = rankPopulation(nextPopulation);
        this.population = rankPopulation(this.population);
        List<Plan> newStartPopulation = new ArrayList<>(100);
        newStartPopulation.addAll(this.population);
        newStartPopulation.addAll(nextPopulation);

        logPopulation("old", this.population);
        logPopulation("new", nextPopulation);

        newStartPopulation = rankPopulation(newStartPopulation);

        this.population = newStartPopulation.subList(0, 100);

        for (final Plan plan : this.population) {
            this.durchschnittsFitness += plan.length();
        }

        this.durchschnittsFitness = this.durchschnittsFitness / this.population.size();

        return this.population;
    }

    /**
     * 
     */
    private List<Plan> rankPopulation(final List<Plan> populationToRank) {
        Collections.sort(populationToRank, new Comparator<Plan>() {

            @Override
            public int compare(final Plan plan1, final Plan plan2) {
                if (plan1.length() < plan2.length()) {
                    return -1;
                } else if (plan1.length() > plan2.length()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return populationToRank;
    }

    /**
     * Wahrscheinlichkeit für ersten 50
     */
    private List<Plan> createNewPopulation(final List<Plan> startPopulation) {
        final List<Plan> newGeneration = new ArrayList<Plan>();
        for (int i = 0; i < startPopulation.size(); i++) {
            final Plan parent1 = this.randomUtils.randomElementByLinearDistribution(startPopulation);
            final Plan parent2 = this.randomUtils.randomElementByLinearDistribution(startPopulation);
            newGeneration.add(createChild(parent1, parent2));
        }
        return newGeneration;
    }

    /**
     * 
     * @param parent1
     *            Parent1
     * @param parent2
     *            Parent2
     * @return child
     */
    private Plan createChild(final Plan parent1, final Plan parent2) {

        Plan child;

        if (Math.random() <= this.CROSSOVER_RATE) {
            final Crossover crossover = this.randomUtils.randomElement(this.crossoverMethods);
            child = crossover.crossover(parent1, parent2);
        } else if (this.randomUtils.randomBoolean()) {
            child = parent1;
        } else {
            child = parent2;
        }

        if (Math.random() <= this.MUTATION_RATE) {
            final Mutation mutation = this.randomUtils.randomElement(this.mutationMethods);
            child = mutation.mutate(child);
        }

        return child;
    }

    private void logPopulation(final String name, final List<Plan> population) {
        final Double best = this.fitnessTest.calculateFitness(population.get(0));
        final Double worst = this.fitnessTest.calculateFitness(population.get(population.size() - 1));
        GeneticAlgorithmImpl.LOG.info("Popuplation " + name + ": best: " + best + ", worst:" + worst);
    }
}
