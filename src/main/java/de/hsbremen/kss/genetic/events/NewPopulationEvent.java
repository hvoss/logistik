package de.hsbremen.kss.genetic.events;

import java.util.List;
import java.util.Map;

import de.hsbremen.kss.genetic.fitness.FitnessTest;
import de.hsbremen.kss.genetic.fitness.FitnessTestBuilder;
import de.hsbremen.kss.model.Plan;

public class NewPopulationEvent {

    public int iteration;

    public FitnessTestBuilder fitnessTest;

    public List<Plan> sortedPopulation;

    public NewPopulationEvent(final int iteration, final FitnessTest fitnessTest, final List<Plan> sortedPopulation) {
        this.iteration = iteration;
        this.fitnessTest = (FitnessTestBuilder) fitnessTest;
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

    public double bestLength() {
        return this.sortedPopulation.get(0).length();
    }

    public double worstLength() {
        return this.sortedPopulation.get(this.sortedPopulation.size() - 1).length();
    }

    public double avgLength() {
        double avgLength = 0.0;
        for (final Plan plan : this.sortedPopulation) {
            avgLength += plan.length() / this.sortedPopulation.size();
        }
        return avgLength;
    }

    public double bestDelayTime() {
        return bestPlan().delayTime();
    }

    public double worstDelayTime() {
        return this.sortedPopulation.get(this.sortedPopulation.size() - 1).delayTime();
    }

    public double avgDelayTime() {
        double avgDelayTime = 0.0;
        for (final Plan plan : this.sortedPopulation) {
            avgDelayTime += plan.delayTime() / this.sortedPopulation.size();
        }
        return avgDelayTime;
    }

    public double bestWaitingyTime() {
        return this.sortedPopulation.get(0).waitingTime();
    }

    public double worstWaitingTime() {
        return this.sortedPopulation.get(this.sortedPopulation.size() - 1).waitingTime();
    }

    public double avgWaitingTime() {
        double avgWaitingTime = 0.0;
        for (final Plan plan : this.sortedPopulation) {
            avgWaitingTime += plan.waitingTime() / this.sortedPopulation.size();
        }
        return avgWaitingTime;
    }

    public Map<Class<? extends FitnessTest>, Double> fitnessDistribution(final Plan plan) {
        return this.fitnessTest.fitnessDistribution(plan);
    }

    public Plan bestPlan() {
        return this.sortedPopulation.get(0);
    }

}
