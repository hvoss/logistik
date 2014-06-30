package de.hsbremen.kss.genetic.abortion;

import java.util.List;

import de.hsbremen.kss.genetic.fitness.FitnessTest;
import de.hsbremen.kss.model.Plan;

public final class AbortionCheckImpl implements AbortionCheck {

    /** maximum iterations to generate population */
    private final int maxIterations;

    /**
     * abort criterion factor. relative deviation from the best population to
     * the average.
     */
    private final double abortCriterion;

    /** fitness test used for ranking */
    private final FitnessTest fitnessTest;

    /**
     * ctor
     * 
     * @param fitnessTest
     *            fitness test used for ranking
     * @param maxIterations
     *            maximum iterations to generate population
     * @param abortCriterion
     *            abort criterion factor. relative deviation from the best
     *            population to the average.
     */
    public AbortionCheckImpl(final FitnessTest fitnessTest, final int maxIterations, final double abortCriterion) {
        this.fitnessTest = fitnessTest;
        this.maxIterations = maxIterations;
        this.abortCriterion = abortCriterion;
    }

    /**
     * checks if the average fitness of population equals almost the best
     * fitness.
     * 
     * @param population
     *            the actual sorted population
     * @return true: average and best fitness almost equal
     */
    private boolean checkAverageEqualBest(final List<Plan> population) {
        final double bestFiness = this.fitnessTest.calculateFitness(population.get(0));
        final double avgFiness = this.fitnessTest.avgFitness(population);
        if (avgFiness > 0) {
            final double factor = 1 - bestFiness / avgFiness;
            return factor < this.abortCriterion;
        }
        return true;
    }

    @Override
    public boolean checkAbort(final int iteration, final List<Plan> population) {
        if (iteration >= this.maxIterations) {
            return true;
        }

        if (checkAverageEqualBest(population)) {
            return true;
        }

        return false;
    }
}
