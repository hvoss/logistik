package de.hsbremen.kss.genetic;

import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.EventBus;

import de.hsbremen.kss.fitness.CapacityFitnessTest;
import de.hsbremen.kss.fitness.FitnessTest;
import de.hsbremen.kss.fitness.FitnessTestBuilder;
import de.hsbremen.kss.fitness.LengthFitnessTest;
import de.hsbremen.kss.fitness.LoadingFitnessTest;
import de.hsbremen.kss.fitness.VehicleFitnessTest;
import de.hsbremen.kss.fitness.VehicleMakespanFitnessTest;
import de.hsbremen.kss.util.RandomUtils;
import de.hsbremen.kss.validate.RightOrderValidatorImpl;
import de.hsbremen.kss.validate.Validator;

public class GeneticAlgorithmFactory {

    public static GeneticAlgorithm createGeneticAlgorithm(final EventBus eventBus, final RandomUtils randomUtils) {
        final int maxIterations = 5000;
        final double abortCriterion = 0.0001;

        final FitnessTest fitnessTest = new FitnessTestBuilder().addFitnessTest(new LengthFitnessTest()).addFitnessTest(new VehicleFitnessTest())
                .addFitnessTest(new CapacityFitnessTest()).addFitnessTest(new VehicleMakespanFitnessTest()).addFitnessTest(new LoadingFitnessTest());

        final MutationBuilder mutationBuilder = new MutationBuilder(randomUtils);

        //@formatter:off
        final List<Mutation> mutationMethods = mutationBuilder
            .moveActionMutation(20)
            .moveSubrouteMutation(10)
            .swapOrderMutation(10)
            .allocateLongestRouteMutation(2)
            .allocateRandomRouteMutation(2)
            .allocateShortestRouteMutation(2)
            .combineTwoToursMutationImpl(1)
//            .nullMutation(10)
            .build();
        //@formatter:on10574

        final List<Crossover> crossoverMethods = new ArrayList<>();
//        crossoverMethods.add(new ControlStringCrossoverImpl(randomUtils));

        final Validator validator = new RightOrderValidatorImpl();
        validator.enableLogging(false);
        Selection selectionMethod = new RandomSelection(randomUtils);
//        selectionMethod = new LinearDistributionSelectionImpl(randomUtils);

        final AbortionCheck abortionCheck = new AbortionCheckImpl(fitnessTest, maxIterations, abortCriterion);
        return new GeneticAlgorithmImpl(eventBus, fitnessTest, randomUtils, mutationMethods, crossoverMethods, validator, selectionMethod,
                abortionCheck);
    }
}
