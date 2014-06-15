package de.hsbremen.kss.events;

import java.util.List;

import de.hsbremen.kss.fitness.FitnessTest;
import de.hsbremen.kss.model.Plan;

public class NewPopulationEvent {

    public int iteration;

    public FitnessTest fitnessTest;

    public List<Plan> sortedPopulation;

    public NewPopulationEvent(final int iteration, final FitnessTest fitnessTest, final List<Plan> sortedPopulation) {
        this.iteration = iteration;
        this.fitnessTest = fitnessTest;
        this.sortedPopulation = sortedPopulation;
    }

    public double bestFitness() {
        return this.fitnessTest.calculateFitness(this.sortedPopulation.get(0));
    }

    public double worstFitness() {
        return this.fitnessTest.calculateFitness(this.sortedPopulation.get(this.sortedPopulation.size() - 1));
    }

    public double avgFitness() {
        return this.fitnessTest.avgFitness(this.sortedPopulation);
    }
}