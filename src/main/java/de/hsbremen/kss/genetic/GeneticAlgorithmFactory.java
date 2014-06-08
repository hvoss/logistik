package de.hsbremen.kss.genetic;

import java.util.Arrays;
import java.util.List;

import com.google.common.eventbus.EventBus;

import de.hsbremen.kss.fitness.FitnessTest;
import de.hsbremen.kss.fitness.LengthFitnessTest;
import de.hsbremen.kss.util.RandomUtils;
import de.hsbremen.kss.validate.SimpleValidator;
import de.hsbremen.kss.validate.Validator;

public class GeneticAlgorithmFactory {

    public static GeneticAlgorithm createGeneticAlgorithm(final EventBus eventBus, final RandomUtils randomUtils) {
        final int maxIterations = 1000;
        final double abortCriterion = 0.001;
        final FitnessTest fitnessTest = new LengthFitnessTest();
        final List<Mutation> mutationMethods = Arrays.asList((Mutation) new MoveActionMutationImpl(randomUtils));
        final List<Crossover> crossoverMethods = Arrays.asList((Crossover) new ControlStringCrossoverImpl(randomUtils));
        final Validator validator = new SimpleValidator();
        final PlanComparator planComparator = new PlanComparator(fitnessTest);
        final Selection selectionMethod = new RandomSelection(randomUtils);
        final AbortionCheck abortionCheck = new AbortionCheckImpl(fitnessTest, maxIterations, abortCriterion);
        return new GeneticAlgorithmImpl(eventBus, fitnessTest, randomUtils, mutationMethods, crossoverMethods, validator, planComparator,
                selectionMethod, abortionCheck);
    }
}
