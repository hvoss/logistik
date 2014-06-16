package de.hsbremen.kss.genetic;

import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.EventBus;

import de.hsbremen.kss.fitness.CapacityFitnessTest;
import de.hsbremen.kss.fitness.FitnessTest;
import de.hsbremen.kss.fitness.FitnessTestBuilder;
import de.hsbremen.kss.fitness.LengthFitnessTest;
import de.hsbremen.kss.fitness.VehicleFitnessTest;
import de.hsbremen.kss.util.RandomUtils;
import de.hsbremen.kss.validate.SimpleValidator;
import de.hsbremen.kss.validate.Validator;

public class GeneticAlgorithmFactory {

    public static GeneticAlgorithm createGeneticAlgorithm(final EventBus eventBus, final RandomUtils randomUtils) {
        final int maxIterations = 1000;
        final double abortCriterion = 0.001;

        final FitnessTest fitnessTest = new FitnessTestBuilder().addFitnessTest(new LengthFitnessTest()).addFitnessTest(new VehicleFitnessTest())
                .addFitnessTest(new CapacityFitnessTest());

        final List<Mutation> mutationMethods = new ArrayList<>();
        mutationMethods.add(new MoveActionMutationImpl(randomUtils));
        mutationMethods.add(new MoveSubrouteMutation(randomUtils));
        mutationMethods.add(new SwapOrderMutationImpl(randomUtils));
        mutationMethods.add(new AllocateLongestRouteMutationImpl(randomUtils));

        final List<Crossover> crossoverMethods = new ArrayList<>();
        // crossoverMethods.add(new ControlStringCrossoverImpl(randomUtils));

        final Validator validator = new SimpleValidator();
        final PlanComparator planComparator = new PlanComparator(fitnessTest);
        final Selection selectionMethod = new RandomSelection(randomUtils);
        final AbortionCheck abortionCheck = new AbortionCheckImpl(fitnessTest, maxIterations, abortCriterion);
        return new GeneticAlgorithmImpl(eventBus, fitnessTest, randomUtils, mutationMethods, crossoverMethods, validator, planComparator,
                selectionMethod, abortionCheck);
    }
}
